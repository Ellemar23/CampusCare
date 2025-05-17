package com.example.CampusCare;

import java.util.ArrayList;
import java.util.List;

public class AppoitmentList {
    private static List<AppointmentDetails> appointmentList = new ArrayList<>();

    public static void addAppointment(AppointmentDetails appointment) {
        appointmentList.add(appointment);
    }

    public static List<AppointmentDetails> getAppointments() {
        return appointmentList;
    }
}
