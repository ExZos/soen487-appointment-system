package com.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import factories.ManagerFactory;
import org.json.simple.JSONObject;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            if(username == null || password == null)
                return Response.status(Response.Status.BAD_REQUEST)
                        .build();

            String token = adminManager.login(username, password);
            if(token == null)
                return Response.status(Response.Status.FORBIDDEN)
                        .build();

            Admin admin = adminManager.getAdminByUsername(username);
            ObjectMapper mapper = new ObjectMapper();

            return Response.status(Response.Status.OK)
                    .entity(mapper.writeValueAsBytes(admin))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Same with UserRest's authenticate, just putting this so that using either this or Manager is available
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("auth")
    public Response authenticate(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username) {
        try {
            boolean isAuthenticated = adminManager.validateToken(username, token);

            JSONObject jo = new JSONObject();
            jo.put("isAuthenticated", isAuthenticated);
            ObjectMapper mapper = new ObjectMapper();

            return Response.status(Response.Status.OK)
                    .entity(mapper.writeValueAsBytes(jo))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @POST
    @Path("logout")
    public Response logout(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username) {
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
