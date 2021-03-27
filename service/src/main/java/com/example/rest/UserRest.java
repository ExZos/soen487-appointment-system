package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import org.apache.http.HttpHeaders;
import repository.interfaces.ISSOManager;
import repository.interfaces.IUserManager;
import repository.pojos.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GET
    @Path("token")
    public Response setAccessToken(@QueryParam("code") String code) {
        try {
            OAuth2AccessToken accessToken = ssoManager.getAccessToken(code);
            String email = ssoManager.getLoginEmail(accessToken);
            User user = userManager.getUserByEmail(email);

            if(user == null)
                userManager.createUser(email, accessToken.getAccessToken().toString());
            else
                userManager.updateUserToken(user.getUserId(), accessToken.getAccessToken().toString());

            return Response.status(Response.Status.OK)
                    .entity("You are now logged in")
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GET
    @Path("test/{userId}")
    public Response test(@PathParam("userId") int userId) {
        try {
            User user = userManager.getUserById(userId);
            OAuth2AccessToken accessToken = new OAuth2AccessToken(user.getToken());

            return Response.status(Response.Status.OK)
                    .entity("TEST")
                    .build();
        } catch(Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
