package com.dev2ever.api.rest;

import com.dev2ever.util.OperationResult;
import com.dev2ever.api.rest.model.ApiResponse;
import com.dev2ever.model.User;
import com.dev2ever.repository.UserRepository;
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
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RolesAllowed("user")
public class UserResource {

    @Inject
    private UserRepository userRepository;

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
    public Response createUser(User newUser) {
        OperationResult<User> operationResult = userRepository.save(newUser);

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
    public Response getUsers() {
        return ApiResponse.success(userRepository.findAll()).buildOkResponse();
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
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        return userRepository.findById(id)
                .map(user -> ApiResponse.success(user).buildOkResponse())
                .orElseGet(() -> ApiResponse.success().buildNoContentResponse());
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
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        OperationResult<Void> operationResult = userRepository.deleteById(id);

        if (operationResult.isSuccess()) {
            return ApiResponse.success().buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
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
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User updatedUser) {
        OperationResult<User> operationResult = userRepository.updateUserFields(id, updatedUser);

        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }
}
