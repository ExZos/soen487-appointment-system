package database.dao;

import database.db.DBConnection;
import repository.pojos.Appointment;
import repository.pojos.Resource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ResourceDAO {
    public static ArrayList<Resource> getResourceList() throws SQLException {
        ArrayList<Resource> resources = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM Resource;";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next())
            resources.add(mapResultSetToResource(rs));

        return resources;
    }

    public static Resource getResourceById(int id) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Resource WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToResource(rs);
    }

    public static Resource getResourceByName(String name) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Resource WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToResource(rs);
    }

    private static Resource mapResultSetToResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setResourceId(rs.getInt("id"));
        resource.setName(rs.getString("name"));

        return resource;
    }
    public static Integer createResource(String name) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO Resource(name) VALUES(?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();

        if(!rs.next())
            return null;

        return rs.getInt(1);
    }
}
