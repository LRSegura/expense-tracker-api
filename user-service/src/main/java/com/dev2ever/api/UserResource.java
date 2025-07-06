package com.dev2ever.api;

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
        Result<User> result = userRepository.save(newUser);
        if (result.isSuccess()) {
            return buildCreatedResponse(result.getValue());
        } else {
            return buildBadRequestResponse(result.getError());
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

    private Response buildBadRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }

    private Response buildOkResponse(Object entity) {
        return Response.ok(entity).build();
    }

    private Response buildNoContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
