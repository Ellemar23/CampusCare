package com.example.CampusCare.HomeDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Appointment.AppointmentList;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.HealthInfo.HealthInfoPage;
import com.example.CampusCare.History.HistoryPage;
import com.example.CampusCare.MedicalInformation.MedicalHistoryList;
import com.example.CampusCare.Profile.ProfilePage;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    private ImageButton manIcon;
    private Button btnBookAppointment, btnMyDocuments, btnHealthInfo;
    private TextView welcomeText, appointmentInfo, name;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homedashboard);

        // Initialize views
        manIcon = findViewById(R.id.man_Icon);
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        btnMyDocuments = findViewById(R.id.btn_my_documents);
        btnHealthInfo = findViewById(R.id.btn_health_info);
        welcomeText = findViewById(R.id.welcome_text);
        name = findViewById(R.id.name);
        appointmentInfo = findViewById(R.id.appointment_info);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load user data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        String userIdStr = prefs.getString("user_id", null);

        welcomeText.setText("Welcome To CampusCare,");
        name.setText(userName);

        // Fetch and display the next appointment if userId available
        if (userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                fetchNextAppointment(userId);
            } catch (NumberFormatException e) {
                appointmentInfo.setText("Invalid user ID.");
            }
        } else {
            appointmentInfo.setText("User ID not found.");
        }

        // Top icon click
        manIcon.setOnClickListener(v ->
                Toast.makeText(HomePage.this, "Profile icon clicked", Toast.LENGTH_SHORT).show()
        );

        // Main buttons click
        btnBookAppointment.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, AppointmentList.class));
        });

        btnMyDocuments.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, MedicalHistoryList.class));
        });


        btnHealthInfo.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, HealthInfoPage.class));
        });

        // Bottom Navigation click
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(HomePage.this, HistoryPage.class));
                return true;

            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomePage.this, ProfilePage.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userIdStr = prefs.getString("user_id", null);

        if (userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                fetchNextAppointment(userId);
            } catch (NumberFormatException e) {
                appointmentInfo.setText("Invalid user ID.");
            }
        } else {
            appointmentInfo.setText("User ID not found.");
        }
    }

    private void fetchNextAppointment(int userId) {

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.nextAppointment,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            JSONObject data = json.getJSONObject("data");

                            String doctor = data.getString("doctor_name");
                            String date = data.getString("date");
                            String time = data.getString("time");

                            String appointmentText = "Doctor: " + doctor + "\nDate: " + date + "\t\t\ttime: " + time;
                            appointmentInfo.setText(appointmentText);
                        } else {
                            appointmentInfo.setText("No upcoming appointment.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        appointmentInfo.setText("Error loading appointment.");
                    }
                },
                error -> {
                    error.printStackTrace();
                    appointmentInfo.setText("Network error.");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "next");
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
