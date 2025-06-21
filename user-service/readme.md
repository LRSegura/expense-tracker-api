# User Service - Expense Tracker API

The **User Service** is a core microservice in the Expense Tracker API project. It is responsible for user registration, authentication integration, and user management operations.

---

## Features

- Register new users via REST API (provisions users in Keycloak)
- Retrieve user information
- Update and delete user data
- Secure endpoints with Keycloak (OIDC)
- Built with Jakarta EE, JPA (Hibernate), and deployable to WildFly

---

## Tech Stack

- **Jakarta EE 11**
- **JPA** (Hibernate)
- **REST API** (JAX-RS)
- **Keycloak** (OIDC)
- **Database:** H2 (dev), PostgreSQL (prod)
- **WildFly** (application server)
- **Maven** (build tool)

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- WildFly 26+
- Keycloak running and configured
- H2 or PostgreSQL database
- (Optional) Docker, if containerizing

### Configuration

1. **Database Datasource**  
   Configure a datasource in WildFly matching the JNDI name in `persistence.xml` (default: `java:jboss/datasources/UserDS`).

2. **Keycloak Setup**
    - Create a realm and client for the Expense Tracker project.
    - Obtain admin credentials for the Keycloak REST Admin API.

3. **Environment Variables / Properties**
    - Configure Keycloak URLs and credentials as needed for the user-service to interact with Keycloak.

### Building the Service

```bash
cd user-service
mvn clean package