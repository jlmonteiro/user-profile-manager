# Test Scenarios

## 1. Profile

### TS-1: Auto-create profile on first access

- **Given**: No user profile exists for `jorge@example.com`.
- **When**: A request arrives with `X-Auth-Request-Email: jorge@example.com`.
- **Then**: A new profile is created with the email, and the response includes the profile data.

**Validates:** [FR-1](../requirements/profile.md#fr-1)

### TS-2: Admin bootstrap on first user

- **Given**: The `users` table is empty.
- **When**: The first user is auto-created.
- **Then**: The user is assigned the `admin` role.

**Validates:** [FR-1](../requirements/profile.md#fr-1), [FR-10](../requirements/rbac.md#fr-10)

### TS-3: Update profile social links

- **Given**: A user profile exists for `jorge@example.com`.
- **When**: The user sends `PUT /me` with phone, instagram, and telegram fields.
- **Then**: The profile is updated and the response reflects the new values.

**Validates:** [FR-2](../requirements/profile.md#fr-2)

### TS-4: Phone validation

- **Given**: A user profile exists.
- **When**: The user sends `PUT /me` with `phone: "invalid"`.
- **Then**: The system returns 400 with a validation error.

**Validates:** [FR-2](../requirements/profile.md#fr-2)

### TS-5: Profile deletion

- **Given**: User `bob@example.com` exists with role `shopping-editor`.
- **When**: An admin deletes the user.
- **Then**: The profile and all role/group assignments are removed.

**Validates:** [FR-4](../requirements/profile.md#fr-4)

### TS-6: Deleted user re-login

- **Given**: User `bob@example.com` was previously deleted.
- **When**: A request arrives with `X-Auth-Request-Email: bob@example.com`.
- **Then**: A fresh profile is created with no roles or groups.

**Validates:** [FR-4](../requirements/profile.md#fr-4)

### TS-7: Cannot delete last admin

- **Given**: Only one user has the `admin` role.
- **When**: An admin attempts to delete that user.
- **Then**: The system returns 422 with an error explaining the last admin cannot be deleted.

**Validates:** [FR-4](../requirements/profile.md#fr-4)

---

## 2. RBAC

### TS-8: Create role with actions

- **Given**: An admin is authenticated.
- **When**: `POST /roles` with name `shopping-editor` and actions `["shopping:create", "shopping:read"]`.
- **Then**: The role is created and returned with the actions.

**Validates:** [FR-6](../requirements/rbac.md#fr-6)

### TS-9: Action format validation

- **Given**: An admin is authenticated.
- **When**: `POST /roles` with actions `["Shopping:Create", "invalid action!"]`.
- **Then**: The system returns 400 with validation errors for each invalid action.

**Validates:** [FR-6](../requirements/rbac.md#fr-6)

### TS-10: Cannot delete system role

- **Given**: The `admin` role exists with `is_system = true`.
- **When**: An admin attempts `DELETE /roles/{admin-role-id}`.
- **Then**: The system returns 422 explaining system roles cannot be deleted.

**Validates:** [FR-6](../requirements/rbac.md#fr-6)

### TS-11: Create group and assign roles

- **Given**: Roles `shopping-editor` and `recipes-viewer` exist.
- **When**: `POST /groups` with name `family-adults`, then `PUT /groups/{id}` with those role IDs.
- **Then**: The group is created and associated with the specified roles.

**Validates:** [FR-7](../requirements/rbac.md#fr-7)

### TS-12: Assign role directly to user

- **Given**: User `bob@example.com` exists with no roles.
- **When**: Admin sends `PUT /users/{bob-id}/roles` with `["shopping-editor"]`.
- **Then**: Bob now has the `shopping-editor` role.

**Validates:** [FR-8](../requirements/rbac.md#fr-8)

### TS-13: Role inheritance via group

- **Given**: Group `family-adults` has roles `shopping-editor` and `recipes-viewer`. Bob is assigned to the group.
- **When**: `GET /me` is called as Bob.
- **Then**: Bob's resolved roles include `shopping-editor` and `recipes-viewer`.

**Validates:** [FR-9](../requirements/rbac.md#fr-9)

### TS-14: Deduplicated role resolution

- **Given**: Bob has `shopping-editor` directly AND via group `family-adults`.
- **When**: `GET /me` is called as Bob.
- **Then**: `shopping-editor` appears only once in the roles array.

**Validates:** [FR-9](../requirements/rbac.md#fr-9)

### TS-15: Flattened actions

- **Given**: Bob has roles `shopping-editor` (actions: `shopping:create`, `shopping:read`) and `recipes-viewer` (actions: `recipes:read`).
- **When**: `GET /me` is called as Bob.
- **Then**: Actions array contains `["recipes:read", "shopping:create", "shopping:read"]` (deduplicated, sorted).

**Validates:** [FR-11](../requirements/rbac.md#fr-11)

---

## 3. Security

### TS-16: Missing auth header

- **Given**: No `X-Auth-Request-Email` header in the request.
- **When**: Any endpoint is called.
- **Then**: The system returns 401.

**Validates:** [NFR-1](../requirements/nfr.md#nfr-1)

### TS-17: Non-admin accessing admin endpoint

- **Given**: User `bob@example.com` has no `admin` role.
- **When**: Bob calls `GET /users`.
- **Then**: The system returns 403.

**Validates:** [NFR-1](../requirements/nfr.md#nfr-1)

---

## Summary & Environment

- **Test Framework:** Cucumber + JUnit 5 + REST Assured (backend), Vitest + Testing Library (frontend)
- **BDD Style:** Gherkin `.feature` files with Java step definitions
- **Database:** Testcontainers (PostgreSQL) for integration tests
- **Mocks:** Google People API mocked via WireMock or Quarkus `@QuarkusTestResource`
- **Verification:** All test scenarios must pass; coverage target ≥ 80% on service layer
