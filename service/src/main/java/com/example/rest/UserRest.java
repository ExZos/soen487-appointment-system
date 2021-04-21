package com.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import org.json.simple.JSONObject;
import repository.interfaces.ISSOManager;
import repository.interfaces.IUserManager;
import repository.pojos.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserRest {
    private ISSOManager ssoManager = (ISSOManager) ManagerFactory.GoogleSSOManager.getManager();
    private IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("login")
    public Response login(@QueryParam("isWebOrigin") boolean isWebOrigin) {
        try {
            String authUrl = ssoManager.getAuthorizationUrl(isWebOrigin);

            return Response.status(Response.Status.OK)
                    .entity(authUrl)
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("token")
    public Response getToken(@QueryParam("code") String code, @QueryParam("isWebOrigin") boolean isWebOrigin) {
        try {
            OAuth2AccessToken accessToken = ssoManager.getAccessToken(code, isWebOrigin);
            String email = ssoManager.getLoginEmail(accessToken);
            User user = userManager.getUserByEmail(email);

            if(user == null)
                user = userManager.createUser(email, accessToken.getAccessToken());
            else
                user.setToken(userManager.updateUserToken(email, accessToken.getAccessToken()));

            ObjectMapper mapper = new ObjectMapper();

            return Response.status(Response.Status.OK)
                    .entity(mapper.writeValueAsString(user))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Not sure if needed or if UserManager.authenticate is enough
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("auth")
    public Response authenticate(@HeaderParam("email") String email, @HeaderParam("x-api-key") String token) {
        try {
            boolean isAuthenticated = userManager.validateToken(email, token);

            JSONObject jo = new JSONObject();
            jo.put("isAuthenticated", isAuthenticated);
            ObjectMapper mapper = new ObjectMapper();

            return Response.status(Response.Status.OK)
                    .entity(mapper.writeValueAsBytes(jo))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(false)
                    .build();
        }
    }

    @POST
    @Path("logout")
    public Response logout(@HeaderParam("email") String email, @HeaderParam("x-api-key") String token) {
        try {
            boolean validated = userManager.validateToken(email, token);
            if(!validated)
                return Response.status(Response.Status.FORBIDDEN)
                        .build();

            boolean success = userManager.logout(email);
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
