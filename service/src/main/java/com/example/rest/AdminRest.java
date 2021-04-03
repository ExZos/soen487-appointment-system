package com.example.rest;

import factories.ManagerFactory;
import repository.interfaces.IAdminManager;
import repository.pojos.Admin;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminRest {
    IAdminManager adminManager = (IAdminManager) ManagerFactory.AdminManager.getManager();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("login")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            String token = adminManager.login(username, password);
            if(token == null)
                return Response.status(Response.Status.FORBIDDEN)
                        .build();

            Admin admin = adminManager.getAdminByUsername(username);

            return Response.status(Response.Status.OK)
                    .entity(admin)
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Same with UserRest's authenticate, just putting this so that using either this or Manager is available
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Path("auth")
    public Response authenticate(@HeaderParam("x-api-key") String token, @FormParam("username") String username) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(adminManager.validateToken(username, token))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("logout")
    public Response logout(@HeaderParam("x-api-key") String token, @FormParam("username") String username) {
        try {
            boolean validated = adminManager.validateToken(username, token);
            if(!validated)
                return Response.status(Response.Status.FORBIDDEN)
                        .build();

            boolean success = adminManager.logout(username);
            if(!success)
                throw new Exception("Failed to logout");

            return Response.status(Response.Status.OK)
                    .entity("You have been logged out")
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
