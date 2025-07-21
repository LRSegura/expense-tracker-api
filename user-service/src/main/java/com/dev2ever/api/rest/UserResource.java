package com.dev2ever.api.rest;

import com.dev2ever.api.rest.model.ApiResponse;
import com.dev2ever.util.OperationResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RolesAllowed("user")
public class UserResource {

    @Inject
    private KeycloakAdminService keycloakAdminService;

    @POST
    @Path("/create")
    public Response createUser(UserDto newUser) {
        OperationResult<Void> operationResult = keycloakAdminService.createUser(newUser);

        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildCreatedResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }

    @GET
    @Path("/list")
    public Response getUsers() {
        OperationResult<List<UserDto>> operationResult = keycloakAdminService.getAllUsers();
        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }

    @GET
    @Path("/find/by/username/{username}")
    public Response getUsersByUsername(@PathParam("username") String username) {
        OperationResult<UserDto> operationResult = keycloakAdminService.findUserByUsername(username);
        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    public Response deleteUser(@PathParam("id") String id) {
        OperationResult<Void> operationResult = keycloakAdminService.deleteUser(id);

        if (operationResult.isSuccess()) {
            return ApiResponse.success().buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }


    @PUT
    @Path("/update")
    public Response updateUser(UserDto updatedUser) {
        OperationResult<Void> operationResult = keycloakAdminService.updateUser(updatedUser);
        if (operationResult.isSuccess()) {
            return ApiResponse.success(operationResult.getValue()).buildOkResponse();
        } else {
            return ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode())
                    .buildDynamicErrorResponse();
        }
    }
}
