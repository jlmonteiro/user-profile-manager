# Requirements: Non-Functional, Assumptions & Out of Scope

## 1. Non-Functional Requirements

### NFR-1: Proxy Trust Security

**Acceptance Criteria:**

1. The system shall identify the authenticated user exclusively from the `X-Auth-Request-Email` header set by oauth2-proxy.
2. The system shall not implement its own authentication mechanism (no login, no session, no token validation).
3. If the `X-Auth-Request-Email` header is missing, the system shall return HTTP 401.

**Rationale:** So that the authentication concern remains centralized in oauth2-proxy, avoiding duplication and reducing attack surface.

### NFR-2: API Response Time

**Acceptance Criteria:**

1. The `GET /user-manager/api/v1/me` endpoint shall respond within 200ms at p95 under normal load.
2. The system shall not introduce external network calls in the permissions resolution path (resolved from database only).

**Rationale:** So that consumer services calling this API on every request do not introduce noticeable latency to end users.

### NFR-3: Database Schema Isolation

**Acceptance Criteria:**

1. The system shall use a dedicated `user_manager` schema within the shared PostgreSQL instance.
2. All tables shall use UUIDv7 as primary keys.
3. All tables shall include `created_at` and `updated_at` timestamp columns.
4. The system shall manage its schema exclusively via Liquibase, with its own changelog tracking table in the `user_manager` schema.
5. The system shall not declare foreign keys to other service schemas.

**Rationale:** So that the service owns its data independently and can be migrated to a separate database in the future without schema entanglement.

---

## 2. Assumptions

### AS-1: OAuth2-proxy Always Present

**Statement:** All requests reaching the backend have been authenticated by oauth2-proxy. The `X-Auth-Request-Email` header is always present and trustworthy.

**Impact if Wrong:** If the backend is accidentally exposed without the proxy, any request could impersonate any user by setting the header. Mitigation: Kubernetes NetworkPolicy restricting ingress to oauth2-proxy only.

### AS-2: Single Database Instance

**Statement:** All micro-frontend backends share a single PostgreSQL instance, each with their own schema.

**Impact if Wrong:** If services move to separate database instances, cross-schema FKs would need to be dropped and replaced with soft references. The user-profile-manager has no cross-schema FKs, so this is low risk.

### AS-3: Email Uniqueness

**Statement:** Each Google account maps to exactly one user profile. The email is the stable, unique identifier linking OAuth identity to the profile.

**Impact if Wrong:** If a user changes their Google email, they would appear as a new user. Mitigation: could add Google `sub` (subject ID) as a secondary identifier in the future.

---

## 3. Out of Scope

### OOS-1: Authentication

**Description:** User authentication (OAuth flow, token exchange, session cookies) is handled entirely by oauth2-proxy in the micro-frontend-launcher deployment.

**Rationale:** Centralizing auth in the proxy avoids duplicated security logic across services.

### OOS-2: Session Management

**Description:** Session creation, cookie encryption, and refresh are managed by oauth2-proxy.

**Rationale:** The backend is stateless; it resolves identity from headers on each request.

### OOS-3: Permission Enforcement

**Description:** This service provides roles and actions data. Consumer services are responsible for interpreting and enforcing those permissions in their own business logic.

**Rationale:** Each application knows its own features best and can decide how to map roles/actions to UI elements and API access.
