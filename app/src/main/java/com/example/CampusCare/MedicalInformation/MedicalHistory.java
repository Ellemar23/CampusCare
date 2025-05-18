package com.example.CampusCare.MedicalInformation;

public class MedicalHistory {
    private String dateCreated;
    private String name;
    private String dob;
    private String bloodType;
    private String medicalConditions;
    private String allergies;
    private String medications;

    public MedicalHistory(String dateCreated, String name, String dob, String bloodType,
                          String medicalConditions, String allergies, String medications) {
        this.dateCreated = dateCreated;
        this.name = name;
        this.dob = dob;
        this.bloodType = bloodType;
        this.medicalConditions = medicalConditions;
        this.allergies = allergies;
        this.medications = medications;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMedications() {
        return medications;
    }
}
