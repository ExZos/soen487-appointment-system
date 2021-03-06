package database.dao;

import database.db.DBConnection;
import repository.pojos.Appointment;
import repository.pojos.Resource;
import repository.pojos.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentDAO {

    public static ArrayList<Appointment> getResourceAppointments(int id) throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM Appointment WHERE resourceId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
            appointments.add(mapResultSetToAppointment(rs));

        return appointments;
    }
    public static ArrayList<Appointment> getAppointments() throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM Appointment";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
            appointments.add(mapResultSetToAppointment(rs));

        return appointments;
    }
    public static ArrayList<Appointment> getOpenAppointments() throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        String status = "OPEN";
        String sql = "SELECT * FROM Appointment WHERE STATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
            appointments.add(mapResultSetToAppointment(rs));

        return appointments;
    }

    /**
     * THIS IS TO CREATE AN OPEN SLOT WITH A RESOURCE SO A CUSTOMER CAN BOOK AN APPOINTMENT WITH!!
     *
     *
     */
    public static Integer createAppointment(int resourceId, LocalDate date, String status) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO Appointment(resourceId, appointmentDate, status) VALUES(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, resourceId);
        stmt.setDate(2, Date.valueOf(date));
        stmt.setString(3, status);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();

        if(!rs.next())
            return null;

        return rs.getInt(1);
    }

    /**
     * THIS IS WHERE A CUSTOMER CAN BOOK AN APPOINTMENT!
     *
     *
     */
    public static Integer updateAppointment(int appointmentId, int userId, String message, String status) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE Appointment SET userId = ?, message = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        if(userId > 0){
            stmt.setInt(1, userId);
        }
        else{
            stmt.setNull(1, java.sql.Types.INTEGER);
        }

        stmt.setString(2, message);
        stmt.setString(3, status);
        stmt.setInt(4, appointmentId);

        int row = stmt.executeUpdate();

        if(row <= 0)
            return null;

        return appointmentId;
    }
    //RETURN SPECIFIC APPOINTMENT DETAIL
    public static Appointment getAppointment(int appointmentId) throws SQLException {

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Appointment WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, appointmentId);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next())
            return null;

        return mapResultSetToAppointment(rs);
    }
    //RETURN LIST OF APPOINTMENTS FOR A SPECIFIC USER
    public static ArrayList<Appointment> getUserAppointments(int userId) throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Appointment WHERE userId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);

        ResultSet rs = stmt.executeQuery();
        while(rs.next())
            appointments.add(mapResultSetToAppointment(rs));

        return appointments;
    }
    public static ArrayList<Appointment> getOpenResourceAppointments(int resourceId) throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();

        String status = "OPEN";

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM Appointment WHERE resourceId = ? AND status = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, resourceId);
        stmt.setString(2, status);

        ResultSet rs = stmt.executeQuery();
        while(rs.next())
            appointments.add(mapResultSetToAppointment(rs));

        return appointments;
    }
    private static Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("id"));
        int resourceId = rs.getInt("resourceId");
        appointment.setResourceId(resourceId);
        int userId = rs.getInt("userId");
        appointment.setUserId(userId);
        appointment.setDate(rs.getDate("appointmentDate").toLocalDate());
        appointment.setMessage(rs.getString("message"));
        String status = rs.getString("status");

        Resource resource = ResourceDAO.getResourceById(resourceId);
        appointment.setResourceName(resource.getName());

        if(status.equals("OPEN"))
        {
            appointment.setStatus(Appointment.Status.OPEN);
        }
        else
        {
            appointment.setStatus(Appointment.Status.CLOSED);
        }

        if(userId != 0){
            User user = UserDAO.selectUserById(userId);
            appointment.setEmail(user.getEmail());
        }

        return appointment;
    }
}
