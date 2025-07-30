# ui-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ui-service-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and
  Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on
  it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

This is a Quarkus application with Vaadin frontend that uses OpenID Connect (OIDC) authentication via Keycloak. Let me break down the different sections of configurations:
## User Service Client Configuration
``` properties
user-service/mp-rest/url=http://localhost:8080/user-service
user-service/mp-rest/scope=jakarta.inject.Singleton
```
These are **legacy MicroProfile REST Client** configurations for connecting to a user service. However, I notice you also have the newer Quarkus REST Client configuration below, so these might be redundant.
## Server Configuration
``` properties
quarkus.http.port=8088
```
Sets your application to run on port **8088** instead of the default 8080.
## OIDC (OpenID Connect) Configuration
``` properties
quarkus.oidc.auth-server-url=http://localhost:8180/realms/expense-tracker
quarkus.oidc.client-id=ui-service
quarkus.oidc.credentials.secret=kM6CJbs2fJAE7qnyTjknb3f5sIVOAvzM
quarkus.oidc.application-type=web-app
quarkus.oidc.roles.source=accesstoken
quarkus.oidc.logout.path=/q/logout
quarkus.oidc.logout.post-logout-path=/login
```
This configures your app as an **OIDC web application** that authenticates against Keycloak:
- : Points to your Keycloak realm **auth-server-url**
- : Your application's identifier in Keycloak **client-id**
- : Client secret for secure communication **credentials.secret**
- : Enables authorization code flow with sessions **application-type=web-app**
- : Extracts user roles from the access token **roles.source=accesstoken**
- **logout paths**: Defines logout endpoints

## OIDC Client Configuration
``` properties
quarkus.oidc-client.auth-server-url=${quarkus.oidc.auth-server-url}
quarkus.oidc-client.client-id=${quarkus.oidc.client-id}
quarkus.oidc-client.credentials.secret=${quarkus.oidc.credentials.secret}
```
This enables **token propagation** - allows your app to make authenticated calls to other services using the user's token.
## REST Client Configuration
``` properties
quarkus.rest-client."com.dev2ever.UserServiceClient".url=http://localhost:8080/user-service
quarkus.rest-client."com.dev2ever.UserServiceClient".scope=jakarta.inject.Singleton
quarkus.rest-client."com.dev2ever.UserServiceClient".providers=com.dev2ever.OidcClientRequestFilter
```
Modern Quarkus REST Client configuration for your interface: `UserServiceClient`
- **url**: Target service endpoint
- **scope**: Makes the client a singleton
- **providers**: Adds the OIDC token propagation filter

## HTTP Security Configuration
``` properties
quarkus.http.auth.permission.authenticated.paths=/*
quarkus.http.auth.permission.authenticated.policy=authenticated
quarkus.http.auth.permission.public.paths=/login,/q/*,/VAADIN/*,/HEARTBEAT/*,/UIDL/*,/favicon.ico
quarkus.http.auth.permission.public.policy=permit
quarkus.http.auth.permission.public.methods=GET,POST
```
Defines **path-based security**:
- All paths (`/*`) require authentication by default
- Specific paths are public: login page, Quarkus endpoints, Vaadin resources
- Session encryption key for secure sessions

## CORS Configuration
``` properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:8180
quarkus.http.cors.methods=GET,PUT,POST,DELETE
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
```
Enables **Cross-Origin Resource Sharing** to allow requests from your Keycloak server.
## Vaadin Configuration
``` properties
quarkus.vaadin.url-mapping=/ui/*
```
Maps Vaadin UI to the path pattern. `/ui/*`
## Key Points:
1. **Dual Authentication Setup**: You have both web app authentication (for users) and client credentials (for service-to-service calls)
2. **Token Propagation**: User tokens are automatically forwarded to the user service
3. **Security Model**: Default deny with explicit allow for public resources
4. **Development Setup**: All services running locally with standard ports
