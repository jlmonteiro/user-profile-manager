# Frontend Architecture

## Overview

The user-profile-manager frontend is a micro-frontend loaded by the launcher shell via ES module parcel. It renders inside the launcher's content area (embedded mode — no competing AppShell).

**Validates:** [FR-2](../requirements/profile.md#fr-2), [FR-5](../requirements/profile.md#fr-5), [FR-6](../requirements/rbac.md#fr-6), [FR-7](../requirements/rbac.md#fr-7), [FR-8](../requirements/rbac.md#fr-8)

## Integration with Launcher

```
Base path: /user-manager/
Entry point: /user-manager/assets/index.js
Launcher section: Security
```

### Parcel Lifecycle

```typescript
// src/parcel.tsx
export { bootstrap, mount, unmount } from './lifecycle'
```

The app exports lifecycle hooks consumed by the launcher's `ParcelLoader`:
- `bootstrap()` — one-time initialization
- `mount(container: HTMLElement)` — render into the given DOM node
- `unmount()` — cleanup and unmount React root

### Vite Config (lib mode)

```typescript
build: {
  lib: {
    entry: 'src/parcel.tsx',
    formats: ['es'],
    fileName: () => 'assets/index.js',
  },
}
```

## Pages & Routing

| Route | Page | Access | Description |
|-------|------|--------|-------------|
| `/user-manager/` | My Profile | Any user | View/edit own profile |
| `/user-manager/users` | Users | Admin | List all users |
| `/user-manager/users/:id` | User Detail | Admin | Manage user roles/groups |
| `/user-manager/roles` | Roles | Admin | CRUD roles with actions |
| `/user-manager/groups` | Groups | Admin | CRUD groups with role assignments |

## Component Structure

```
frontend/src/
├── api/                    # API client functions
│   ├── client.ts           # Base fetch wrapper
│   ├── profile.ts          # /me endpoints
│   ├── users.ts            # /users endpoints
│   ├── roles.ts            # /roles endpoints
│   └── groups.ts           # /groups endpoints
├── components/             # Shared components
│   └── AdminGuard.tsx      # Redirects non-admin users
├── hooks/                  # TanStack Query hooks
│   ├── useProfile.ts       # useQuery/useMutation for profile
│   ├── useUsers.ts
│   ├── useRoles.ts
│   └── useGroups.ts
├── pages/                  # Page components
│   ├── ProfilePage.tsx
│   ├── UsersPage.tsx
│   ├── UserDetailPage.tsx
│   ├── RolesPage.tsx
│   └── GroupsPage.tsx
├── types/                  # TypeScript interfaces
│   └── index.ts
├── App.tsx                 # Router and layout
├── main.tsx                # Standalone dev entry
├── lifecycle.tsx           # Parcel lifecycle hooks
└── parcel.tsx              # Lib entry (re-exports lifecycle)
```

## State Management

- **Server state:** TanStack Query (React Query) for all API data
- **Local state:** React `useState` for UI-only state (modals, form visibility)
- **No global state library** — follows the same pattern as finances

## Admin Guard

Pages under `/user-manager/users`, `/user-manager/roles`, `/user-manager/groups` require admin access. The `AdminGuard` component:
1. Calls `GET /user-manager/api/v1/me`
2. Checks if `roles` includes `admin`
3. If not admin → shows "Access Denied" message
4. If admin → renders children

## Launcher Registration

**apps.json entry:**
```json
{
  "label": "Security",
  "icon": "shield-lock",
  "apps": [
    {
      "id": "user-manager",
      "label": "User & Profile Manager",
      "icon": "users",
      "href": "/user-manager/",
      "entry": "/user-manager/assets/index.js",
      "search": [
        { "label": "My Profile", "description": "Security > My Profile", "href": "/user-manager/" },
        { "label": "Users", "description": "Security > Users", "href": "/user-manager/users" },
        { "label": "Roles", "description": "Security > Roles", "href": "/user-manager/roles" },
        { "label": "Groups", "description": "Security > Groups", "href": "/user-manager/groups" }
      ]
    }
  ]
}
```

## Development

```bash
# Standalone dev mode (with mock data)
cd frontend && npm run dev

# Build for launcher integration
cd frontend && npm run build
```

In dev mode, `main.tsx` renders the app in standalone mode with a mock `X-Auth-Request-Email` identity (similar to how finances handles dev auth).
