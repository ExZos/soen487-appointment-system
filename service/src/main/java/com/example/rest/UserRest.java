package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import repository.interfaces.ICalendarManager;
import repository.interfaces.ISSOManager;
import repository.interfaces.IUserManager;
import repository.pojos.User;
import utilities.UserAgentUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

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
                userManager.createUser(email, accessToken.getAccessToken());
            else
                userManager.updateUserToken(email, accessToken.getAccessToken());

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

//    // EVERYTHING BELOW HERE WAS JUST USED TO MAKE IT EASIER TO TEST THE CalendarAPIManager METHODS WITH DIFF VALUES
//    // *** Will delete everything below later ***
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("get")
//    public Response testGet(@FormParam("email") String email, @FormParam("year") int year, @FormParam("month") int month,
//                            @FormParam("dayOfMonth") int dayOfMonth) {
//        try {
//            ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();
//            User user = userManager.getUserByEmail(email);
//
//            String eventId = calendarManager.getEventIdOnDate(new OAuth2AccessToken(user.getToken()), LocalDate.of(year, month, dayOfMonth));
//            if(eventId == null)
//                throw new Exception("No event on that day");
//
//            return Response.status(Response.Status.OK)
//                    .entity(eventId)
//                    .build();
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("create")
//    public Response testCreate(@FormParam("email") String email, @FormParam("resourceName") String resourceName, @FormParam("year") int year,
//                               @FormParam("month") int month, @FormParam("dayOfMonth") int dayOfMonth) {
//        try {
//            ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();
//            User user = userManager.getUserByEmail(email);
//
//            boolean success = calendarManager.createEventOnDate(new OAuth2AccessToken(user.getToken()), resourceName, LocalDate.of(year, month, dayOfMonth));
//            if(!success)
//                throw new Exception("Failed to create event");
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("delete")
//    public Response testDelete(@FormParam("email") String email, @FormParam("year") int year, @FormParam("month") int month,
//                               @FormParam("dayOfMonth") int dayOfMonth) {
//        try {
//            ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();
//            User user = userManager.getUserByEmail(email);
//
//            boolean success = calendarManager.deleteEventOnDate(new OAuth2AccessToken(user.getToken()), LocalDate.of(year, month, dayOfMonth));
//            if(!success)
//                throw new Exception("Failed to delete event");
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("update")
//    public Response testUpdate(@FormParam("email") String email, @FormParam("fromYear") int fromYear, @FormParam("fromMonth") int fromMonth,
//                             @FormParam("fromDayInMonth") int fromDayInMonth, @FormParam("toYear") int toYear, @FormParam("toMonth") int toMonth,
//                             @FormParam("toDayInMonth") int toDayInMonth) {
//        try {
//            ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();
//            User user = userManager.getUserByEmail(email);
//
//            LocalDate from = LocalDate.of(fromYear, fromMonth, fromDayInMonth);
//            LocalDate to = LocalDate.of(toYear, toMonth, toDayInMonth);
//            boolean success = calendarManager.updateEventOnDate(new OAuth2AccessToken(user.getToken()), from, to);
//            if(!success)
//                throw new Exception("Failed to move event");
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }
}
