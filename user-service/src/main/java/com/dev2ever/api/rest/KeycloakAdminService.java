package com.dev2ever.api.rest;

import com.dev2ever.model.ErrorCode;
import com.dev2ever.util.OperationResult;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class KeycloakAdminService {

    private final Logger logger = Logger.getLogger(KeycloakAdminService.class.getName());

    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8180";
    private static final String KEYCLOAK_REALM = "expense-tracker";
    private static final String KEYCLOAK_CLIENT_ID = "user-service-api";
    private static final String KEYCLOAK_CLIENT_SECRET = "39dLCdPxu31vv3jxeUPRlRQYqmgrOyE9";

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_SERVER_URL)
                .realm(KEYCLOAK_REALM)
                .grantType("client_credentials")
                .clientId(KEYCLOAK_CLIENT_ID)
                .clientSecret(KEYCLOAK_CLIENT_SECRET)
                .build();
    }

    @PreDestroy
    public void close() {
        if (keycloak != null) {
            keycloak.close();
        }
    }

    public OperationResult<Void> createUser(UserDto userDto) {
        logger.log(Level.INFO, "Creating user in Keycloak... {0}", userDto);
        try {
            UserRepresentation userRepresentation = buildUserRepresentation(userDto);
            UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();

            try (Response response = usersResource.create(userRepresentation)) {
                if (response.getStatus() == 201) {
                    logger.log(Level.INFO, "User created successfully!");
                    return OperationResult.success();
                } else {
                    String errorResponse = response.readEntity(String.class);
                    String error = "Failed to create user in Keycloak. Status: " + response.getStatus() + ", Response: " + errorResponse;
                    logger.severe(error);
                    return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, error);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while creating user in Keycloak", e);
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public OperationResult<UserDto> findUserByUsername(String username) {
        logger.log(Level.INFO, "Finding user by username in Keycloak... {0}", username);
        try {
            UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();
            Optional<UserRepresentation> optional = usersResource.search(username, true).stream().findFirst();
            if(optional.isPresent()){
                UserRepresentation userRepresentation = optional.get();
                UserDto userDto = buildUserDto(userRepresentation);
                logger.log(Level.INFO, "User found successfully!");
                return OperationResult.success(userDto);
            }

            logger.log(Level.INFO, "User not found!");
            return OperationResult.success();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while finding user by username in Keycloak", e);
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public OperationResult<List<UserDto>> getAllUsers() {
        logger.log(Level.INFO, "Getting all users in Keycloak...");
        try {
            UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();
            List<UserRepresentation> userRepresentations = usersResource.list();
            List<UserDto> userDtos = userRepresentations.stream().map(this::buildUserDto).toList();
            logger.log(Level.INFO, "Users found successfully!");
            return OperationResult.success(userDtos);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while getting all users in Keycloak", e);
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public OperationResult<Void> updateUser(UserDto userDto) {
        logger.log(Level.INFO, "Updating user in Keycloak... {0}", userDto);
        try {
            if (userDto.id() == null || userDto.id().trim().isEmpty()) {
                String error = "User ID is required for update operation";
                logger.warning(error);
                return OperationResult.error(ErrorCode.BAD_REQUEST, error);
            }

            // Build user representation without credentials and without setting ID
            UserRepresentation userRepresentation = buildUserRepresentationForUpdate(userDto);

            UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();
            usersResource.get(userDto.id()).update(userRepresentation);

            // Handle password update separately if provided
            if (userDto.password() != null && !userDto.password().trim().isEmpty()) {
                updateUserPassword(userDto.id(), userDto.password());
            }

            logger.log(Level.INFO, "User updated successfully!");
            return OperationResult.success();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while updating user in Keycloak", e);
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private UserRepresentation buildUserRepresentationForUpdate(UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEnabled(true);
        return user;
    }

    private void updateUserPassword(String userId, String newPassword) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            keycloak.realm(KEYCLOAK_REALM).users().get(userId).resetPassword(credential);
            logger.log(Level.INFO, "User password updated successfully!");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to update user password", e);
        }
    }


    public OperationResult<Void> deleteUser(String id) {
        logger.log(Level.INFO, "Deleting user in Keycloak... {0}", id);
        try {
            UsersResource usersResource = keycloak.realm(KEYCLOAK_REALM).users();
            try(Response response = usersResource.delete(id)){
                if(response.getStatus() == 204){
                    logger.log(Level.INFO, "User deleted successfully!");
                    return OperationResult.success();
                } else {
                    String errorResponse = response.readEntity(String.class);
                    String error = "Failed to delete user in Keycloak. Status: " + response.getStatus() + ", Response: " + errorResponse;
                    logger.severe(error);
                    return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, error);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while deleting user in Keycloak", e);
            return OperationResult.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private UserRepresentation buildUserRepresentation(UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEnabled(true);
        user.setId(userDto.id());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.password());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        return user;
    }

    private UserDto buildUserDto(UserRepresentation userRepresentation) {
        return new UserDto(userRepresentation.getId(), userRepresentation.getUsername(), userRepresentation.getEmail(),
                userRepresentation.getFirstName(), userRepresentation.getLastName(), null);
    }
}