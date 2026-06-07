# Project Context

## Overview

User Profile Manager — a micro-frontend application that enriches Google OAuth identity data with additional profile attributes (social networks, contacts) and provides Role-Based Access Control (RBAC) for all micro-frontend applications in the home platform.

## Strategy

- Part of the micro-frontend platform under `home.monteiro.net`
- First service to be extracted from the home-application monolith
- Provides identity and permissions API consumed by other micro-frontends
- Open source on GitHub (https://github.com/jlmonteiro/user-profile-manager)

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Quarkus 3.x, RESTEasy Reactive |
| ORM | Hibernate ORM with Panache (Repository pattern) |
| Database | PostgreSQL 17, Liquibase (YAML changelogs) |
| Build | Gradle 8.x (Kotlin DSL), version catalog |
| Frontend | React 19, Mantine 7, Vite, TypeScript |
| State | TanStack Query (React Query) |
| Testing (BE) | JUnit 5 + Cucumber + REST Assured, Testcontainers |
| Testing (FE) | Vitest + Testing Library |
| CI/CD | GitHub Actions |
| IDs | UUIDv7 (application-generated) |
| Container | Quarkus container-image-docker extension (backend), nginx (frontend) |

## Architecture

- **Multi-module Gradle build:** `backend/` (Quarkus API) + `frontend/` (React SPA)
- **No bundled JAR:** Frontend and backend deploy independently
- **Security:** Trusts `X-Auth-Request-Email` header from oauth2-proxy (no auth logic in backend)
- **Micro-frontend:** Renders inside launcher shell (embedded mode, parcel lifecycle)
- **Base path:** `/user-manager`
- **DB schema:** `user_manager` (isolated, own Liquibase tracking table)

## Key Conventions

- API First (OpenAPI 3.x), REST under `/api/v1/`
- RFC 7807 error responses
- UUIDv7 primary keys on all tables
- Records for DTOs, entities never exposed in API responses
- Pagination with `page` and `size` parameters
- camelCase in JSON, snake_case in database
- Constructor injection (CDI `@Inject`)
- Validate all input at boundary (Bean Validation)
- All public Java classes and methods must have JavaDoc

## Git Workflow

- Never commit without explicit user request
- The user decides when to commit, what message to use, and when to push
- Conventional commits (feat, fix, docs, refactor, test, chore)

## Specifications

All specs live in `.specs/`:
- `requirements/` — Functional requirements (EARS pattern)
- `design/` — ADRs, data models, REST architecture, frontend architecture
- `tasks/` — Epic/story breakdown

## Running Locally

```bash
# Start PostgreSQL
docker compose up -d

# Backend dev mode (hot reload)
./gradlew :backend:quarkusDev

# Frontend dev mode
cd frontend && npm run dev
```

## Links

- [Requirements Index](.specs/requirements/requirements.md)
- [Design Index](.specs/design/design.md)
- [Tasks Index](.specs/tasks/tasks.md)
