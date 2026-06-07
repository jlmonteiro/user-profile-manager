# Tasks: Project Setup & Infrastructure

## Epic

- **Epic ID**: EPIC-1
- **Status**: Draft

## References

| ID | Name |
|-----|------|
| [NFR-3](../requirements/nfr.md#nfr-3) | Database Schema Isolation |
| [NFR-1](../requirements/nfr.md#nfr-1) | Proxy Trust Security |
| [ADR-1](../design/adrs.md#adr-1-quarkus-with-java-21) | Quarkus with Java 21 |
| [ADR-4](../design/adrs.md#adr-4-liquibase-for-migrations) | Liquibase for migrations |
| [ADR-9](../design/adrs.md#adr-9-gradle-kotlin-dsl-build) | Gradle Kotlin DSL build |
| [ADR-10](../design/adrs.md#adr-10-quarkus-container-image-extension) | Quarkus Container Image Extension |

## User Stories

### Story 1: Scaffold Quarkus backend project

- **Story ID**: EPIC-1-S1
- **Estimate**: 4h
- **Description**: Initialize the Gradle multi-module project with Quarkus backend. Set up `build.gradle.kts` (root + backend), Gradle wrapper, version catalog, and Quarkus extensions (RESTEasy Reactive, Hibernate Panache, Liquibase, PostgreSQL JDBC, SmallRye Health, container-image-docker, config-yaml).
- **Dependencies**: None
- **Acceptance Criteria**:
  - [ ] Gradle multi-module build compiles successfully
  - [ ] Quarkus dev mode starts (`./gradlew :backend:quarkusDev`)
  - [ ] Health endpoint responds at `/user-manager/q/health`
  - [ ] `application.yaml` configured with root-path, datasource, and Liquibase settings

### Story 2: Database schema and Liquibase migrations

- **Story ID**: EPIC-1-S2
- **Estimate**: 4h
- **Description**: Create the initial Liquibase changelog (YAML) that creates the `user_manager` schema and all 7 tables (users, roles, role_actions, groups, user_roles, user_groups, group_roles). Seed the `admin` role with `is_system = true`.
- **Dependencies**: EPIC-1-S1
- **Acceptance Criteria**:
  - [ ] Liquibase runs on startup and creates all tables in `user_manager` schema
  - [ ] `admin` role exists with `is_system = true` after migration
  - [ ] All tables have UUIDv7 PKs, `created_at`, `updated_at`
  - [ ] Unique constraints and indexes created per data model spec
  - [ ] Migration is idempotent (`CREATE SCHEMA IF NOT EXISTS`)

### Story 3: Auth filter and request identity

- **Story ID**: EPIC-1-S3
- **Estimate**: 2h
- **Description**: Implement a JAX-RS request filter that extracts `X-Auth-Request-Email` header. Returns 401 if missing. Makes the email available to resources via CDI.
- **Dependencies**: EPIC-1-S1
- **Acceptance Criteria**:
  - [ ] Requests without `X-Auth-Request-Email` return 401
  - [ ] Requests with the header proceed normally
  - [ ] Email is injectable in resource classes
  - [ ] Test: TS-16 (missing header → 401)

### Story 4: Scaffold frontend project

- **Story ID**: EPIC-1-S4
- **Estimate**: 4h
- **Description**: Initialize the frontend module with React 19, Mantine 7, Vite, TypeScript, TanStack Query. Set up Vite lib mode, parcel lifecycle (`bootstrap`, `mount`, `unmount`), standalone dev mode with mock identity, and the Gradle node plugin for build integration.
- **Dependencies**: None
- **Acceptance Criteria**:
  - [ ] `npm run dev` starts standalone dev server
  - [ ] `npm run build` produces `dist/assets/index.js` (ES module)
  - [ ] Parcel lifecycle hooks exported correctly
  - [ ] Base path set to `/user-manager/`
  - [ ] Mantine provider and React Query provider configured

### Story 5: CI/CD and deployment

- **Story ID**: EPIC-1-S5
- **Estimate**: 4h
- **Description**: Create GitHub Actions workflow (build + test), Helm chart (`helm-deployment/`), frontend Dockerfile (nginx), deploy scripts (`deploy-local.sh`, `sync-and-deploy.sh`), and Docker Compose for local PostgreSQL.
- **Dependencies**: EPIC-1-S1, EPIC-1-S4
- **Acceptance Criteria**:
  - [ ] GitHub Actions runs build and tests on push
  - [ ] `docker compose up` starts PostgreSQL with `user_manager` schema
  - [ ] Helm chart deploys backend + frontend to K3s
  - [ ] Launcher values updated with user-manager upstreams
  - [ ] `./scripts/sync-and-deploy.sh` deploys to local cluster
