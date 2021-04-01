package com.example.rest;

import factories.ManagerFactory;
import repository.interfaces.IAdminManager;
import repository.interfaces.IResourceManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("resource")
public class ResourceRest {
    IResourceManager resourceManager = (IResourceManager) ManagerFactory.ResourceManager.getManager();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("create")
    public Response create(@FormParam("name") String name) {
        try {
            if(name == null)
                return Response.status(Response.Status.FORBIDDEN)
                        .build();

            return Response.status(Response.Status.OK)
                    .entity(resourceManager.createResource(name))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
