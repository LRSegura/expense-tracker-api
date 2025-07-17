package com.dev2ever.api.rest;

import com.dev2ever.api.rest.model.ApiResponse;
import com.dev2ever.util.OperationResult;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * REST resource for managing user operations.
 * This class provides endpoints for creating, retrieving, and managing user resources.
 * All endpoints require "user" role authorization.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RolesAllowed("user")
public class UserResource {

    @Inject
    private KeycloakAdminService keycloakAdminService;


    /**
     * Creates a new user in the system.
     *
     * @param newUser The user object containing the user details to be created
     * @return Response with status:
     * 201 (Created) if a user was successfully created
     * 409 (Conflict) if username or email already exists
     * 400 (Bad Request) if validation fails
     * 500 (Internal Server Error) if an unexpected error occurs
     */
    @POST
    @Path("/create")
    @PermitAll
    public Response createUser(UserDto newUser) {
//        return ApiResponse.success().buildCreatedResponse();
        OperationResult<Void> operationResult = keycloakAdminService.createUser(newUser);

        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildCreatedResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }

    /**
     * Retrieves all users in the system.
     *
     * @return Response with status 200 (OK) and a list of all users in the system
     */
    @GET
    @Path("/list")
    public Response getUsers() {
        return ApiResponse.success().buildCreatedResponse();
//        return ApiResponse.success(userRepository.findAll()).buildOkResponse();
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return Response with status:
     * 200 (OK) with user data if found
     * 204 (No Content) if user not found
     */
    @GET
    @Path("/find")
    public Response getUserById(@QueryParam("id") Long id) {
        return ApiResponse.success().buildCreatedResponse();
//        return userRepository.findById(id)
//                .map(user -> ApiResponse.success(user).buildOkResponse())
//                .orElseGet(() -> ApiResponse.success().buildNoContentResponse());
    }

    /**
     * Deletes a user from the system by their ID.
     *
     * @param id The ID of the user to delete
     * @return Response with status:
     * 200 (OK) if user was successfully deleted
     * 404 (Not Found) if user doesn't exist
     * 500 (Internal Server Error) if an unexpected error occurs
     */
    @DELETE
    @Path("/delete")
    public Response deleteUser(@QueryParam("id") Long id) {
        return ApiResponse.success().buildCreatedResponse();
//        OperationResult<Void> operationResult = userRepository.deleteById(id);
//
//        if (operationResult.isSuccess()) {
//            return ApiResponse.success().buildOkResponse();
//        } else {
//            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
//                    .buildDynamicErrorResponse();
//        }
    }

    /**
     * Updates an existing user in the system.
     *
     * @param id          The ID of the user to update
     * @param updatedUser The user object containing the updated user details
     * @return Response with status:
     * 200 (OK) if the user was successfully updated
     * 404 (Not Found) if the user doesn't exist
     * 409 (Conflict) if username or email already exists
     * 400 (Bad Request) if validation fails
     * 500 (Internal Server Error) if an unexpected error occurs
     */
    @PUT
    @Path("/update")
    public Response updateUser(@QueryParam("id") Long id, UserDto updatedUser) {
        return ApiResponse.success().buildCreatedResponse();
//        OperationResult<User> operationResult = userRepository.updateUserFields(id, updatedUser);
//
//        if (operationResult.isSuccess()) {
//            return ApiResponse.success(operationResult.getValue()).buildOkResponse();
//        } else {
//            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
//                    .buildDynamicErrorResponse();
//        }
    }
//
//    @Inject
//    private KeycloakAdminService keycloakAdminService;
//
//    @POST
//    @Path("/register")
//    public Response registerUser(UserCreationDto userDto) {
//        try {
//            keycloakAdminService.createUser(userDto);
//            return Response.status(Response.Status.CREATED).entity("{\"message\":\"User created successfully\"}").build();
//        } catch (Exception e) {
//            // Basic error handling
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
//                    .build();
//        }
//    }
}
