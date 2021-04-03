package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import repository.interfaces.ISSOManager;
import repository.interfaces.IUserManager;
import repository.pojos.User;
import utilities.UserAgentUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserRest {
    private ISSOManager ssoManager = (ISSOManager) ManagerFactory.GoogleSSOManager.getManager();
    private IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();

    @POST
    @Path("login")
    public Response login(@HeaderParam("User-Agent") String userAgent) {
        try {
            boolean isWebOrigin = UserAgentUtils.isWebUserAgent(userAgent);
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
    @Path("token")
    public Response getToken(@HeaderParam("User-Agent") String userAgent, @QueryParam("code") String code) {
        try {
            boolean isWebOrigin = UserAgentUtils.isWebUserAgent(userAgent);
            OAuth2AccessToken accessToken = ssoManager.getAccessToken(code, isWebOrigin);
            String email = ssoManager.getLoginEmail(accessToken);
            User user = userManager.getUserByEmail(email);

            if(user == null)
                user = userManager.createUser(email, accessToken.getAccessToken());
            else
                user.setToken(userManager.updateUserToken(email, accessToken.getAccessToken()));

            return Response.status(Response.Status.OK)
                    .entity(user)
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
                    .entity(userManager.validateToken(email, token))
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
