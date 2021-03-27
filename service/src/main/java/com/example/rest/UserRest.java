package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import org.apache.http.HttpHeaders;
import repository.interfaces.ISSOManager;
import repository.interfaces.IUserManager;
import repository.pojos.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserRest {
    ISSOManager ssoManager = (ISSOManager) ManagerFactory.GoogleSSOManager.getManager();
    IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();

    @GET
    @Path("login")
    public Response login() {
        try {
            return Response.status(Response.Status.TEMPORARY_REDIRECT)
                    .header(HttpHeaders.LOCATION, ssoManager.getAuthorizationUrl())
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GET
    @Path("token")
    public Response getToken(@QueryParam("code") String code) {
        try {
            OAuth2AccessToken accessToken = ssoManager.getAccessToken(code);
            String email = ssoManager.getLoginEmail(accessToken);
            User user = userManager.getUserByEmail(email);

            if(user == null)
                userManager.createUser(email, accessToken.getAccessToken());
            else
                userManager.updateUserToken(email, accessToken.getAccessToken());;

            return Response.status(Response.Status.OK)
                    .entity(accessToken.getAccessToken())
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Not sure if needed or if UserManager.authenticate is enough
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("auth")
    public Response authenticate(@FormParam("email") String email, @HeaderParam("x-api-key") String token) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(userManager.authenticateUser(email, token))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(false)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("logout")
    public Response logout(@FormParam("email") String email, @HeaderParam("x-api-key") String token) {
        try {
            boolean success = userManager.removeUserToken(email);
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
