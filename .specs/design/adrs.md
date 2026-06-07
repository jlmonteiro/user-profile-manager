# Architecture Decision Records

## ADR-1: Quarkus with Java 21

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** Technology choice for backend

### Context

The platform already has a Spring Boot 4.x backend (finances). We want to explore a different framework for this service to diversify skills and evaluate alternatives.

### Decision

Use Quarkus (latest 3.x) with Java 21 LTS, running in JVM mode.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| Spring Boot 4.x | Team familiarity, consistency | No learning opportunity |
| Quarkus (native) | Fastest startup, lowest memory | GraalVM complexity, longer builds, reflection limitations |
| **Quarkus (JVM)** | Fast startup (~1s), CDI standards, reactive, low boilerplate | New framework to learn |

### Consequences

- Must learn Quarkus idioms (CDI, SmallRye, RESTEasy)
- Benefits from faster startup and lower memory vs Spring Boot
- Java 21 provides records, sealed classes, virtual threads (if needed)

### Future: Native Image

Once the application is stable, evaluate migration to GraalVM native image for reduced memory footprint on the K3s cluster. No code changes required — only build configuration (`./gradlew build -Dquarkus.native.enabled=true`). Validate all dependencies are native-compatible before switching.

---

## ADR-2: RESTEasy Reactive

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** API framework choice

### Context

Quarkus offers RESTEasy Reactive as the default REST framework, running on the event loop with automatic worker thread offloading for blocking calls (Hibernate).

### Decision

Use RESTEasy Reactive with blocking-style code (Quarkus auto-offloads Hibernate calls to worker threads).

### Consequences

- Write normal JAX-RS endpoints — no reactive types needed
- Get non-blocking infrastructure without code complexity
- Consistent with Quarkus defaults and documentation

---

## ADR-3: Hibernate ORM with Panache

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** Data access layer

### Context

Need an ORM for PostgreSQL access. Quarkus supports Hibernate with Panache (Active Record or Repository pattern), plain Hibernate, or JDBC.

### Decision

Use Hibernate ORM with Panache (Repository pattern) for consistency with standard JPA and testability.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Panache (Repository)** | Testable, familiar JPA, less boilerplate | Slightly more magic than plain Hibernate |
| Panache (Active Record) | Minimal code | Entities have persistence logic, harder to test |
| Plain Hibernate | Full control | More boilerplate |
| JOOQ | Type-safe SQL, used in finances | Different Quarkus integration maturity |

### Consequences

- Entities are plain JPA `@Entity` classes
- Repositories extend `PanacheRepositoryBase<Entity, UUID>`
- UUIDv7 generation via `@PrePersist` or custom generator

---

## ADR-4: Liquibase for Migrations

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** [NFR-3](../requirements/nfr.md#nfr-3)

### Context

Need versioned database migrations. Quarkus supports both Liquibase and Flyway.

### Decision

Use Liquibase with YAML changelogs, consistent with the finances project.

### Configuration

```yaml
# application.yaml
quarkus:
  liquibase:
    default-schema-name: user_manager
    liquibase-schema: user_manager
    migrate-at-start: true
```

### Consequences

- Own changelog tracking table in `user_manager` schema
- Independent migration lifecycle from other services
- Idempotent initial migration (`CREATE SCHEMA IF NOT EXISTS`)

---

## ADR-5: Proxy-Trust Authentication

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** [NFR-1](../requirements/nfr.md#nfr-1)

### Context

The backend sits behind oauth2-proxy (Kubernetes cluster-internal, no direct Ingress). All requests are pre-authenticated.

### Decision

Trust `X-Auth-Request-Email` header for user identification. No token validation in the backend.

### Security Model

```
Internet → Traefik → oauth2-proxy (authenticates) → backend (trusts header)
```

- Backend is only reachable via ClusterIP (no external exposure)
- If header is missing → 401 (safety net)
- Admin check → service layer verifies user has `admin` role → 403 if not

### Consequences

- Zero auth libraries/config in the backend
- Security depends on network boundary (Kubernetes)
- Simple request filter extracts email from header

---

## ADR-6: Schema Isolation Strategy

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** [NFR-3](../requirements/nfr.md#nfr-3)

### Context

Multiple micro-frontend backends share one PostgreSQL instance. Each needs independent schema evolution.

### Decision

- Dedicated `user_manager` schema
- Dedicated database user with grants scoped to own schema
- Own Liquibase changelog tracking table inside the schema
- No cross-schema foreign keys from this service
- Other services may reference `user_manager.users(id)` via soft reference (no FK)

### Consequences

- Full independence for schema migrations
- Can be moved to a separate database instance in the future
- Other services store `user_id` as a UUID column without FK enforcement

---

## ADR-7: UUIDv7 for Primary Keys

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** [NFR-3](../requirements/nfr.md#nfr-3)

### Context

Need globally unique, time-sortable identifiers. Consistent with the finances project.

### Decision

Use UUIDv7 generated at the application layer (not database-generated).

### Consequences

- Time-sortable (natural ordering by creation time)
- No sequence contention
- IDs can be generated before database insert (useful for event-driven patterns)
- Java implementation via `java.util.UUID` with custom v7 generator or library (e.g., `com.github.f4b6a3:uuid-creator`)

---

## ADR-8: React + Mantine + Vite Frontend

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** Frontend technology choice

### Context

All micro-frontends in the platform use React 19 + Mantine 7 + Vite. Consistency enables shared skills and predictable integration with the launcher.

### Decision

Use the same frontend stack: React 19, Mantine 7, TypeScript, Vite, TanStack Query.

### Consequences

- Consistent UX across all applications
- Micro-frontend integration via parcel lifecycle (`bootstrap`, `mount`, `unmount`)
- Vite lib mode build for launcher consumption
- Same development patterns as finances project

---

## ADR-9: Gradle Kotlin DSL Build

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** Build tool choice

### Context

Quarkus supports both Maven and Gradle. The finances project uses Gradle (Kotlin DSL).

### Decision

Use Gradle with Kotlin DSL for consistency across the platform.

### Consequences

- Shared build knowledge across projects
- Multi-module build (backend + frontend) like finances
- Quarkus Gradle plugin for dev mode, build, and Docker image generation

---

## ADR-10: Quarkus Container Image Extension

**Status:** Accepted  
**Date:** 2026-06-07  
**Validates:** Build and deployment strategy

### Context

Need to build Docker images for the backend. Options: manual Dockerfile (like finances) or Quarkus's `container-image-docker` extension.

### Decision

Use `quarkus-container-image-docker` extension for the backend. No Dockerfile needed. Use a manual Dockerfile for the frontend (nginx-based, same as finances).

Do NOT use `quarkus-kubernetes` — Helm charts are already the standard deployment mechanism across the platform.

### Configuration

```yaml
quarkus:
  container-image:
    group: ghcr.io/jlmonteiro
    name: user-profile-manager
    base-jvm-image: eclipse-temurin:21-jre-alpine
```

### Build Commands

```bash
# Build image locally
./gradlew build -Dquarkus.container-image.build=true

# Build and push to GHCR
./gradlew build -Dquarkus.container-image.push=true
```

### Consequences

- No Dockerfile to maintain for the backend
- Quarkus handles JRE selection, layering, and entrypoint
- Consistent image structure across Quarkus projects
- Frontend still uses a manual Dockerfile (nginx + static assets)
