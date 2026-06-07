# Requirements Index

## Overview

User Profile Manager — a micro-frontend application that enriches Google OAuth identity data with additional profile attributes (social networks, contacts) and provides Role-Based Access Control (RBAC) for all micro-frontend applications in the home platform.

**The North Star:** A centralized identity and access management service that other micro-frontends consume to resolve user profiles and enforce permissions.

**Stakeholders:**
- **End Users:** Household members managing their profiles
- **Admin:** User with the `admin` role who manages roles, groups, and user assignments
- **Consumer Services:** Other micro-frontend backends that query this API for user info and permissions

---

## Requirement Classifications

All specifications are categorized into three types:

- **User Journeys (UJ):** Narrative-driven scenarios describing an end-to-end goal from the user's perspective.
- **Functional Requirements (FR):** Specific features or behaviors the system MUST perform.
- **Non-Functional Requirements (NFR):** Quality attributes such as performance, security, and reliability.

---

## EARS Pattern

Requirements use the **EARS (Easy Approach to Requirements Syntax)** pattern:

- **Ubiquitous:** "The system shall..." (Always true)
- **Event-driven:** "When <trigger>, the system shall..."
- **Unwanted Behavior:** "If <condition>, then the system shall..."
- **State-driven:** "While <state>, the system shall..."
- **Optional:** "Where <feature exists>, the system shall..."
- **Complex:** Combinations of the above triggers.

[EARS Documentation](https://alistairmavin.com/ears/)

---

## 1. User Journeys

| ID | Journey | Description |
| :--- | :--- | :--- |
| [**UJ-1**](profile.md#uj-1) | First-Time Access | New user accesses the platform and profile is auto-created from OAuth data. |
| [**UJ-2**](profile.md#uj-2) | Profile Enrichment | User completes their profile with social links and contacts. |
| [**UJ-3**](rbac.md#uj-3) | Admin Role Assignment | Admin assigns roles and groups to users. |
| [**UJ-4**](rbac.md#uj-4) | Permission Check | Consumer service queries user permissions to allow/deny features. |

---

## 2. User Profile

| ID | Requirement | Description |
| :--- | :--- | :--- |
| [**FR-1**](profile.md#fr-1) | Auto-Create Profile | Profile auto-created on first authenticated request. |
| [**FR-2**](profile.md#fr-2) | Profile Attributes | User can manage social links and contact information. |
| [**FR-3**](profile.md#fr-3) | Google People Integration | Enrich profile with data from Google People API. |
| [**FR-4**](profile.md#fr-4) | Profile Deletion | User profile can be deleted; re-login recreates from scratch. |
| [**FR-5**](profile.md#fr-5) | Profile API | Expose profile data for consumer services via `/me` endpoint. |

---

## 3. RBAC

| ID | Requirement | Description |
| :--- | :--- | :--- |
| [**FR-6**](rbac.md#fr-6) | Roles Management | Admin can create, update, and delete roles with optional actions. |
| [**FR-7**](rbac.md#fr-7) | Groups Management | Admin can create, update, and delete groups and assign roles to them. |
| [**FR-8**](rbac.md#fr-8) | User Role Assignment | Admin can assign roles and groups directly to users. |
| [**FR-9**](rbac.md#fr-9) | Role Inheritance | Users inherit all roles assigned to their groups. |
| [**FR-10**](rbac.md#fr-10) | Admin Bootstrap | First authenticated user receives the predefined `admin` role automatically. |
| [**FR-11**](rbac.md#fr-11) | Resolved Permissions API | API returns resolved roles and flattened actions for a user. |

---

## 4. Non-Functional Requirements

| ID | Requirement | Description |
| :--- | :--- | :--- |
| [**NFR-1**](nfr.md#nfr-1) | Proxy Trust Security | Backend trusts oauth2-proxy headers for authentication. |
| [**NFR-2**](nfr.md#nfr-2) | API Response Time | Profile and permissions API responds within acceptable latency. |
| [**NFR-3**](nfr.md#nfr-3) | Database Schema Isolation | Uses dedicated schema with UUIDv7 primary keys. |

---

## 5. Assumptions

| # | Assumption | Detail |
|---|-----------|--------|
| [**AS-1**](nfr.md#as-1) | OAuth2-proxy always present | All requests have been authenticated by oauth2-proxy before reaching the backend. |
| [**AS-2**](nfr.md#as-2) | Single database instance | Shared PostgreSQL instance with dedicated `user_manager` schema. |
| [**AS-3**](nfr.md#as-3) | Email uniqueness | Each Google account maps to exactly one user profile via email. |

---

## 6. Out of Scope

| # | Item | Detail |
|---|------|--------|
| [**OOS-1**](nfr.md#oos-1) | Authentication | Handled by oauth2-proxy in the launcher; this service does not authenticate users. |
| [**OOS-2**](nfr.md#oos-2) | Session management | Cookie-based sessions managed by oauth2-proxy. |
| [**OOS-3**](nfr.md#oos-3) | Permission enforcement | Consumer services are responsible for enforcing permissions; this service only provides the data. |

---

## 7. Glossary

| Term | Definition |
| :--- | :--- |
| **Role** | A named entity with optional actions that can be assigned to users or groups. |
| **Group** | A named collection of roles that can be assigned to users for bulk role inheritance. |
| **Action** | A user-defined permission string (e.g., `shopping:create`) attached to a role. |
| **Consumer Service** | Another micro-frontend backend that calls this API to resolve user identity/permissions. |
| **Admin** | A user with the predefined `admin` role; can manage all RBAC entities. |
