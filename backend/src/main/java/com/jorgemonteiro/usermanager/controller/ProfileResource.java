package com.jorgemonteiro.usermanager.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for the authenticated user's profile.
 */
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    /**
     * Returns the authenticated user's profile with resolved roles and actions.
     *
     * @param email the authenticated user's email from oauth2-proxy
     * @return the user profile
     */
    @GET
    @Path("/me")
    public Response me(@HeaderParam("X-Auth-Request-Email") String email) {
        // Placeholder — will be implemented in EPIC-2
        return Response.ok("{\"email\":\"" + email + "\"}").build();
    }
}
