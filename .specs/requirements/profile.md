# Requirements: User Profile

## 1. User Journeys

### UJ-1: First-Time Access

1. User authenticates via Google OAuth through the launcher.
2. oauth2-proxy forwards the request to the user-profile-manager backend with `X-Auth-Request-Email` header.
3. **The system SHALL** check if a profile exists for the given email.
4. **The system SHALL** auto-create a profile using the email and available OAuth data (name, photo).
5. If this is the first user ever in the system, **the system SHALL** assign the predefined `admin` role to this user.
6. User is presented with their profile page showing basic info from Google.

### UJ-2: Profile Enrichment

1. User navigates to "My Profile" in the launcher sidebar (under Security section).
2. User is presented with their current profile data (name, email, photo from Google).
3. User fills in additional fields: phone, Instagram, Facebook, LinkedIn, WhatsApp, Telegram.
4. **The system SHALL** validate and save the updated profile.
5. **The system SHALL** display a success notification confirming the save.

---

## 2. Functional Requirements

### FR-1: Auto-Create Profile

**Acceptance Criteria:**

1. When a request arrives with an `X-Auth-Request-Email` header and no profile exists for that email, the system shall create a new profile with the email as the unique identifier.
2. The system shall populate the profile name and photo from Google People API data when available.
3. If no user exists in the system (empty users table), the system shall assign the `admin` role to the newly created user.
4. The system shall generate a UUIDv7 as the primary key for the new profile.

**Rationale:** So that users don't need a manual registration step, the system auto-provisions profiles on first authenticated access.

### FR-2: Profile Attributes

**Acceptance Criteria:**

1. The system shall store the following profile attributes:
   - `email` (unique key, from OAuth, immutable)
   - `name` (from Google, editable)
   - `photo` (URL from Google, editable)
   - `phone` (optional)
   - `instagram` (optional, handle without @)
   - `facebook` (optional, profile URL or username)
   - `linkedin` (optional, profile URL or username)
   - `whatsapp` (optional, phone number)
   - `telegram` (optional, handle without @)
2. When the user updates their profile, the system shall validate that phone/WhatsApp fields contain valid phone formats.
3. The system shall persist changes and return the updated profile.

**Rationale:** So that household members can share contact info beyond what Google provides, enriching the user directory.

### FR-3: Google People Integration

**Acceptance Criteria:**

1. When a profile is auto-created, the system shall call the Google People API to retrieve the user's display name and photo URL.
2. If the Google People API is unavailable, the system shall create the profile with email only and log a warning.
3. The system shall not store Google access tokens; it shall use the forwarded access token from oauth2-proxy (`X-Forwarded-Access-Token`) for the API call.

**Rationale:** So that new profiles are pre-populated with useful data without requiring manual input.

### FR-4: Profile Deletion

**Acceptance Criteria:**

1. When an admin requests deletion of a user profile, the system shall remove the profile and all associated role/group assignments.
2. If the deleted user logs in again, the system shall create a fresh profile with no roles or group memberships.
3. The system shall not allow deletion of the last remaining admin user.

**Rationale:** So that household members can leave the platform cleanly, and re-joining starts fresh for security.

### FR-5: Profile API

**Acceptance Criteria:**

1. The system shall expose `GET /user-manager/api/v1/me` returning the authenticated user's profile, resolved roles, and flattened actions.
2. The system shall expose `GET /user-manager/api/v1/users` returning all user profiles (admin only).
3. The system shall expose `GET /user-manager/api/v1/users/{id}` returning a specific user's profile with role/group assignments (admin only).
4. The system shall use RFC 7807 (Problem Detail) for all error responses.
5. The system shall paginate list endpoints with `?page=&size=` parameters.

**Rationale:** So that consumer services and the frontend can retrieve user data in a consistent, standardized way.
