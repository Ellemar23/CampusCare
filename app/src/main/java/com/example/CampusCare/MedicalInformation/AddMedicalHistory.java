package com.example.CampusCare.MedicalInformation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import java.util.HashMap;
import java.util.Map;
//MAIN Coder: Pundavela
//Magallon
public class AddMedicalHistory extends AppCompatActivity {

    EditText etName,etPastDiagnoses, etPreviousSurgeries, etChronicConditions, etHospitalizations, etAllergies;
    EditText etMedicationsHistory, etVaccinationRecords, etFamilyMedicalHistory, etLifestyleFactors, etDoctorNotes, etLabTestResults;
    Button btnSaveMedicalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_information);

        etName = findViewById(R.id.etName);
        etPastDiagnoses = findViewById(R.id.etPastDiagnoses);
        etPreviousSurgeries = findViewById(R.id.etPreviousSurgeries);
        etChronicConditions = findViewById(R.id.etChronicConditions);
        etHospitalizations = findViewById(R.id.etHospitalizations);
        etAllergies = findViewById(R.id.etAllergies);
        etMedicationsHistory = findViewById(R.id.etMedicationsHistory);
        etVaccinationRecords = findViewById(R.id.etVaccinationRecords);
        etFamilyMedicalHistory = findViewById(R.id.etFamilyMedicalHistory);
        etLifestyleFactors = findViewById(R.id.etLifestyleFactors);
        etDoctorNotes = findViewById(R.id.etDoctorNotes);
        etLabTestResults = findViewById(R.id.etLabTestResults);

        btnSaveMedicalHistory = findViewById(R.id.btnSaveMedicalHistory);

        btnSaveMedicalHistory.setOnClickListener(v -> saveMedicalHistory());
    }

    private void saveMedicalHistory() {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.SaveMedicalInformation,
                response -> {
                    Toast.makeText(this, "Medical history saved", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(this, "Failed to save medical history", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("name", etName.getText().toString());
                params.put("past_diagnoses", etPastDiagnoses.getText().toString());
                params.put("previous_surgeries", etPreviousSurgeries.getText().toString());
                params.put("chronic_conditions", etChronicConditions.getText().toString());
                params.put("hospitalizations", etHospitalizations.getText().toString());
                params.put("allergies", etAllergies.getText().toString());
                params.put("medications_history", etMedicationsHistory.getText().toString());
                params.put("vaccination_records", etVaccinationRecords.getText().toString());
                params.put("family_medical_history", etFamilyMedicalHistory.getText().toString());
                params.put("lifestyle_factors", etLifestyleFactors.getText().toString());
                params.put("doctor_notes", etDoctorNotes.getText().toString());
                params.put("lab_test_results", etLabTestResults.getText().toString());
                params.put("action", "add");  // Important: to trigger add action in PHP
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
