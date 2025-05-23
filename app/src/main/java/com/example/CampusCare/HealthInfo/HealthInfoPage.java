package com.example.CampusCare.HealthInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HealthInfoPage extends AppCompatActivity {

    private EditText edtBloodGroup, edtHeight, edtWeight, edtAllergies, edtConditions, edtMedications, edtCheckupDate, edtEmergencyContact;
    private Button btnEdit;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthinfo);

        edtBloodGroup = findViewById(R.id.edt_blood_group);
        edtHeight = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        edtAllergies = findViewById(R.id.edt_allergies);
        edtConditions = findViewById(R.id.edt_conditions);
        edtMedications = findViewById(R.id.edt_medications);
        edtCheckupDate = findViewById(R.id.edt_checkup_date);
        edtEmergencyContact = findViewById(R.id.edt_emergency_contact);
        btnEdit = findViewById(R.id.edit_btn);

        // Disable editing
        disableEditing();

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        if (userId != null) {
            fetchHealthInfo(userId);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditHealthInfoPage.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableEditing();
        if (userId != null) {
            fetchHealthInfo(userId);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableEditing() {
        edtBloodGroup.setEnabled(false);
        edtHeight.setEnabled(false);
        edtWeight.setEnabled(false);
        edtAllergies.setEnabled(false);
        edtConditions.setEnabled(false);
        edtMedications.setEnabled(false);
        edtCheckupDate.setEnabled(false);
        edtEmergencyContact.setEnabled(false);
    }

    private void fetchHealthInfo(String userId) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.healthInfo,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            JSONObject data = json.getJSONObject("data");
                            edtBloodGroup.setText(data.getString("blood_group"));
                            edtHeight.setText(data.getString("height"));
                            edtWeight.setText(data.getString("weight"));
                            edtAllergies.setText(data.getString("allergies"));
                            edtConditions.setText(data.getString("conditions"));
                            edtMedications.setText(data.getString("medications"));
                            edtCheckupDate.setText(data.getString("last_checkup"));
                            edtEmergencyContact.setText(data.getString("emergency_contact"));
                        } else {
                            Toast.makeText(this, json.optString("message", "No health info found."), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get"); // Required by PHP backend
                params.put("user_id", userId);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
