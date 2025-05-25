package com.example.CampusCare.Appointment;

//Main Coder: Pundavela
//Delfin
public class AppointmentDetails {
    private String doctorName;
    private String date;
    private String time;
    private String type;
    private String reason;

    public AppointmentDetails(String doctorName, String date, String time, String type, String reason) {
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.type = type;
        this.reason = reason;
    }

    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getReason() { return reason; }
}
