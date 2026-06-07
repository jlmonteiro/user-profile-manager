# User Profile Manager

![Version](https://img.shields.io/github/v/release/jlmonteiro/user-profile-manager?label=version&color=blue)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Quarkus](https://img.shields.io/badge/Quarkus-3.17-blue?logo=quarkus)
![React](https://img.shields.io/badge/React-19-blue?logo=react)
![License](https://img.shields.io/badge/license-Apache%202.0-green)

User profile enrichment and RBAC for the micro-frontend platform — extends Google OAuth identity with social links and provides roles/groups/actions for all applications.

## Prerequisites

- JDK 21+
- Docker & Docker Compose
- Node.js 20+ (or let Gradle download it)

## Quick Start

### Backend

```bash
docker compose up -d
./gradlew :backend:quarkusDev
```

Quarkus dev mode runs on http://localhost:8080/user-manager with hot reload.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Vite dev server runs on http://localhost:5173 with API proxy to http://localhost:8080.

## Build

```bash
./gradlew build
```

## Project Structure

```
├── backend/           # Quarkus + Java 21 (RESTEasy Reactive, Hibernate Panache)
├── frontend/          # React + Mantine + Vite
├── helm-deployment/   # Helm chart for K3s deployment
├── docker-compose.yml # Local PostgreSQL
└── .specs/            # Requirements, design, tasks (SDD)
```

## License

Apache 2.0
