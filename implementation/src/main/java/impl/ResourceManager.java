package impl;

import database.dao.ResourceDAO;
import repository.interfaces.IResourceManager;
import repository.pojos.Resource;

import java.sql.SQLException;
import java.util.ArrayList;

public class ResourceManager implements IResourceManager {

    public ArrayList<Resource> getResourceList() throws SQLException {
        return ResourceDAO.getResourceList();
    }

    public Resource getResourceById(int resourceId) throws SQLException {
        return ResourceDAO.getResourceById(resourceId);
    }

    public Resource getResourceByName(String name) throws SQLException {
        return ResourceDAO.getResourceByName(name);
    }

    public Resource createResource(String name) throws SQLException {
        Integer id = ResourceDAO.createResource(name);

        if(id == null)
            return null;

        return ResourceDAO.getResourceById(id);
    }

//    public static void main(String[] args) {
//        try {
//            //Assume we already created 3 customers and 2 resource(dentist1 and dentist2)
//            ResourceManager resourceManager = new ResourceManager();
//
//            System.out.println("View all resources:");
//            System.out.println(resourceManager.getResourceList());
//
//            System.out.println("View resource1 by id:");
//            System.out.println(resourceManager.getResourceById(1));
//
//            System.out.println("View resource2 by name:");
//            System.out.println(resourceManager.getResourceByName("dentist2"));
//
//            System.out.println("Creating new resource:");
//            System.out.println(resourceManager.createResource("Dentist3"));
//
//            System.out.println("View all resources:");
//            System.out.println(resourceManager.getResourceList());
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
