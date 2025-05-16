package com.example.CampusCare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMedicalInfornation extends AppCompatActivity {

    EditText etPatientName, etDOB, etBloodType, etMedicalConditions, etAllergies, etMedications;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_information);

        etPatientName = findViewById(R.id.etPatientName);
        etDOB = findViewById(R.id.etDOB);
        etBloodType = findViewById(R.id.etBloodType);
        etMedicalConditions = findViewById(R.id.etMedicalConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnSave = findViewById(R.id.btnSave);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        etDOB.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String name = etPatientName.getText().toString().trim();
            String dob = etDOB.getText().toString().trim();
            String bloodType = etBloodType.getText().toString().trim();
            String medicalConditions = etMedicalConditions.getText().toString().trim();
            String allergies = etAllergies.getText().toString().trim();
            String medications = etMedications.getText().toString().trim();

            if (name.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Please enter Name and Date of Birth.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveMedicalInfo(userId, name, dob, bloodType, medicalConditions, allergies, medications);
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String dob = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    etDOB.setText(dob);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveMedicalInfo(String userId, String name, String dob, String bloodType,
                                 String medicalConditions, String allergies, String medications) {
        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddMedicalInfornation.this, LogInPage.class);
            startActivity(intent);
            finish();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.MedicalInfo + "?action=add",
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddMedicalInfornation.this, MedicalInformation.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Failed: " + obj.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("name", name);
                params.put("dob", dob);
                params.put("bloodType", bloodType);
                params.put("medicalConditions", medicalConditions);
                params.put("allergies", allergies);
                params.put("medications", medications);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}