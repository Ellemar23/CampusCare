package com.example.CampusCare.MedicalInformation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.HomeDashboard.ProfilePage;
import com.example.CampusCare.MessagesPage;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewmedicalInfo extends AppCompatActivity {

    TextView dateD, fullname, dob, bloodType, medicalConditions, allergies, medications;
    Button Delete, Update;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmedicalinfo);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ViewmedicalInfo.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_messages) {
                startActivity(new Intent(ViewmedicalInfo.this, MessagesPage.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ViewmedicalInfo.this, ProfilePage.class));
                return true;
            }
            return false;
        });

        fullname = findViewById(R.id.tvFullName);
        dob = findViewById(R.id.tvDOB);
        bloodType = findViewById(R.id.tvBloodType);
        medicalConditions = findViewById(R.id.tvMedicalConditions);
        allergies = findViewById(R.id.tvAllergies);
        medications = findViewById(R.id.tvMedications);
        dateD = findViewById(R.id.tvDate);
        Delete = findViewById(R.id.delete);
        Update = findViewById(R.id.update);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", "-1");

        if (userIdStr.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int userId = Integer.parseInt(userIdStr);
        String date = getIntent().getStringExtra("dateCreated");

        if (date == null) {
            Toast.makeText(this, "No date specified.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchMedicalInfo(userId, date);

        // Delete button

        Delete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Appointment")
                    .setMessage("Are you sure you want to delete this appointment?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteMedicalInfo(userId, date))
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Update button
        Update.setOnClickListener(v -> {
            Intent intent = new Intent(ViewmedicalInfo.this, UpdateMedical.class);
            intent.putExtra("dateCreated", date);
            startActivity(intent);
        });
    }


    private void fetchMedicalInfo(int userId, String date) {
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
                            dateD.setText(data.getString("date"));
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

    private void deleteMedicalInfo(int userId, String date) {
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.DeleteMedicalInfo,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to delete: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Invalid server response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "delete"); // ✅ Required for routing
                params.put("user_id", String.valueOf(userId));
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
