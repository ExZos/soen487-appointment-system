package repository.interfaces;

import repository.pojos.Appointment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface IAppointmentManager {
    ArrayList<Appointment> getUserAppointments(int userId) throws SQLException;
    Appointment getAppointment(int appointmentId) throws SQLException;
    Appointment createAppointment(int resourceId, Date date) throws SQLException;
    Appointment bookAppointment(int appointmentId, int userId, String message) throws SQLException;
    Appointment cancelAppointment(int appointmentId) throws SQLException;
    Appointment getResourceAppointments(String name) throws SQLException;
}
