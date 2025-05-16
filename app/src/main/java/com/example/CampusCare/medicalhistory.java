package com.example.CampusCare;

import java.io.Serializable;

public class MedicalHistory implements Serializable {
    private String name, dob, bloodType, medicalConditions, allergies, medications, dateCreated;
    private boolean isExpanded = false;

    public MedicalHistory(String name, String dob, String bloodType, String medicalConditions,
                          String allergies, String medications, String dateCreated) {
        this.name = name;
        this.dob = dob;
        this.bloodType = bloodType;
        this.medicalConditions = medicalConditions;
        this.allergies = allergies;
        this.medications = medications;
        this.dateCreated = dateCreated;
    }

    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getBloodType() { return bloodType; }
    public String getMedicalConditions() { return medicalConditions; }
    public String getAllergies() { return allergies; }
    public String getMedications() { return medications; }
    public String getDateCreated() { return dateCreated; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
