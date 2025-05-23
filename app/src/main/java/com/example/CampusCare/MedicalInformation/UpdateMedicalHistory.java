package com.example.CampusCare.MedicalInformation;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMedicalHistory extends AppCompatActivity {

    EditText etName, etPastDiagnoses, etPreviousSurgeries, etChronicConditions, etHospitalizations,
            etAllergies, etMedicationsHistory, etVaccinationRecords, etFamilyMedicalHistory,
            etLifestyleFactors, etDoctorNotes, etLabTestResults;
    Button btnUpdate;

    String userId, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_history);

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
        btnUpdate = findViewById(R.id.update);

        userId = getIntent().getStringExtra("user_id");
        date = getIntent().getStringExtra("date");

        loadMedicalHistoryDetails();

        btnUpdate.setOnClickListener(v -> updateMedicalHistory());
    }

    private void loadMedicalHistoryDetails() {// Replace with your actual URL

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetMedicalHistory,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            etName.setText(data.getString("name"));
                            etPastDiagnoses.setText(data.getString("past_diagnoses"));
                            etPreviousSurgeries.setText(data.getString("previous_surgeries"));
                            etChronicConditions.setText(data.getString("chronic_conditions"));
                            etHospitalizations.setText(data.getString("hospitalizations"));
                            etAllergies.setText(data.getString("allergies"));
                            etMedicationsHistory.setText(data.getString("medications_history"));
                            etVaccinationRecords.setText(data.getString("vaccination_records"));
                            etFamilyMedicalHistory.setText(data.getString("family_medical_history"));
                            etLifestyleFactors.setText(data.getString("lifestyle_factors"));
                            etDoctorNotes.setText(data.getString("doctor_notes"));
                            etLabTestResults.setText(data.getString("lab_test_results"));
                        } else {
                            Toast.makeText(UpdateMedicalHistory.this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateMedicalHistory.this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(UpdateMedicalHistory.this, "Request failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get");
                params.put("user_id", userId);
                params.put("date", date);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void updateMedicalHistory() {

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.UpdateMedicalHistory,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Toast.makeText(UpdateMedicalHistory.this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        if (jsonResponse.getBoolean("success")) {
                            finish(); // Close activity after successful update
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateMedicalHistory.this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(UpdateMedicalHistory.this, "Request failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "update");
                params.put("user_id", userId);
                params.put("date", date);
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
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }
}
