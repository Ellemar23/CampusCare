package com.example.CampusCare.Endpoints;

public class endpoints {
    private static final String BASE_URL = "http://192.168.0.107/CampusCare/"; // Replace with your WAMP IP & folder


    public static final String LOGIN = BASE_URL + "login.php?action=login";
    public static final String Delete = BASE_URL + "login.php?action=delete";
    public static final String SIGNUP = BASE_URL + "signup.php";
    public static final String OTP = BASE_URL + "otp.php";
    public static final String ResetPassword = BASE_URL + "resetPassword.php";
    public static final String AddAppointment = BASE_URL + "appointments.php?action=add";
    public static final String ListAppointments = BASE_URL + "appointments.php?action=list";
    public static final String GetAppointment = BASE_URL + "appointments.php?action=get";
    public static final String GetAppointmentId = BASE_URL + "appointments.php?action=get_id";
    public static final String DeleteAppointment = BASE_URL + "appointments.php?action=delete";
    public static final String UpdateAppointment = BASE_URL + "appointments.php?action=update";
    public static final String nextAppointment = BASE_URL + "appointments.php?action=next";

    public static final String SaveMedicalInformation = BASE_URL + "medicalHistory.php?action=add";
    public static final String GetMedicalHistoryList = BASE_URL + "medicalHistory.php?action=list";
    public static final String GetMedicalHistory = BASE_URL + "medicalHistory.php?action=get";
    public static final String DeleteMedicalHistory = BASE_URL + "medicalHistory.php?action=delete";
    public static final String UpdateMedicalHistory = BASE_URL + "medicalHistory.php?action=update";

    public static final String healthInfo = BASE_URL + "healthInfo.php?action=get";
    public static final String updateHealthInfo = BASE_URL + "healthInfo.php?action=edit";



    public static final String history = BASE_URL + "history.php";



}

