# Tasks: RBAC

## Epic

- **Epic ID**: EPIC-3
- **Status**: Draft

## References

| ID | Name |
|-----|------|
| [FR-6](../requirements/rbac.md#fr-6) | Roles Management |
| [FR-7](../requirements/rbac.md#fr-7) | Groups Management |
| [FR-8](../requirements/rbac.md#fr-8) | User Role Assignment |
| [FR-9](../requirements/rbac.md#fr-9) | Role Inheritance |
| [FR-11](../requirements/rbac.md#fr-11) | Resolved Permissions API |
| [UJ-3](../requirements/rbac.md#uj-3) | Admin Role Assignment |
| [UJ-4](../requirements/rbac.md#uj-4) | Permission Check |

## User Stories

### Story 1: Role entity and CRUD

- **Story ID**: EPIC-3-S1
- **Estimate**: 4h
- **Description**: Create `Role` and `RoleAction` entities. Implement `RoleRepository`. Build `RoleResource` with CRUD endpoints at `/user-manager/api/v1/roles`. Validate action format (`[a-z0-9]+([:-][a-z0-9]+)*`). Protect `is_system` roles from deletion.
- **Dependencies**: EPIC-1-S2, EPIC-1-S3
- **Acceptance Criteria**:
  - [ ] `POST /roles` creates a role with optional actions
  - [ ] `PUT /roles/{id}` updates name and replaces actions
  - [ ] `DELETE /roles/{id}` removes role and cascades
  - [ ] Cannot delete role with `is_system = true` (returns 422)
  - [ ] Invalid action format returns 400
  - [ ] All endpoints require admin role
  - [ ] Tests: TS-8, TS-9, TS-10

### Story 2: Group entity and CRUD

- **Story ID**: EPIC-3-S2
- **Estimate**: 4h
- **Description**: Create `Group` and `GroupRole` entities. Implement `GroupRepository`. Build `GroupResource` with CRUD endpoints at `/user-manager/api/v1/groups`. Support assigning roles to a group on create/update.
- **Dependencies**: EPIC-3-S1
- **Acceptance Criteria**:
  - [ ] `POST /groups` creates a group
  - [ ] `PUT /groups/{id}` updates name, description, and role assignments
  - [ ] `DELETE /groups/{id}` removes group and cascades user-group associations
  - [ ] Duplicate group name returns 409
  - [ ] All endpoints require admin role
  - [ ] Test: TS-11

### Story 3: User listing and detail (admin)

- **Story ID**: EPIC-3-S3
- **Estimate**: 4h
- **Description**: Implement `GET /user-manager/api/v1/users` (paginated) and `GET /user-manager/api/v1/users/{id}` returning user profiles with their direct roles and group memberships. Admin only.
- **Dependencies**: EPIC-2-S2
- **Acceptance Criteria**:
  - [ ] `GET /users` returns paginated list with roles and groups per user
  - [ ] `GET /users/{id}` returns full user detail
  - [ ] Non-admin gets 403
  - [ ] Pagination works with `page` and `size` parameters
  - [ ] Test: TS-17

### Story 4: User role and group assignment

- **Story ID**: EPIC-3-S4
- **Estimate**: 4h
- **Description**: Implement `PUT /user-manager/api/v1/users/{id}/roles` and `PUT /user-manager/api/v1/users/{id}/groups`. Full replacement — accepts a list of IDs, removes old assignments, creates new ones.
- **Dependencies**: EPIC-3-S1, EPIC-3-S2, EPIC-3-S3
- **Acceptance Criteria**:
  - [ ] `PUT /users/{id}/roles` replaces direct role assignments
  - [ ] `PUT /users/{id}/groups` replaces group memberships
  - [ ] Invalid role/group IDs return 404
  - [ ] Admin only
  - [ ] Test: TS-12

### Story 5: Permission resolution

- **Story ID**: EPIC-3-S5
- **Estimate**: 4h
- **Description**: Implement the permission resolution logic in `UserService`. Resolve effective roles (direct + inherited from groups, deduplicated) and flatten actions. Update `GET /me` response to include resolved `roles` and `actions` arrays.
- **Dependencies**: EPIC-3-S4
- **Acceptance Criteria**:
  - [ ] Roles from direct assignment and group membership are merged
  - [ ] Duplicate roles appear only once
  - [ ] Actions from all resolved roles are flattened and deduplicated
  - [ ] User with no roles/groups returns empty arrays
  - [ ] Tests: TS-13, TS-14, TS-15

### Story 6: Admin guard and error handling

- **Story ID**: EPIC-3-S6
- **Estimate**: 2h
- **Description**: Implement a reusable admin check (interceptor or annotation) for admin-only endpoints. Implement RFC 7807 exception mapper for all error types (400, 401, 403, 404, 409, 422).
- **Dependencies**: EPIC-2-S2
- **Acceptance Criteria**:
  - [ ] Non-admin accessing admin endpoint gets 403 Problem Detail
  - [ ] All validation errors return 400 Problem Detail
  - [ ] Duplicate name conflicts return 409 Problem Detail
  - [ ] Business rule violations return 422 Problem Detail
  - [ ] Test: TS-17
