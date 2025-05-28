package com.example.CampusCare.MedicalInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.History.HistoryPage;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.Profile.ProfilePage;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//MAIN Coder: Pundavela
//Magallon
public class ViewmedicalHistory extends AppCompatActivity {

    TextView tvDateCreated, tvName, tvPastDiagnoses, tvPreviousSurgeries, tvChronicConditions,
            tvHospitalizations, tvAllergies, tvMedicationsHistory, tvVaccinationRecords,
            tvFamilyMedicalHistory, tvLifestyleFactors, tvDoctorNotes, tvLabTestResults;

    Button btnUpdate, btnDelete;

    private String dateCreated;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_medical_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ViewmedicalHistory.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(ViewmedicalHistory.this, HistoryPage.class) );
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ViewmedicalHistory.this, ProfilePage.class));
                return true;
            }
            return false;
        });

        tvDateCreated = findViewById(R.id.tvDateCreated);
        tvName = findViewById(R.id.tvName);
        tvPastDiagnoses = findViewById(R.id.tvPastDiagnoses);
        tvPreviousSurgeries = findViewById(R.id.tvPreviousSurgeries);
        tvChronicConditions = findViewById(R.id.tvChronicConditions);
        tvHospitalizations = findViewById(R.id.tvHospitalizations);
        tvAllergies = findViewById(R.id.tvAllergies);
        tvMedicationsHistory = findViewById(R.id.tvMedicationsHistory);
        tvVaccinationRecords = findViewById(R.id.tvVaccinationRecords);
        tvFamilyMedicalHistory = findViewById(R.id.tvFamilyMedicalHistory);
        tvLifestyleFactors = findViewById(R.id.tvLifestyleFactors);
        tvDoctorNotes = findViewById(R.id.tvDoctorNotes);
        tvLabTestResults = findViewById(R.id.tvLabTestResults);


        btnUpdate = findViewById(R.id.update);
        btnDelete = findViewById(R.id.delete);

        dateCreated = getIntent().getStringExtra("dateCreated");
        if (dateCreated == null || dateCreated.isEmpty()) {
            Toast.makeText(this, "No medical history selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchMedicalHistoryDetails(dateCreated);

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(ViewmedicalHistory.this, UpdateMedicalHistory.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("date", dateCreated);
            startActivity(intent);
            finish();
        });

        btnDelete.setOnClickListener(v -> deleteMedicalHistory());
    }

    private void fetchMedicalHistoryDetails(String dateCreated) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetMedicalHistory,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");


                            tvDateCreated.setText(dateCreated);
                            tvName.setText(data.optString("name", ""));
                            tvPastDiagnoses.setText(data.optString("past_diagnoses", ""));
                            tvPreviousSurgeries.setText(data.optString("previous_surgeries", ""));
                            tvChronicConditions.setText(data.optString("chronic_conditions", ""));
                            tvHospitalizations.setText(data.optString("hospitalizations", ""));
                            tvAllergies.setText(data.optString("allergies", ""));
                            tvMedicationsHistory.setText(data.optString("medications_history", ""));
                            tvVaccinationRecords.setText(data.optString("vaccination_records", ""));
                            tvFamilyMedicalHistory.setText(data.optString("family_medical_history", ""));
                            tvLifestyleFactors.setText(data.optString("lifestyle_factors", ""));
                            tvDoctorNotes.setText(data.optString("doctor_notes", ""));
                            tvLabTestResults.setText(data.optString("lab_test_results", ""));
                        } else {
                            Toast.makeText(this, "Failed to load: " + obj.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading details", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get");
                params.put("user_id", userId);
                params.put("date", dateCreated);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deleteMedicalHistory() {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.DeleteMedicalHistory,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            Toast.makeText(ViewmedicalHistory.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ViewmedicalHistory.this, "Delete failed: " + obj.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ViewmedicalHistory.this, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ViewmedicalHistory.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "delete");
                params.put("user_id", userId);
                params.put("date", dateCreated);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
