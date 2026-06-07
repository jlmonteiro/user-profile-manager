# REST Architecture

## Base Path

All API endpoints are served under `/user-manager/api/v1/`.

**Validates:** [FR-5](../requirements/profile.md#fr-5), [FR-6](../requirements/rbac.md#fr-6), [FR-7](../requirements/rbac.md#fr-7), [FR-8](../requirements/rbac.md#fr-8), [FR-11](../requirements/rbac.md#fr-11)

## Authentication

Every request must include the `X-Auth-Request-Email` header (set by oauth2-proxy). The backend extracts the email and resolves the user. If the header is missing, the backend returns `401 Unauthorized`.

Admin-only endpoints additionally check that the resolved user has the `admin` role. If not, `403 Forbidden` is returned.

## Endpoints

### Profile

| Method | Path | Access | Description | Validates |
|--------|------|--------|-------------|-----------|
| `GET` | `/me` | Any user | Get own profile with resolved roles and actions | FR-5, FR-11 |
| `PUT` | `/me` | Any user | Update own profile (social links, contacts) | FR-2 |
| `DELETE` | `/users/{id}` | Admin | Delete a user profile | FR-4 |

### Users

| Method | Path | Access | Description | Validates |
|--------|------|--------|-------------|-----------|
| `GET` | `/users` | Admin | List all users (paginated) | FR-5 |
| `GET` | `/users/{id}` | Admin | Get user detail with roles/groups | FR-5 |
| `PUT` | `/users/{id}/roles` | Admin | Set direct roles for a user | FR-8 |
| `PUT` | `/users/{id}/groups` | Admin | Set group memberships for a user | FR-8 |

### Roles

| Method | Path | Access | Description | Validates |
|--------|------|--------|-------------|-----------|
| `GET` | `/roles` | Admin | List all roles | FR-6 |
| `POST` | `/roles` | Admin | Create a role (with optional actions) | FR-6 |
| `PUT` | `/roles/{id}` | Admin | Update role name and actions | FR-6 |
| `DELETE` | `/roles/{id}` | Admin | Delete a role (not system roles) | FR-6 |

### Groups

| Method | Path | Access | Description | Validates |
|--------|------|--------|-------------|-----------|
| `GET` | `/groups` | Admin | List all groups | FR-7 |
| `POST` | `/groups` | Admin | Create a group | FR-7 |
| `PUT` | `/groups/{id}` | Admin | Update group (name, description, roles) | FR-7 |
| `DELETE` | `/groups/{id}` | Admin | Delete a group | FR-7 |

## Request/Response Examples

### GET /user-manager/api/v1/me

**Response 200:**
```json
{
  "id": "019050a0-0000-7000-0000-000000000001",
  "email": "jorge@example.com",
  "name": "Jorge Monteiro",
  "photo": "https://lh3.googleusercontent.com/...",
  "phone": "+351912345678",
  "instagram": "jorge.monteiro",
  "facebook": null,
  "linkedin": "jlmonteiro",
  "whatsapp": "+351912345678",
  "telegram": "jorge_m",
  "roles": ["admin", "shopping-editor"],
  "actions": ["shopping:create", "shopping:read", "shopping:export-data"],
  "createdAt": "2026-06-07T14:00:00Z"
}
```

### PUT /user-manager/api/v1/me

**Request:**
```json
{
  "name": "Jorge Monteiro",
  "photo": "https://lh3.googleusercontent.com/...",
  "phone": "+351912345678",
  "instagram": "jorge.monteiro",
  "facebook": null,
  "linkedin": "jlmonteiro",
  "whatsapp": "+351912345678",
  "telegram": "jorge_m"
}
```

**Response 200:** Same as GET `/me` response.

### POST /user-manager/api/v1/roles

**Request:**
```json
{
  "name": "shopping-editor",
  "actions": ["shopping:create", "shopping:read", "shopping:update", "shopping:export-data"]
}
```

**Response 201:**
```json
{
  "id": "019050a0-0000-7000-0000-000000000010",
  "name": "shopping-editor",
  "isSystem": false,
  "actions": ["shopping:create", "shopping:read", "shopping:update", "shopping:export-data"],
  "createdAt": "2026-06-07T15:00:00Z",
  "updatedAt": "2026-06-07T15:00:00Z"
}
```

### PUT /user-manager/api/v1/users/{id}/roles

**Request:**
```json
{
  "roleIds": [
    "019050a0-0000-7000-0000-000000000010",
    "019050a0-0000-7000-0000-000000000011"
  ]
}
```

**Response 200:** Updated user detail with roles.

### GET /user-manager/api/v1/users (paginated)

**Response 200:**
```json
{
  "data": [
    {
      "id": "019050a0-0000-7000-0000-000000000001",
      "email": "jorge@example.com",
      "name": "Jorge Monteiro",
      "photo": "https://...",
      "roles": ["admin"],
      "groups": ["family-adults"]
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 3,
    "totalPages": 1
  }
}
```

## Error Handling

All errors use RFC 7807 Problem Detail:

```json
{
  "type": "https://home.monteiro.net/problems/not-found",
  "title": "Not Found",
  "status": 404,
  "detail": "User with id '019050a0-...' not found",
  "instance": "/user-manager/api/v1/users/019050a0-..."
}
```

### Error Codes

| Status | When |
|--------|------|
| `400` | Validation error (invalid action format, missing required field) |
| `401` | Missing `X-Auth-Request-Email` header |
| `403` | Non-admin accessing admin-only endpoint |
| `404` | Resource not found |
| `409` | Conflict (duplicate role name, duplicate group name) |
| `422` | Business rule violation (deleting system role, deleting last admin) |

## Pagination

List endpoints accept:
- `page` (default: 0) — zero-based page number
- `size` (default: 20, max: 100) — page size

Response envelope:
```json
{
  "data": [...],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 142,
    "totalPages": 8
  }
}
```

## Conventions

- JSON request/response (`application/json`)
- Dates in ISO 8601 UTC (`2026-06-07T15:00:00Z`)
- UUIDv7 string representation for all IDs
- Consistent field naming: camelCase in JSON, snake_case in DB
- `PUT` for full replacement of sub-resources (roles, groups on a user)
- `POST` for creation, `DELETE` for removal
