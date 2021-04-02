package com.example.rest;

import factories.ManagerFactory;
import repository.interfaces.IAdminManager;
import repository.interfaces.IResourceManager;
import repository.interfaces.IUserManager;
import repository.pojos.Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("resource")
public class ResourceRest {
    IResourceManager resourceManager = (IResourceManager) ManagerFactory.ResourceManager.getManager();
    IAdminManager adminManager = (IAdminManager) ManagerFactory.AdminManager.getManager();
    IUserManager userManager = (IUserManager) ManagerFactory.UserManager.getManager();

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
                return Response.status(Response.Status.OK)
                        .entity(resourceManager.createResource(name))
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
