# Tasks: Frontend

## Epic

- **Epic ID**: EPIC-4
- **Status**: Draft

## References

| ID | Name |
|-----|------|
| [FR-2](../requirements/profile.md#fr-2) | Profile Attributes |
| [FR-5](../requirements/profile.md#fr-5) | Profile API |
| [FR-6](../requirements/rbac.md#fr-6) | Roles Management |
| [FR-7](../requirements/rbac.md#fr-7) | Groups Management |
| [FR-8](../requirements/rbac.md#fr-8) | User Role Assignment |
| [UJ-2](../requirements/profile.md#uj-2) | Profile Enrichment |
| [UJ-3](../requirements/rbac.md#uj-3) | Admin Role Assignment |
| [ADR-8](../design/adrs.md#adr-8-react-mantine-vite-frontend) | React + Mantine + Vite frontend |

## User Stories

### Story 1: API client and hooks

- **Story ID**: EPIC-4-S1
- **Estimate**: 4h
- **Description**: Implement the API client layer (`api/client.ts`, `api/profile.ts`, `api/users.ts`, `api/roles.ts`, `api/groups.ts`) and TanStack Query hooks (`useProfile`, `useUsers`, `useRoles`, `useGroups`). Define TypeScript types for all API responses.
- **Dependencies**: EPIC-1-S4
- **Acceptance Criteria**:
  - [ ] All API endpoints have typed client functions
  - [ ] TanStack Query hooks for queries and mutations
  - [ ] Error handling returns typed Problem Detail
  - [ ] Types match the REST architecture spec

### Story 2: Profile page

- **Story ID**: EPIC-4-S2
- **Estimate**: 4h
- **Description**: Build the "My Profile" page at `/user-manager/`. Display current profile data (name, email, photo, social links). Edit form with validation (phone format). Success/error notifications on save.
- **Dependencies**: EPIC-4-S1
- **Acceptance Criteria**:
  - [ ] Profile data displayed on load
  - [ ] Edit form with all social fields
  - [ ] Phone/WhatsApp validated on blur
  - [ ] Email displayed but not editable
  - [ ] Success toast on save, error toast on failure
  - [ ] Loading indicator while fetching

### Story 3: Users and User Detail pages

- **Story ID**: EPIC-4-S3
- **Estimate**: 8h
- **Description**: Build the "Users" page (paginated table of all users with roles/groups) and "User Detail" page (assign roles and groups to a user via multi-select). Both protected by `AdminGuard`.
- **Dependencies**: EPIC-4-S1
- **Acceptance Criteria**:
  - [ ] Users table with name, email, roles, groups columns
  - [ ] Pagination controls
  - [ ] Click row navigates to user detail
  - [ ] User detail shows profile info + role/group assignment
  - [ ] Multi-select for roles and groups with save button
  - [ ] Delete user button with confirmation modal
  - [ ] Non-admin sees "Access Denied"

### Story 4: Roles page

- **Story ID**: EPIC-4-S4
- **Estimate**: 4h
- **Description**: Build the "Roles" page with a table listing all roles and their actions. Create/edit modal with role name and action tags input. Delete with confirmation. System roles show as non-deletable.
- **Dependencies**: EPIC-4-S1
- **Acceptance Criteria**:
  - [ ] Roles table with name, actions count, system badge
  - [ ] Create button opens modal with name + action tags input
  - [ ] Actions validated on input (lowercase, no spaces)
  - [ ] Edit updates name and actions
  - [ ] Delete disabled for system roles
  - [ ] Confirmation modal on delete

### Story 5: Groups page

- **Story ID**: EPIC-4-S5
- **Estimate**: 4h
- **Description**: Build the "Groups" page with a table listing all groups and their assigned roles. Create/edit modal with group name, description, and role multi-select. Delete with confirmation.
- **Dependencies**: EPIC-4-S1, EPIC-4-S4
- **Acceptance Criteria**:
  - [ ] Groups table with name, description, role count
  - [ ] Create button opens modal with name + description + role multi-select
  - [ ] Edit updates all fields including role assignments
  - [ ] Delete with confirmation modal
  - [ ] Success/error notifications
