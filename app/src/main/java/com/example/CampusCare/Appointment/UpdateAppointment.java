package com.example.CampusCare.Appointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
//Main Coder: Pundavela

public class UpdateAppointment extends AppCompatActivity {

    private Spinner timeSpinner;
    private EditText dateInput, reasonInput, doctorInput;
    private RadioGroup appointmentTypeGroup;
    private Button updateButton;
    private int appointmentId = -1;

    private String doctorName, originalDate, time, type, reason;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_appointment);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("updating in...");
        progressDialog.setCancelable(false);

        doctorInput = findViewById(R.id.doctorInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        dateInput = findViewById(R.id.dateInput);
        reasonInput = findViewById(R.id.reasonInput);
        appointmentTypeGroup = findViewById(R.id.appointmentTypeGroup);
        updateButton = findViewById(R.id.confirmButton);
        updateButton.setText("Update");

        String[] times = {"9:00 AM", "10:30 AM", "1:00 PM", "3:00 PM"};

        timeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times));

        dateInput.setOnClickListener(v -> showDatePicker());

        // Get intent data
        Intent intent = getIntent();
        doctorName = intent.getStringExtra("doctor_name");
        originalDate = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        type = intent.getStringExtra("type");
        reason = intent.getStringExtra("reason");

        // Pre-fill values
        if (doctorName != null) doctorInput.setText(doctorName);
        if (originalDate != null) dateInput.setText(originalDate);
        if (time != null) timeSpinner.setSelection(getIndex(timeSpinner, time));
        if (reason != null) reasonInput.setText(reason);
        if (type != null) {
            for (int i = 0; i < appointmentTypeGroup.getChildCount(); i++) {
                RadioButton rb = (RadioButton) appointmentTypeGroup.getChildAt(i);
                if (rb.getText().toString().equalsIgnoreCase(type)) {
                    rb.setChecked(true);
                    break;
                }
            }
        }



        fetchAppointmentId();

        updateButton.setOnClickListener(v -> {
            String newDoctor = doctorInput.getText().toString().trim();
            String newDate = dateInput.getText().toString().trim();
            String newTime = timeSpinner.getSelectedItem().toString();
            String newReason = reasonInput.getText().toString().trim();
            int selectedTypeId = appointmentTypeGroup.getCheckedRadioButtonId();

            if (selectedTypeId == -1 || newDate.isEmpty() || newReason.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String newType = ((RadioButton) findViewById(selectedTypeId)).getText().toString();

            if (appointmentId != -1) {
                fetchAvailableDoctorAndBook(newDoctor, newDate, newTime, newType, newReason);
            } else {
                Toast.makeText(this, "Appointment ID not yet loaded. Try again shortly.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
            dateInput.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void fetchAppointmentId() {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetAppointmentId,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            appointmentId = obj.getInt("id");
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing appointment ID.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch appointment ID", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
                String userId = prefs.getString("user_id", "");

                Map<String, String> params = new HashMap<>();
                params.put("action", "get_id");
                params.put("user_id", userId);
                params.put("doctor_name", doctorName);
                params.put("date", originalDate);
                params.put("time", time);
                params.put("type", type);
                params.put("reason", reason);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchAvailableDoctorAndBook(String preferredDoctor, String date, String time, String type, String reason) {
        String encodedTime = time.replace(" ", "%20");
        String encodedPreferred = preferredDoctor.replace(" ", "%20");
        String url = endpoints.AssignDoctor + "&time=" + encodedTime + "&preferred=" + encodedPreferred;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            String assignedDoctor = json.getString("doctor_name");
                            updateAppointmentInDatabase(appointmentId,assignedDoctor,date, time, type, reason);
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Failed to parse doctor assignment", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Doctor assignment failed", Toast.LENGTH_LONG).show()
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateAppointmentInDatabase(int appointmentId, String doctorName, String newDate, String time, String type, String reason) {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.UpdateAppointment,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if (obj.getBoolean("success")) {
                            startActivity(new Intent(UpdateAppointment.this, AppointmentList.class));
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Update failed: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "update");
                params.put("id", String.valueOf(appointmentId));
                params.put("doctor_name", doctorName);
                params.put("date", newDate);
                params.put("time", time);
                params.put("type", type);
                params.put("reason", reason);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
