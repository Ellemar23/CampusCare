package com.example.CampusCare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddMedicalInfornation extends AppCompatActivity {

    EditText etName, etDob, etBloodType, etMedicalConditions, etAllergies, etMedications;
    Button btnSaveMedicalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_information);

        etName = findViewById(R.id.etName);
        etDob = findViewById(R.id.etDOB);
        etBloodType = findViewById(R.id.etBloodType);
        etMedicalConditions = findViewById(R.id.etMedicalConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnSaveMedicalInfo = findViewById(R.id.btnSaveMedicalInfo);

        btnSaveMedicalInfo.setOnClickListener(v -> saveMedicalInfo());
    }

    private void saveMedicalInfo() {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.SaveMedicalInformation,
                response -> {
                    Toast.makeText(this, "Medical information saved", Toast.LENGTH_SHORT).show();
                    finish(); // Close after saving
                },
                error -> Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("name", etName.getText().toString());
                params.put("dob", etDob.getText().toString());
                params.put("bloodType", etBloodType.getText().toString());
                params.put("medicalConditions", etMedicalConditions.getText().toString());
                params.put("allergies", etAllergies.getText().toString());
                params.put("medications", etMedications.getText().toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
