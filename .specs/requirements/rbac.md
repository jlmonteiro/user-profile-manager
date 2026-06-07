# Requirements: RBAC

## 1. User Journeys

### UJ-3: Admin Role Assignment

1. Admin navigates to "Users" page from the Security section in the launcher sidebar.
2. Admin sees a list of all registered users with their current roles and groups.
3. Admin clicks on a user to open their detail page.
4. Admin assigns roles directly to the user and/or adds the user to groups.
5. **The system SHALL** persist the assignments immediately.
6. **The system SHALL** display a success notification.
7. The user's resolved permissions are updated on their next API call.

### UJ-4: Permission Check

1. A consumer service (e.g., shopping-service) receives a request from a user.
2. The consumer service calls `GET /user-manager/api/v1/me` with the same `X-Auth-Request-Email` header.
3. **The system SHALL** return the user's profile with resolved roles (direct + inherited from groups) and flattened actions.
4. The consumer service uses the returned actions to allow or deny the requested feature.

---

## 2. Functional Requirements

### FR-6: Roles Management

**Acceptance Criteria:**

1. When an admin creates a role, the system shall require a unique role name and optionally accept a list of actions (e.g., `shopping:create`, `recipes:export-data`).
2. The system shall validate that action strings are lowercase, contain only alphanumeric characters, hyphens, and colons (pattern: `[a-z0-9]+([:-][a-z0-9]+)*`), and reject any that contain spaces or special characters.
3. When an admin updates a role, the system shall allow modifying the name and the list of actions.
4. When an admin deletes a role, the system shall remove all user and group associations to that role.
5. The system shall not allow deletion of the predefined `admin` role.
6. The system shall expose CRUD endpoints at `/user-manager/api/v1/roles`.

**Rationale:** So that the admin can define application-specific permissions as roles, giving fine-grained control over features across all micro-frontends.

### FR-7: Groups Management

**Acceptance Criteria:**

1. When an admin creates a group, the system shall require a unique group name.
2. When an admin assigns roles to a group, the system shall persist the association.
3. When an admin removes a role from a group, the system shall remove the association.
4. When an admin deletes a group, the system shall remove all user associations to that group (users lose inherited roles from that group).
5. The system shall expose CRUD endpoints at `/user-manager/api/v1/groups`.

**Rationale:** So that the admin can define logical groupings (e.g., `family-adults`, `guests`) with predefined role sets, simplifying user permission management.

### FR-8: User Role Assignment

**Acceptance Criteria:**

1. When an admin assigns a role directly to a user, the system shall persist the user-role association.
2. When an admin assigns a group to a user, the system shall persist the user-group association.
3. When an admin removes a role or group from a user, the system shall remove the respective association.
4. The system shall expose assignment endpoints at `/user-manager/api/v1/users/{id}/roles` and `/user-manager/api/v1/users/{id}/groups`.

**Rationale:** So that the admin can tailor individual user permissions through direct role assignment or group membership.

### FR-9: Role Inheritance

**Acceptance Criteria:**

1. The system shall resolve a user's effective roles as the union of: directly assigned roles + roles inherited from all assigned groups.
2. The system shall resolve a user's effective actions as the union of all actions from all effective roles (deduplicated).
3. If a role appears both directly and via a group, the system shall include it only once in the resolved set.

**Rationale:** So that permission resolution is predictable and follows a simple additive model without conflicts or overrides.

### FR-10: Admin Bootstrap

**Acceptance Criteria:**

1. The system shall include a predefined `admin` role created via database migration (not application code).
2. When the first user is auto-created (no other users exist), the system shall automatically assign the `admin` role to that user.
3. If users already exist, new users shall receive no roles or groups by default.

**Rationale:** So that the first person to access the platform has administrative control without manual database intervention.

### FR-11: Resolved Permissions API

**Acceptance Criteria:**

1. When `GET /user-manager/api/v1/me` is called, the system shall return:
   - User profile (id, email, name, photo)
   - Resolved roles (list of role names, direct + inherited)
   - Flattened actions (deduplicated list of all action strings from all resolved roles)
2. The response shall be computed at request time (no caching of resolved permissions).
3. If the user has no roles or groups, `roles` and `actions` shall be empty arrays.

**Rationale:** So that consumer services can make authorization decisions with a single API call, without needing to understand the group/role hierarchy.
