package repository.pojos;

import java.time.LocalDate;
import java.util.Date;

public class Appointment {
    public enum Status
    {
       OPEN, CLOSED;
    }
    private int appointmentId;
    private int resourceId;
    private int userId;
    private LocalDate date;
    private String message;
    private Status status;

    public Appointment() { }

    public Appointment(int appointmentId, int resourceId, int userId, LocalDate date, String message, Status status) {
        this.appointmentId = appointmentId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.date = date;
        this.message = message;
        this.status =  status;
    }

    public int getAppointmentId(){ return appointmentId;}
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId;}

    public int getResourceId(){ return resourceId;}
    public void setResourceId(int resourceId){ this.resourceId = resourceId;}

    public int getUserId(){ return userId;}
    public void setUserId(int userId){ this.userId = userId;}

    public LocalDate getDate(){ return date;}
    public void setDate(LocalDate date){ this.date = date;}

    public String getMessage(){ return message;}
    public void setMessage(String message){ this.message = message;}

    public Status getStatus(){ return status;}
    public void setStatus(Status status){ this.status = status;}

    public String toString() {
        String str;
        return str = "AppointmentID: " + appointmentId + " , ResourceID: " + resourceId + " , UserID: " + userId + " , DATE: " + date + " , Message: " + message + " , Status: " + status + "\n";
    }
}
