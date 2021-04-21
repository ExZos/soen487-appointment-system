package com.example.rest;

import com.github.scribejava.core.model.OAuth2AccessToken;
import factories.ManagerFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import repository.interfaces.*;
import repository.pojos.Appointment;
import repository.pojos.Resource;
import repository.pojos.User;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Path("appointment")
public class AppointmentRest {
    IAppointmentManager appointmentManager = (IAppointmentManager) ManagerFactory.AppointmentManager.getManager();
    IResourceManager resourceManager = (IResourceManager) ManagerFactory.ResourceManager.getManager();
    IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();
    ICalendarManager calendarManager = (ICalendarManager) ManagerFactory.CalendarManager.getManager();
    IAdminManager adminManager = (IAdminManager) ManagerFactory.AdminManager.getManager();

    //Customer can book an appointment
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("book")
    public Response book(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, @FormParam("appointmentId") int appointmentId, @FormParam("message") String message ) {
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
                            .entity(appointmentManager.bookAppointment(appointmentId, user.getUserId(), message))
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
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/{appointmentId}")
    public Response cancel(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, @PathParam("appointmentId") int appointmentId) {
        try {
            Appointment appointment = appointmentManager.getAppointment(appointmentId);
            User user = userManager.getUserByEmail(email);
            if (appointment.getUserId() == user.getUserId() && userManager.validateToken(email, token)) {
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response update(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, String data) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            System.out.println(jsonObject);

            int appointmentId =  ((Long)jsonObject.get("appointmentId")).intValue();
            int newAppointmentId = ((Long)jsonObject.get("newAppointmentId")).intValue();
            String message = URLDecoder.decode((String) jsonObject.get("message"), StandardCharsets.UTF_8.name());

            Appointment appointment = appointmentManager.getAppointment(appointmentId);
            User user = userManager.getUserByEmail(email);
            if (appointment.getUserId() == user.getUserId() && userManager.validateToken(email, token)) {
                Appointment newAppointment = appointmentManager.getAppointment(newAppointmentId);

                if(newAppointment.getStatus().toString().equals("OPEN"))
                {
                    appointmentManager.cancelAppointment(appointmentId);
                    newAppointment = appointmentManager.bookAppointment(newAppointmentId, user.getUserId(), message);

                    LocalDate from = appointment.getDate();
                    LocalDate to = newAppointment.getDate();
                    boolean success = calendarManager.updateEventOnDate(new OAuth2AccessToken(user.getToken()), from, to);
                    if(!success)
                        throw new Exception("Failed to move event");

                    return Response.status(Response.Status.OK)
                            .entity(newAppointment)
                            .build();
                }
                else
                {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .build();
                }
            }
            else {
                return Response.status(Response.Status.FORBIDDEN)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * NEWLY ADDED CODE BELOW THIS!!
     */
    //User can view their OWN appointments
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    public Response getUserAppointments(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email) {
        try {
            User user = userManager.getUserByEmail(email);

            if(userManager.validateToken(email, token))
            {
                List<Appointment> appointments = appointmentManager.getUserAppointments(user.getUserId());
                GenericEntity<List<Appointment>> entity = new GenericEntity<List<Appointment>>(appointments) {};
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            else{
                return Response.status(Response.Status.FORBIDDEN)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    //Display all open appointments with all resources
    @GET
    @Path("openAppointments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenAppointments(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email) {
        try {
            if(userManager.validateToken(email, token)) {
                List<Appointment> appointments = appointmentManager.getOpenAppointments();
                GenericEntity<List<Appointment>> entity = new GenericEntity<List<Appointment>>(appointments) {};

                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
            }
            else{
                return Response.status(Response.Status.FORBIDDEN)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    //Display ALL appointments from a specific resource
    @GET
    @Path("resourceAppointments/{resourceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourceAppointments(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username, @PathParam("resourceId") int resourceId) {
        try {
            if (adminManager.validateToken(username, token)) {
                List<Appointment> appointments = appointmentManager.getResourceAppointments(resourceId);
                GenericEntity<List<Appointment>> entity = new GenericEntity<List<Appointment>>(appointments) {
                };

                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
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
    //Display OPEN appointments with a specific resource
    @GET
    @Path("resourceAppointments/open/{resourceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenResourceAppointments(@HeaderParam("x-api-key") String token, @HeaderParam("email") String email, @PathParam("resourceId") int resourceId) {
        try {
            if (userManager.validateToken(email, token)) {
                List<Appointment> appointments = appointmentManager.getOpenResourceAppointments(resourceId);
                GenericEntity<List<Appointment>> entity = new GenericEntity<List<Appointment>>(appointments) {
                };

                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
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
    //Get appointment detail : Admin can view any appointment detail. User can only view theirs.
    @GET
    @Path("{appointmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointmentDetail(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username, @HeaderParam("email") String email, @PathParam("appointmentId") int appointmentId) {
        try {
            Appointment appointment = appointmentManager.getAppointment(appointmentId);
            if(adminManager.validateToken(username, token) || (userManager.validateToken(email, token) && appointment.getEmail().equals(email))) {

                return Response.status(Response.Status.OK)
                        .entity(appointment)
                        .build();
            }
            else{
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
