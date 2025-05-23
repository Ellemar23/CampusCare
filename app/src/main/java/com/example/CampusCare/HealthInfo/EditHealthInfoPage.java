package com.example.CampusCare.HealthInfo;

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

public class EditHealthInfoPage extends AppCompatActivity {

    private EditText edtBloodGroup, edtHeight, edtWeight, edtAllergies, edtConditions, edtMedications, edtCheckupDate, edtEmergencyContact;
    private Button btnSave;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_health_info);

        edtBloodGroup = findViewById(R.id.edt_blood_group);
        edtHeight = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        edtAllergies = findViewById(R.id.edt_allergies);
        edtConditions = findViewById(R.id.edt_conditions);
        edtMedications = findViewById(R.id.edt_medications);
        edtCheckupDate = findViewById(R.id.edt_checkup_date);
        edtEmergencyContact = findViewById(R.id.edt_emergency_contact);
        btnSave = findViewById(R.id.btn_save_health_info);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        btnSave.setOnClickListener(v -> saveHealthInfo());
    }

    private void saveHealthInfo() {
        if (userId == null) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.updateHealthInfo,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            Toast.makeText(this, "Health info updated!", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to previous screen
                        } else {
                            Toast.makeText(this, json.optString("message", "Update failed."), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "edit");  // Required by PHP backend
                params.put("user_id", userId);
                params.put("blood_group", edtBloodGroup.getText().toString());
                params.put("height", edtHeight.getText().toString());
                params.put("weight", edtWeight.getText().toString());
                params.put("allergies", edtAllergies.getText().toString());
                params.put("conditions", edtConditions.getText().toString());
                params.put("medications", edtMedications.getText().toString());
                params.put("last_checkup", edtCheckupDate.getText().toString());
                params.put("emergency_contact", edtEmergencyContact.getText().toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
