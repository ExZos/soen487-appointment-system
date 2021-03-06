package repository.interfaces;

import repository.pojos.Appointment;
import repository.pojos.Resource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public interface IAppointmentManager extends IManager{
    ArrayList<Appointment> getUserAppointments(int userId) throws SQLException;
    Appointment getAppointment(int appointmentId) throws SQLException;
    Appointment createAppointment(int resourceId, LocalDate date) throws SQLException;
    Appointment bookAppointment(int appointmentId, int userId, String message) throws SQLException;
    Appointment cancelAppointment(int appointmentId) throws SQLException;
    //ArrayList<Appointment> getResourceAppointments(String name) throws SQLException;
    ArrayList<Appointment> getResourceAppointments(int id) throws SQLException;
    ArrayList<Appointment> getOpenAppointments() throws SQLException;
    ArrayList<Appointment> getOpenResourceAppointments(int resourceId) throws SQLException;
    void createResourceAppointments(int resourceId) throws SQLException;
}
