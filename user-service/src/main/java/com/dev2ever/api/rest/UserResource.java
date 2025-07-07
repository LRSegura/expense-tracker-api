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

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RolesAllowed("user")
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @POST
    public Response createUser(User newUser) {
        OperationResult<User> operationResult = userRepository.save(newUser);

        if (operationResult.isSuccess()) {
            ApiResponse<User> response = ApiResponse.success(operationResult.getValue());
            return buildCreatedResponse(response);
        } else {
            ApiResponse<Void> response = ApiResponse.error(operationResult.getErrorMessage(), operationResult.getErrorCode());
            return buildBadRequestResponse(response);
        }
    }

    @GET
    public Response getUsers() {
        return buildOkResponse(userRepository.findAll());
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        return userRepository.findById(id)
                .map(this::buildOkResponse)
                .orElseGet(this::buildNoContentResponse);
    }

    private Response buildCreatedResponse(Object entity) {
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    private Response buildBadRequestResponse(Object message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }

    private Response buildOkResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response buildNoContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
