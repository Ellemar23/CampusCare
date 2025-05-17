package com.example.CampusCare;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewmedicalInfo extends AppCompatActivity {

    TextView fullname, dob, bloodType, medicalConditions, allergies, medications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmedicalinfo);

        fullname = findViewById(R.id.tvFullName);
        dob = findViewById(R.id.tvDOB);
        bloodType = findViewById(R.id.tvBloodType);
        medicalConditions =findViewById(R.id.tvMedicalConditions);
        allergies = findViewById(R.id.tvAllergies);
        medications = findViewById(R.id.tvMedications);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", "-1");

        if (userIdStr.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int userId = Integer.parseInt(userIdStr);
        fetchMedicalInfo(userId);
    }

    private void fetchMedicalInfo(int userId) {
        String date = getIntent().getStringExtra("dateCreated");
        if (date == null) {
            Toast.makeText(this, "No date specified.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.MedicalInfo,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            fullname.setText(data.getString("name"));
                            dob.setText(data.getString("dob"));
                            bloodType.setText(data.getString("bloodType"));
                            medicalConditions.setText(data.getString("medicalConditions"));
                            allergies.setText(data.getString("allergies"));
                            medications.setText(data.getString("medications"));
                        } else {
                            Toast.makeText(this, "Data not found for this record.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
