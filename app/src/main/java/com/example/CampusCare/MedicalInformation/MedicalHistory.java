package com.example.CampusCare.MedicalInformation;
//MAIN Coder: Pundavela
//Magallon
public class MedicalHistory {
    private String dateCreated;
    private String name;
    private String pastDiagnoses;
    private String previousSurgeries;
    private String chronicConditions;
    private String hospitalizations;
    private String allergies;
    private String medicationsHistory;
    private String vaccinationRecords;
    private String familyMedicalHistory;
    private String lifestyleFactors;
    private String doctorNotes;
    private String labTestResults;

    // Constructor for list (summary)
    public MedicalHistory(String dateCreated, String name) {
        this.dateCreated = dateCreated;
        this.name = name;

        // Set empty for other fields
        this.pastDiagnoses = "";
        this.previousSurgeries = "";
        this.chronicConditions = "";
        this.hospitalizations = "";
        this.allergies = "";
        this.medicationsHistory = "";
        this.vaccinationRecords = "";
        this.familyMedicalHistory = "";
        this.lifestyleFactors = "";
        this.doctorNotes = "";
        this.labTestResults = "";
    }

    // Constructor for full detail
    public MedicalHistory(String dateCreated, String name, String pastDiagnoses, String previousSurgeries,
                          String chronicConditions, String hospitalizations, String allergies,
                          String medicationsHistory, String vaccinationRecords, String familyMedicalHistory,
                          String lifestyleFactors, String doctorNotes, String labTestResults) {
        this.dateCreated = dateCreated;
        this.name = name;
        this.pastDiagnoses = pastDiagnoses;
        this.previousSurgeries = previousSurgeries;
        this.chronicConditions = chronicConditions;
        this.hospitalizations = hospitalizations;
        this.allergies = allergies;
        this.medicationsHistory = medicationsHistory;
        this.vaccinationRecords = vaccinationRecords;
        this.familyMedicalHistory = familyMedicalHistory;
        this.lifestyleFactors = lifestyleFactors;
        this.doctorNotes = doctorNotes;
        this.labTestResults = labTestResults;
    }

    // Getters
    public String getDateCreated() { return dateCreated; }
    public String getName() { return name; }
    public String getPastDiagnoses() { return pastDiagnoses; }
    public String getPreviousSurgeries() { return previousSurgeries; }
    public String getChronicConditions() { return chronicConditions; }
    public String getHospitalizations() { return hospitalizations; }
    public String getAllergies() { return allergies; }
    public String getMedicationsHistory() { return medicationsHistory; }
    public String getVaccinationRecords() { return vaccinationRecords; }
    public String getFamilyMedicalHistory() { return familyMedicalHistory; }
    public String getLifestyleFactors() { return lifestyleFactors; }
    public String getDoctorNotes() { return doctorNotes; }
    public String getLabTestResults() { return labTestResults; }
}
