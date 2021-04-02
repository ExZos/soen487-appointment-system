package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import repository.interfaces.*;
import repository.pojos.Appointment;
import repository.pojos.Resource;
import repository.pojos.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("appointment")
public class AppointmentRest {
    IAppointmentManager appointmentManager = (IAppointmentManager) ManagerFactory.AppointmentManager.getManager();
    IAdminManager adminManager = (IAdminManager) ManagerFactory.AdminManager.getManager();
    IResourceManager resourceManager = (IResourceManager) ManagerFactory.ResourceManager.getManager();
    IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();
    ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();

//    //ADMIN CREATES APPOINTMENTS WITH RESOURCES
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("create")
//    public Response create(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username, @FormParam("resourceId") int resourceId, @FormParam("date") String date ) {
//        try {
//            LocalDate localDate = LocalDate.parse(date);
//            if(adminManager.validateToken(username, token))
//            {
//                if(resourceManager.getResourceById(resourceId) == null)
//                {
//                    return Response.status(Response.Status.BAD_REQUEST)
//                            .build();
//                }
//                else{
//                    return Response.status(Response.Status.OK)
//                            .entity(appointmentManager.createAppointment(resourceId, localDate))
//                            .build();
//                }
//            }
//            else{
//                return Response.status(Response.Status.FORBIDDEN)
//                        .build();
//            }
//
//        } catch(Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .build();
//        }
//    }

    //Customer can book an appointment
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("book")
    public Response book(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, @FormParam("appointmentId") int appointmentId, @FormParam("userId") int userId, @FormParam("message") String message ) {
        try {
            if(userManager.validateToken(email, token))
            {
                Appointment appointment = appointmentManager.getAppointment(appointmentId);
                User user = userManager.getUserByEmail(email);
                Resource resource = resourceManager.getResourceById(appointment.getResourceId());

                if(appointment.getStatus().toString().equals("CLOSED"))
                {
                    return Response.status(Response.Status.FORBIDDEN)
                            .build();
                }
                else{
                    boolean success = calendarManager.createEventOnDate(new OAuth2AccessToken(user.getToken()), resource.getName(), appointment.getDate());
                    if(!success)
                        throw new Exception("Failed to create event");

                    return Response.status(Response.Status.OK)
                            .entity(appointmentManager.bookAppointment(appointmentId, userId, message))
                            .build();
                }
            }
            else{
                return Response.status(Response.Status.FORBIDDEN)
                        .build();
            }

        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    //Customer can cancel their own appointment
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cancel")
    public Response cancel(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, @FormParam("appointmentId") int appointmentId, @FormParam("userId") int userId) {
        try {
            Appointment appointment = appointmentManager.getAppointment(appointmentId);
            User user = userManager.getUserByEmail(email);
            if (appointment.getUserId() == userId && userManager.validateToken(email, token)) {
                boolean success = calendarManager.deleteEventOnDate(new OAuth2AccessToken(user.getToken()), appointment.getDate());
                if (!success)
                    throw new Exception("Failed to delete event");

                Appointment cancelledAppointment = appointmentManager.cancelAppointment(appointmentId);
                if (cancelledAppointment.getStatus().toString().equals("OPEN")) {
                    return Response.status(Response.Status.OK)
                            .entity(cancelledAppointment)
                            .build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN)
                            .build();
                }

            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
