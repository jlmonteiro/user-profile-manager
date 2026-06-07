# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.1.0

### Added

- Project scaffolding with Quarkus 3.17, Java 21, Gradle Kotlin DSL
- Multi-module build (backend + frontend)
- Liquibase migrations for `user_manager` schema (7 tables + admin role seed)
- Docker Compose for local PostgreSQL
- SDD specifications (requirements, design, tasks)
- Pre-commit hooks (conventional commits, yaml/json checks, secrets detection)
