# Design Index

## Architecture Overview

User Profile Manager is a micro-frontend application composed of a Java/Quarkus backend API and a React/Mantine frontend, loaded by the micro-frontend-launcher shell. It provides user profile management and RBAC for all applications in the platform.

```mermaid
graph TB
    subgraph Launcher["🚀 Micro-Frontend Launcher"]
        Shell[🖥️ Shell / Chrome]
        OAuth2Proxy[🔐 oauth2-proxy]
    end

    subgraph UserProfileManager["👤 User Profile Manager"]
        FE[⚛️ Frontend<br/>React + Mantine + Vite]
        BE[☕ Backend<br/>Quarkus + Java 21]
        DB[(🐘 PostgreSQL<br/>user_manager schema)]
    end

    subgraph Consumers["📡 Consumer Services"]
        Finance[💰 Finances Backend]
        Shopping[🛒 Shopping Backend]
    end

    Shell --> FE
    OAuth2Proxy -->|X-Auth-Request-Email| BE
    FE -->|REST /user-manager/api| BE
    BE --> DB
    Finance -->|GET /user-manager/api/v1/me| BE
    Shopping -->|GET /user-manager/api/v1/me| BE
```

## Pages

| Page | Description |
|------|-------------|
| [adrs.md](adrs.md) | Architecture Decision Records |
| [data-models.md](data-models.md) | Database schema and entity relationships |
| [rest-architecture.md](rest-architecture.md) | REST API design and endpoints |
| [frontend-architecture.md](frontend-architecture.md) | Frontend structure, routing, and component design |
| [test-scenarios.md](test-scenarios.md) | Test scenarios validating requirements |

## Component Interaction

```mermaid
graph LR
    subgraph Frontend
        Pages[Pages / Views]
        Hooks[TanStack Query Hooks]
        API[API Client]
    end

    subgraph Backend
        Resources[JAX-RS Resources]
        Services[Service Layer]
        Entities[Hibernate Entities]
    end

    subgraph Infrastructure
        PG[(PostgreSQL)]
        LB[Liquibase Migrations]
        Google[Google People API]
    end

    Pages --> Hooks
    Hooks --> API
    API -->|HTTP| Resources
    Resources --> Services
    Services --> Entities
    Entities --> PG
    LB --> PG
    Services -->|Profile creation only| Google
```

## Key Design Decisions

| ADR | Decision | Status |
|-----|----------|--------|
| [ADR-1](adrs.md#adr-1-quarkus-with-java-21) | Quarkus with Java 21 | Accepted |
| [ADR-2](adrs.md#adr-2-resteasy-reactive) | RESTEasy Reactive | Accepted |
| [ADR-3](adrs.md#adr-3-hibernate-orm-with-panache) | Hibernate ORM with Panache | Accepted |
| [ADR-4](adrs.md#adr-4-liquibase-for-migrations) | Liquibase for migrations | Accepted |
| [ADR-5](adrs.md#adr-5-proxy-trust-authentication) | Proxy-trust authentication | Accepted |
| [ADR-6](adrs.md#adr-6-schema-isolation-strategy) | Schema isolation strategy | Accepted |
| [ADR-7](adrs.md#adr-7-uuidv7-for-primary-keys) | UUIDv7 for primary keys | Accepted |
| [ADR-8](adrs.md#adr-8-react-mantine-vite-frontend) | React + Mantine + Vite frontend | Accepted |
| [ADR-9](adrs.md#adr-9-gradle-kotlin-dsl-build) | Gradle Kotlin DSL build | Accepted |
| [ADR-10](adrs.md#adr-10-quarkus-container-image-extension) | Quarkus Container Image Extension | Accepted |

## Cross-Cutting Concerns

| Concern | Approach |
|---------|----------|
| Authentication | Trusts `X-Auth-Request-Email` header from oauth2-proxy (NFR-1) |
| Authorization | Admin role checked via service layer; enforced per endpoint |
| Error Handling | RFC 7807 Problem Detail for all errors |
| Logging | Structured JSON, correlation via request ID |
| Configuration | Environment variables, Quarkus `application.yaml` |
| Health Checks | Quarkus SmallRye Health (`/q/health`) |
