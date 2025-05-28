package com.example.CampusCare.ADMIN;

public class adminAppointmentModel {
    public int id, userId;
    public String username, doctorName, date, time, type, reason;

    public adminAppointmentModel(int id, int userId, String username, String doctorName, String date, String time, String type, String reason) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.type = type;
        this.reason = reason;
    }
}
