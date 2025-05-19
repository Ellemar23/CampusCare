package com.example.CampusCare.Appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentList extends AppCompatActivity {

    Button AddAppointment;
    RecyclerView recyclerView;
    List<AppointmentDetails> appointmentList;
    AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_list);

        recyclerView = findViewById(R.id.appointmentListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();

        adapter = new AppointmentAdapter(this, appointmentList, appointment -> {
            // Handle click event here
            Toast.makeText(this, "Clicked appointment: " + appointment.getDate() + " " + appointment.getTime(), Toast.LENGTH_SHORT).show();

            // Example: Open a detail screen
            Intent intent = new Intent(AppointmentList.this, ViewAppointment.class);
            intent.putExtra("date", appointment.getDate());
            intent.putExtra("time", appointment.getTime());
            intent.putExtra("doctorName", appointment.getDoctorName());
            // Add other details as needed
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        AddAppointment = findViewById(R.id.AddAppointment);
        AddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentList.this, BookAppointmentPage.class);
            startActivity(intent);
        });

        fetchAppointmentList();
    }

    private void fetchAppointmentList() {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.ListAppointments,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (!success) {
                            Toast.makeText(this, "Failed to get appointments", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray array = jsonResponse.getJSONArray("data");
                        appointmentList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String date = obj.getString("date");
                            String time = obj.getString("time");
                            String doctorName = obj.getString("doctor_name"); // Use underscore key!

                            Log.d("AppointmentList", "DoctorName: " + doctorName);

                            AppointmentDetails details = new AppointmentDetails(doctorName, date, time,"", "");
                            appointmentList.add(details);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to parse appointment data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error fetching appointments", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
