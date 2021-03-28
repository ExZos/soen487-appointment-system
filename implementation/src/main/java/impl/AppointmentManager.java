package impl;

import database.dao.AppointmentDAO;
import repository.interfaces.IAppointmentManager;
import repository.pojos.Appointment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentManager implements IAppointmentManager {
    public ArrayList<Appointment> getUserAppointments(int userId) throws SQLException {
        return AppointmentDAO.getUserAppointments(userId);
    }

    public Appointment getAppointment(int appointmentId) throws SQLException {
        return AppointmentDAO.getAppointment(appointmentId);
    }

    public Appointment createAppointment(int resourceId, LocalDate date) throws SQLException {
        Integer id = AppointmentDAO.createAppointment(resourceId, date, Appointment.Status.OPEN.toString());

        if(id == null)
            return null;

        return AppointmentDAO.getAppointment(id);
    }

    public Appointment bookAppointment(int appointmentId, int userId, String message) throws SQLException {
        Integer id =  AppointmentDAO.updateAppointment(appointmentId, userId, message, Appointment.Status.CLOSED.toString());
        return AppointmentDAO.getAppointment(id);
    }
    public Appointment cancelAppointment(int appointmentId) throws SQLException {
        int userId = -1;
        String message = null;
        Integer id =  AppointmentDAO.updateAppointment(appointmentId, userId, message, Appointment.Status.OPEN.toString());
        //Integer id =  AppointmentDAO.cancelAppointment(appointmentId, Appointment.Status.OPEN.toString());
        return AppointmentDAO.getAppointment(id);
    }

    /**
     * NOT NEEDED FOR NOW!!
     *
    public ArrayList<Appointment> getResourceAppointments(String name) throws SQLException {
        return AppointmentDAO.getResourceAppointments(name);
    }
     */

    public ArrayList<Appointment> getResourceAppointments(int id) throws SQLException {
        return AppointmentDAO.getResourceAppointments(id);
    }

//    public static void main(String[] args) {
//        try {
//            //Assume we already created 3 customers and 2 resource(dentist1 and dentist2)
//            AppointmentManager appointmentManager = new AppointmentManager();
//
//            //CREATE APPOINTMENT for dentist1
//            LocalDate date1 = LocalDate.of(2020,03,27);
//            LocalDate date2 = LocalDate.of(2020,03,28);
//            System.out.println("Dentist 1 newly created appointments: ");
//            System.out.println(appointmentManager.createAppointment(1, date1));
//            System.out.println(appointmentManager.createAppointment(1, date2));
//
//            //CREATE APPOINTMENT for dentist2
//            LocalDate date3 = LocalDate.of(2020,03,29);
//            LocalDate date4 = LocalDate.of(2020,03,30);
//            System.out.println("Dentist 2 newly created appointments: ");
//            System.out.println(appointmentManager.createAppointment(2, date3));
//            System.out.println(appointmentManager.createAppointment(2, date4));
//
//            System.out.println("\n");
//            //CUSTOMER1 books an appointment with dentist2
//            System.out.println("Customer1 books an appointment with dentist2");
//            System.out.println(appointmentManager.bookAppointment(3, 1, "Hello, customer1 booked an appointment with dentist2"));
//
//            //CUSTOMER1 also books an appointment with dentist1
//            System.out.println("Customer1 books an appointment with dentist1");
//            System.out.println(appointmentManager.bookAppointment(1, 1, "Hello, customer1 booked an appointment with dentist1"));
//
//            System.out.println("\n");
//            //Customer1 list of appointments
//            System.out.println("List of customer1's appointments:");
//            System.out.println(appointmentManager.getUserAppointments(1));
//
//            System.out.println("\n");
//            //Customer2 list of appointments
//            System.out.println("List of customer2's appointments:");
//            System.out.println(appointmentManager.getUserAppointments(2));
//
//            System.out.println("\n");
//            //Appointment 3 details
//            System.out.println("Appointment 3 details:");
//            System.out.println(appointmentManager.getAppointment(3));
//
//            System.out.println("\n");
//            //Cancel Appointment3 booking
//            System.out.println("Cancel Appointment3:");
//            System.out.println(appointmentManager.cancelAppointment(3));
//
//            System.out.println("View dentist1 appointments:");
//            System.out.println(appointmentManager.getResourceAppointments(1));
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
