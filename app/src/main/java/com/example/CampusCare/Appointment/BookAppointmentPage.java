package com.example.CampusCare.Appointment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class BookAppointmentPage extends AppCompatActivity {

    private EditText dateInput, reasonInput, preferredDoctorInput;
    private Spinner timeSpinner;
    private RadioGroup appointmentTypeGroup;
    private Button confirmButton;

    private final List<String> holidayDates = new ArrayList<>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);

        preferredDoctorInput = findViewById(R.id.preferredDoctorInput);
        dateInput = findViewById(R.id.dateInput);
        reasonInput = findViewById(R.id.reasonInput);
        timeSpinner = findViewById(R.id.timeSpinner);
        appointmentTypeGroup = findViewById(R.id.appointmentTypeGroup);
        confirmButton = findViewById(R.id.confirmButton);

        prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);

        String[] times = {"9:00 AM", "10:30 AM", "1:00 PM", "3:00 PM"};
        timeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times));

        fetchHolidays();

        dateInput.setOnClickListener(v -> showDatePicker());

        confirmButton.setOnClickListener(v -> {
            String preferredDoctor = preferredDoctorInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String time = timeSpinner.getSelectedItem().toString();
            String reason = reasonInput.getText().toString().trim();
            int selectedTypeId = appointmentTypeGroup.getCheckedRadioButtonId();

            if (preferredDoctor.isEmpty() || date.isEmpty() || reason.isEmpty() || selectedTypeId == -1) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = ((RadioButton) findViewById(selectedTypeId)).getText().toString();

            if (holidayDates.contains(date)) {
                Toast.makeText(this, "Cannot book on a holiday", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchAvailableDoctorAndBook(preferredDoctor, date, time, type, reason);
        });
    }

    private void fetchHolidays() {
        StringRequest request = new StringRequest(Request.Method.GET, endpoints.holidayList,
                response -> {
                    try {
                        JSONArray holidays = new JSONArray(response);
                        for (int i = 0; i < holidays.length(); i++) {
                            holidayDates.add(holidays.getString(i));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing holidays", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch holidays", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    String selected = String.format("%04d-%02d-%02d", year, month + 1, day);
                    if (holidayDates.contains(selected)) {
                        Toast.makeText(this, "Cannot book on a holiday", Toast.LENGTH_LONG).show();
                    } else {
                        dateInput.setText(selected);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
                            addAppointmentToDatabase(assignedDoctor, date, time, type, reason);
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


    private void addAppointmentToDatabase(String doctorName, String date, String time, String type, String reason) {
        String userId = prefs.getString("user_id", "-1");
        String userName = prefs.getString("user_name", "User");

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.AddAppointment,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_LONG).show();
                            clearFields();
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Response parsing failed", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Appointment booking failed", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("action", "add");
                map.put("user_id", userId);
                map.put("username", userName);
                map.put("doctor_name", doctorName);
                map.put("date", date);
                map.put("time", time);
                map.put("type", type);
                map.put("reason", reason);
                return map;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void clearFields() {
        preferredDoctorInput.setText("");
        dateInput.setText("");
        reasonInput.setText("");
        appointmentTypeGroup.clearCheck();
        timeSpinner.setSelection(0);
    }
}
