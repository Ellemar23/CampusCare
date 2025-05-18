package com.example.CampusCare.Endpoints;

public class endpoints {
    private static final String BASE_URL = "http://192.168.0.105/FinalProject/"; // Replace with your WAMP IP & folder


    public static final String LOGIN = BASE_URL + "login.php";
    public static final String SIGNUP = BASE_URL + "signup.php";
    public static final String ForgotPassword = BASE_URL + "forgotPassword.php";
    public static final String ResetPassword = BASE_URL + "resetPassword.php";
    public static final String AddAppointment = BASE_URL + "appointments.php?action=add";
    public static final String ListAppointments = BASE_URL + "appointments.php?action=list";
    public static final String GetAppointment = BASE_URL + "appointments.php?action=get";
    public static final String SaveMedicalInformation = BASE_URL + "medicalInfo.php?action=add";
    public static final String GetMedicalHistoryList = BASE_URL + "medicalInfo.php?action=list";
    public static final String MedicalInfo = BASE_URL + "medicalInfo.php?action=get";
    public static final String DeleteMedicalInfo = BASE_URL + "medicalInfo.php?action=delete";
    public static final String UpdateMedicalInfo = BASE_URL + "medicalInfo.php?action=update";

    public static final String SendMessage = BASE_URL + "message.php?action=send";
    public static final String GetMessage = BASE_URL + "message.php?action=list";
    public static final String MessageDetails = BASE_URL + "message.php?action=get";
    public static final String DeleteMessage = BASE_URL + "message.php?action=delete";
    public static final String UpdateMessage = BASE_URL + "message.php?action=update";
    public static final String GetNotificationList = BASE_URL + "notification.php?action=list";
    public static final String NotificationDetails = BASE_URL + "notification.php?action=get";
    public static final String DeleteNotification = BASE_URL + "notification.php?action=delete";
    public static final String UpdateNotification = BASE_URL + "notification.php?action=update";


}

