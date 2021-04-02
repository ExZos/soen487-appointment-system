package repository.interfaces;

import repository.pojos.Resource;
import repository.pojos.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IResourceManager extends IManager {
    ArrayList<Resource> getResourceList() throws SQLException;
    Resource getResourceById(int resourceId) throws SQLException;
    Resource getResourceByName(String name) throws SQLException;
    Resource createResource(String name) throws SQLException;
}
