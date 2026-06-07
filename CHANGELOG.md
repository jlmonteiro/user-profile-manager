# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Auth filter extracting `X-Auth-Request-Email` header (returns 401 if missing)
- Exception hierarchy (`UserManagerException` base, specific subtypes)
- Global exception handler with RFC 7807 `application/problem+json` responses
- Cucumber BDD tests via `quarkiverse-cucumber` extension
- DevServices integration with init script for schema pre-creation
- Frontend scaffolding (React 19, Mantine 7, Vite, TanStack Query)
- Parcel lifecycle hooks (`bootstrap`, `mount`, `unmount`) for launcher integration
- Vite lib mode build producing `dist/assets/index.js`
- GitHub Actions CI workflows (backend + frontend)
- Helm chart for K3s deployment (backend + frontend)
- Frontend Dockerfile (multi-stage, nginx, non-root)
- Deploy scripts (`deploy-local.sh`, `sync-and-deploy.sh`)

## 0.1.0

### Added

- Project scaffolding with Quarkus 3.17, Java 21, Gradle Kotlin DSL
- Multi-module build (backend + frontend)
- Liquibase migrations for `user_manager` schema (7 tables + admin role seed)
- Docker Compose for local PostgreSQL
- SDD specifications (requirements, design, tasks)
- Pre-commit hooks (conventional commits, yaml/json checks, secrets detection)
