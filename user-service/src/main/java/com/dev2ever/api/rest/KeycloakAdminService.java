package com.dev2ever.api.rest;

import com.dev2ever.model.ErrorCode;
import com.dev2ever.util.OperationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class KeycloakAdminService {

    private final Logger logger = Logger.getLogger(KeycloakAdminService.class.getName());

    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8180";
    private static final String KEYCLOAK_REALM = "expense-tracker";
    private static final String KEYCLOAK_CLIENT_ID = "user-service-api";
    private static final String KEYCLOAK_CLIENT_SECRET = "39dLCdPxu31vv3jxeUPRlRQYqmgrOyE9";
    private static final String ACCESS_TOKEN_KEY = "access_token";

    private String getAdminAccessToken() {
        try (Client client = ClientBuilder.newClient()) {
            String tokenRequestUrl = buildTokenRequestUrl();
            Form tokenForm = buildTokenForm();

            Response tokenResponse = client.target(tokenRequestUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(tokenForm));

            validateTokenResponse(tokenResponse);

            Map<String, Object> tokenBody = tokenResponse.readEntity(Map.class);
            return (String) tokenBody.get(ACCESS_TOKEN_KEY);
        }
    }

    private String buildTokenRequestUrl() {
        return KEYCLOAK_SERVER_URL + "/realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token";
    }

    private Form buildTokenForm() {
        return new Form()
                .param("grant_type", "client_credentials")
                .param("client_id", KEYCLOAK_CLIENT_ID)
                .param("client_secret", KEYCLOAK_CLIENT_SECRET);
    }

    private void validateTokenResponse(Response response) {
        if (response.getStatus() != 200) {
            String error = "Failed to get Keycloak admin token. Status: " + response.getStatus();
            logger.severe(error);
            throw new RuntimeException(error);
        }
    }


    public OperationResult<Void> createUser(UserDto userDto) {
        logger.log(Level.INFO, "Creating user in Keycloak... {0}", userDto);
        String accessToken = getAdminAccessToken();
        String usersUrl = KEYCLOAK_SERVER_URL + "/admin/realms/" + KEYCLOAK_REALM + "/users";
        Map<String, Object> userRepresentation = buildUserRepresentation(userDto);

        try (Client client = ClientBuilder.newClient()) {
            try (Response response = client.target(usersUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .post(Entity.json(userRepresentation))) {

                if (response.getStatus() != 201) {
                    String errorResponse = response.readEntity(String.class);
                    String error = "Failed to create user in Keycloak. Status: " + response.getStatus() + ", Response: " + errorResponse;
                    logger.severe(error);
                    return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, error);
                }
            }
            logger.log(Level.INFO,"User created successfully!");
        }
        return OperationResult.success();
    }

    private Map<String, Object> buildUserRepresentation(UserDto userDto) {
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", userDto.username());
        userPayload.put("email", userDto.email());
        userPayload.put("firstName", userDto.firstName());
        userPayload.put("lastName", userDto.lastName());
        userPayload.put("enabled", true);
        userPayload.put("credentials", Collections.singletonList(buildPasswordCredential(userDto.password())));
        return userPayload;
    }

    private Map<String, Object> buildPasswordCredential(String password) {
        Map<String, Object> passwordCred = new HashMap<>();
        passwordCred.put("type", "password");
        passwordCred.put("value", password);
        passwordCred.put("temporary", false);
        return passwordCred;
    }

}


