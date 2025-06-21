# Expense Tracker API

A hands-on learning project to explore **Jakarta EE microservices** with real-world authentication and authorization using Keycloak. The system lets users track their expenses and incomes securely, with each microservice focused on a single domain.

---

## Project Overview

- **Monorepo** structure for easy refactoring and shared configuration
- **Microservices architecture**: each core feature is a separate service
- **Keycloak** for user authentication, roles, and security
- **H2** database for development; **PostgreSQL** for production
- **Deployed on WildFly** (Jakarta EE 11 compatible)
- **Dependencies shared in parent POM**

---

## Modules

| Module           | Purpose                                      |
|------------------|----------------------------------------------|
| `user-service`   | User registration and profile management     |
| `expense-service`| CRUD for expenses (per-user isolation)       |
| `income-service` | CRUD for incomes (per-user isolation)        |
| `gateway`*       | API gateway for routing and aggregation      |
| `common`         | Shared DTOs, utilities, config               |

*`gateway` is optional for local dev; useful if you want centralized routing and future integration points.

---

## Technology Stack

- **Backend Framework:** Jakarta EE 11 (Web Profile)
- **Security:** Keycloak (OIDC, JWT)
- **Database:**
    - Development: H2 (in-memory, via WildFly's ExampleDS)
    - Production: PostgreSQL
- **JPA Provider:** Hibernate (managed by WildFly)
- **Build Tool:** Maven (multi-module/monorepo)
- **Application Server:** WildFly 30+ (Jakarta EE 11 compatible)
- **Containerization:** Docker (for local dev and integration, optional)
- **Testing:** JUnit, REST-assured (add as needed)

---

## Setup Highlights

- **Shared dependencies** (`jakarta.jakartaee-web-api`, `h2`, etc.) are declared in the parent POM.
- Each service can have its own specific dependencies as needed.
- For local development, H2 is used via WildFly's default `ExampleDS`.
- For production, swap datasource to PostgreSQL and configure in WildFly.
- **Keycloak integration:** All REST endpoints are secured; user registration is handled via Keycloak Admin REST API (not self-registration).

---

## Quick Start (Development Mode)

1. Clone the repo.
2. Start WildFly and Keycloak (see `docker/` for setup scripts, if present).
3. Build all services: mvn clean install
4. Deploy the WARs to WildFly.
5. Use Postman or curl to interact with secured endpoints.

---

## Security Best Practices

- All endpoints require authentication (JWT via Keycloak).
- Users can only access their own expenses/incomes.
- Roles and permissions managed centrally in Keycloak.