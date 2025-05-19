package com.example.CampusCare.Appointment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookAppointmentPage extends AppCompatActivity {

    private Spinner doctorSpinner, timeSpinner;
    private EditText dateInput, reasonInput;
    private RadioGroup appointmentTypeGroup;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);

        doctorSpinner = findViewById(R.id.doctorSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        dateInput = findViewById(R.id.dateInput);
        reasonInput = findViewById(R.id.reasonInput);
        appointmentTypeGroup = findViewById(R.id.appointmentTypeGroup);
        confirmButton = findViewById(R.id.confirmButton);

        // Sample data
        String[] doctors = {"Dr. Pretz Elle", "Dr. Ramirez", "Dr. Tan"};
        String[] times = {"9:00 AM", "10:30 AM", "1:00 PM", "3:00 PM"};

        doctorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctors));
        timeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times));

        dateInput.setOnClickListener(v -> showDatePicker());

        confirmButton.setOnClickListener(v -> {
            String doctorName = doctorSpinner.getSelectedItem().toString();
            String date = dateInput.getText().toString().trim();
            String time = timeSpinner.getSelectedItem().toString();
            String reason = reasonInput.getText().toString().trim();

            int selectedTypeId = appointmentTypeGroup.getCheckedRadioButtonId();

            if (selectedTypeId == -1 || date.isEmpty() || reason.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = ((RadioButton) findViewById(selectedTypeId)).getText().toString();

            addAppointmentToDatabase(doctorName, date, time, type, reason);
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Format date as yyyy-MM-dd
            String formattedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            dateInput.setText(formattedDate);
        }, year, month, day).show();
    }

    private void clearFields() {
        dateInput.setText("");
        reasonInput.setText("");
        appointmentTypeGroup.clearCheck();
        doctorSpinner.setSelection(0);
        timeSpinner.setSelection(0);
    }

    private void addAppointmentToDatabase(String doctorName, String date, String time, String type, String reason) {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("BookAppointment", "Sending params: user_id=" + userId + ", doctor_name=" + doctorName + ", date=" + date + ", time=" + time + ", type=" + type + ", reason=" + reason);

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.AddAppointment,
                response -> {
                    Log.d("ServerResponse", "Raw: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            clearFields();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid server response", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    String errMsg = error.getMessage();
                    if (errMsg == null) {
                        if (error.networkResponse != null) {
                            errMsg = "HTTP Error code: " + error.networkResponse.statusCode;
                        } else {
                            errMsg = "Unknown error occurred";
                        }
                    }
                    Toast.makeText(this, "Save failed: " + errMsg, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("doctor_name", doctorName);
                params.put("date", date);
                params.put("time", time);
                params.put("type", type);
                params.put("reason", reason);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,  // 10 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
