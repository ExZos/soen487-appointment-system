package com.example.rest;

import factories.ManagerFactory;
import repository.interfaces.IAdminManager;
import repository.interfaces.IAppointmentManager;
import repository.interfaces.IResourceManager;
import repository.interfaces.IUserManager;
import repository.pojos.Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

@Path("resource")
public class ResourceRest {
    IResourceManager resourceManager = (IResourceManager) ManagerFactory.ResourceManager.getManager();
    IAdminManager adminManager = (IAdminManager) ManagerFactory.AdminManager.getManager();
    IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();
    IAppointmentManager appointmentManager = (IAppointmentManager) ManagerFactory.AppointmentManager.getManager();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create")
    public Response create(@HeaderParam("x-api-key") String token, @HeaderParam("username") String username, @FormParam("name") String name) {
        try {
            if(name == null)
                return Response.status(Response.Status.BAD_REQUEST)
                        .build();
            if(adminManager.validateToken(username, token))
            {
                Resource resource = resourceManager.createResource(name);
                YearMonth yearMonthObject = YearMonth.of(2021, 4);
                int daysInMonth = yearMonthObject.lengthOfMonth();
                LocalDate currentDate = LocalDate.now();
                int dayOfMonth = currentDate.getDayOfMonth();

                int year = 2021;
                int month = 4;

                for(; dayOfMonth <= daysInMonth; dayOfMonth++)
                {
                    LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                    java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    if(!dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY))
                    {
                        appointmentManager.createAppointment(resource.getResourceId(), LocalDate.of(year, month, dayOfMonth));
                    }
                }

                return Response.status(Response.Status.OK)
                        .entity(resource)
                        .build();
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResources(@HeaderParam("email") String email, @HeaderParam("username") String username, @HeaderParam("x-api-key") String token) {
        try {
            if(userManager.validateToken(email, token) || adminManager.validateToken(username, token)){
                List<Resource> resources = resourceManager.getResourceList();
                GenericEntity<List<Resource>> entity = new GenericEntity<List<Resource>>(resources) {};
                return Response.status(Response.Status.OK)
                        .entity(entity)
                        .build();
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
}
