package com.example.CampusCare.ADMIN;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class adminAppointment extends AppCompatActivity {

    EditText editTextDate, editTextTime, editTextLimit;
    Button btnSetLimit;
    RecyclerView recyclerView;
    List<adminAppointmentModel> appointmentList;
    adminAppointmentAdapter adapter;
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_appointment);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextLimit = findViewById(R.id.editTextLimit);
        btnSetLimit = findViewById(R.id.btnSetLimit);
        recyclerView = findViewById(R.id.recyclerViewAppointments);

        calendar = Calendar.getInstance();
        appointmentList = new ArrayList<>();
        adapter = new adminAppointmentAdapter(appointmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setupPickers();
        setupSetLimit();
        fetchAllAppointments();
    }

    private void setupPickers() {
        editTextDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, y, m, d) ->
                    editTextDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d)),
                    year, month, day).show();
        });

    }

    private void setupSetLimit() {
        btnSetLimit.setOnClickListener(v -> {
            String date = editTextDate.getText().toString().trim();
            String time = editTextTime.getText().toString().trim();
            String maxStr = editTextLimit.getText().toString().trim();

            if (date.isEmpty() || time.isEmpty() || maxStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int max;
            try {
                max = Integer.parseInt(maxStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number for max limit", Toast.LENGTH_SHORT).show();
                return;
            }

            setAppointmentLimit(date, time, max);
        });
    }

    private void setAppointmentLimit(String date, String time, int max) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.setLimit,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("action", "set_limit");
                map.put("date", date);
                map.put("time", time);
                map.put("max", String.valueOf(max));
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void fetchAllAppointments() {

        StringRequest request = new StringRequest(Request.Method.GET, endpoints.fetch_all_appointments,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            appointmentList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                appointmentList.add(new adminAppointmentModel(
                                        obj.getInt("id"),
                                        obj.getInt("user_id"),
                                        obj.getString("username"),
                                        obj.getString("doctor_name"),
                                        obj.getString("date"),
                                        obj.getString("time"),
                                        obj.getString("type"),
                                        obj.getString("reason")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }
}
