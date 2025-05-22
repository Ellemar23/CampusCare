package com.example.CampusCare.Appointment;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewAppointment extends AppCompatActivity {

    TextView doctorText, dateText, timeText, typeText, reasonText;
    Button deleteButton, updateButton;
    int userId;

    String doctorName, date, time, type, reason, id;  // id is appointment ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_appointment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ViewAppointment.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_messages) {
                startActivity(new Intent(ViewAppointment.this, MessagesPage.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ViewAppointment.this, ProfilePage.class));
                return true;
            }
            return false;
        });


        doctorText = findViewById(R.id.textDoctor);
        dateText = findViewById(R.id.textDate);
        timeText = findViewById(R.id.textTime);
        typeText = findViewById(R.id.textType);
        reasonText = findViewById(R.id.textReason);
        deleteButton = findViewById(R.id.delete);
        updateButton = findViewById(R.id.updateButton);

        String selectedDate = getIntent().getStringExtra("date");

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = Integer.parseInt(prefs.getString("user_id", "-1"));


        if (userId == -1 || selectedDate == null) {
            Toast.makeText(this, "Missing user ID or date.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchAppointmentByDate(selectedDate);

        updateButton.setOnClickListener(v -> {
            if (id == null || id.isEmpty()) {
                Toast.makeText(ViewAppointment.this, "Appointment ID not found.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ViewAppointment.this, UpdateAppointment.class);
            intent.putExtra("id", id);
            intent.putExtra("doctor_name", doctorName);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            intent.putExtra("type", type);
            intent.putExtra("reason", reason);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Appointment")
                    .setMessage("Are you sure you want to delete this appointment?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAppointmentByDate(userId, date))
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void fetchAppointmentByDate(String dateParam) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetAppointment,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            id = data.getString("id");  // <-- important: get appointment id here
                            doctorName = data.getString("doctor_name");
                            date = data.getString("date");
                            time = data.getString("time");
                            type = data.getString("type");
                            reason = data.getString("reason");

                            doctorText.setText("Doctor: " + doctorName);
                            dateText.setText("Date: " + date);
                            timeText.setText("Time: " + time);
                            typeText.setText("Type: " + type);
                            reasonText.setText("Reason: " + reason);
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get");
                params.put("user_id", String.valueOf(userId));
                params.put("date", dateParam);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deleteAppointmentByDate(int userId, String date) {
        if (date == null || date.isEmpty()) {
            Toast.makeText(this, "Cannot delete appointment: date is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest fetchIdRequest = new StringRequest(Request.Method.POST, endpoints.GetAppointmentId,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            int appointmentId = obj.getInt("id");

                            deleteAppointmentById(appointmentId);
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing appointment ID.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "get_id");
                params.put("user_id", String.valueOf(userId));
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(fetchIdRequest);
    }

    private void deleteAppointmentById(int appointmentId) {
        AlertDialog progressDialog = new AlertDialog.Builder(this)
                .setTitle("Deleting...")
                .setMessage("Please wait")
                .setCancelable(false)
                .create();
        progressDialog.show();

        StringRequest deleteRequest = new StringRequest(Request.Method.POST, endpoints.DeleteAppointment,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, "Appointment deleted successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing delete response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Network error on delete: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "delete");
                params.put("id", String.valueOf(appointmentId));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(deleteRequest);
    }
}
