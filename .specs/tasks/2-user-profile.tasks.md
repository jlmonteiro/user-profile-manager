# Tasks: User Profile

## Epic

- **Epic ID**: EPIC-2
- **Status**: Draft

## References

| ID | Name |
|-----|------|
| [FR-1](../requirements/profile.md#fr-1) | Auto-Create Profile |
| [FR-2](../requirements/profile.md#fr-2) | Profile Attributes |
| [FR-3](../requirements/profile.md#fr-3) | Google People Integration |
| [FR-4](../requirements/profile.md#fr-4) | Profile Deletion |
| [FR-5](../requirements/profile.md#fr-5) | Profile API |
| [FR-10](../requirements/rbac.md#fr-10) | Admin Bootstrap |
| [UJ-1](../requirements/profile.md#uj-1) | First-Time Access |
| [UJ-2](../requirements/profile.md#uj-2) | Profile Enrichment |

## User Stories

### Story 1: User entity and repository

- **Story ID**: EPIC-2-S1
- **Estimate**: 4h
- **Description**: Create the `User` JPA entity mapped to `user_manager.users` table. Implement `UserRepository` extending `PanacheRepositoryBase<User, UUID>` with `findByEmail()` method. Add UUIDv7 generation via `@PrePersist`.
- **Dependencies**: EPIC-1-S2
- **Acceptance Criteria**:
  - [ ] Entity maps all columns from the users table
  - [ ] Repository provides `findByEmail(String email)`
  - [ ] UUIDv7 generated automatically on persist
  - [ ] Integration test: persist and retrieve a user

### Story 2: Auto-create profile and admin bootstrap

- **Story ID**: EPIC-2-S2
- **Estimate**: 4h
- **Description**: Implement `UserService` that resolves or creates a user from the email header. On first-ever user, assign the `admin` role. Expose `GET /user-manager/api/v1/me` returning the user profile.
- **Dependencies**: EPIC-2-S1, EPIC-1-S3
- **Acceptance Criteria**:
  - [ ] First request with unknown email creates a new profile
  - [ ] First-ever user gets `admin` role assigned
  - [ ] Subsequent requests with same email return existing profile
  - [ ] `GET /me` returns profile with roles and actions
  - [ ] Tests: TS-1, TS-2

### Story 3: Update profile (social links)

- **Story ID**: EPIC-2-S3
- **Estimate**: 4h
- **Description**: Implement `PUT /user-manager/api/v1/me` to update profile attributes (name, photo, phone, social links). Validate phone/WhatsApp format. Return updated profile.
- **Dependencies**: EPIC-2-S2
- **Acceptance Criteria**:
  - [ ] Valid update saves and returns updated profile
  - [ ] Invalid phone format returns 400 with validation error
  - [ ] Email field is immutable (ignored in update payload)
  - [ ] Tests: TS-3, TS-4

### Story 4: Google People API integration

- **Story ID**: EPIC-2-S4
- **Estimate**: 4h
- **Description**: Implement a REST client that calls Google People API to fetch display name and photo URL using the forwarded access token (`X-Forwarded-Access-Token`). Call on profile auto-creation. Graceful degradation if API is unavailable.
- **Dependencies**: EPIC-2-S2
- **Acceptance Criteria**:
  - [ ] New profile is enriched with name and photo from Google
  - [ ] If Google API fails, profile is created with email only + warning logged
  - [ ] Access token is NOT stored
  - [ ] Test with WireMock: success and failure scenarios

### Story 5: Profile deletion

- **Story ID**: EPIC-2-S5
- **Estimate**: 4h
- **Description**: Implement `DELETE /user-manager/api/v1/users/{id}` (admin only). Cascades to all role/group assignments. Prevent deletion of last admin. Re-login creates fresh profile.
- **Dependencies**: EPIC-2-S2
- **Acceptance Criteria**:
  - [ ] Admin can delete any user
  - [ ] Deleted user's roles and groups are removed
  - [ ] Cannot delete the last user with `admin` role (returns 422)
  - [ ] Deleted user re-authenticating gets a fresh profile with no roles
  - [ ] Non-admin calling delete gets 403
  - [ ] Tests: TS-5, TS-6, TS-7
