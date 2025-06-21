package com.dev2ever.api;

import com.dev2ever.model.User;
import com.dev2ever.repository.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RolesAllowed("user")
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @POST
    public Response createUser(User user) {
        userRepository.save(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @GET
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
