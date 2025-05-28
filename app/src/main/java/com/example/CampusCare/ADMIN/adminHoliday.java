package com.example.CampusCare.ADMIN;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class adminHoliday extends AppCompatActivity {

    EditText dateInput, descriptionInput;
    Button addButton, deleteButton, checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holiday);

        dateInput = findViewById(R.id.date_input);
        descriptionInput = findViewById(R.id.description_input);
        addButton = findViewById(R.id.add_holiday_button);
        deleteButton = findViewById(R.id.delete_holiday_button);
        checkButton = findViewById(R.id.check_holiday_button);

        // Show DatePicker when clicking the date field
        dateInput.setOnClickListener(v -> showDatePicker());

        addButton.setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            String desc = descriptionInput.getText().toString().trim();
            if (date.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Enter date and description", Toast.LENGTH_SHORT).show();
            } else {
                addHoliday(date, desc);
            }
        });

        deleteButton.setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            if (date.isEmpty()) {
                Toast.makeText(this, "Enter date to delete", Toast.LENGTH_SHORT).show();
            } else {
                deleteHoliday(date);
            }
        });

        checkButton.setOnClickListener(v -> {
            String date = dateInput.getText().toString().trim();
            if (date.isEmpty()) {
                Toast.makeText(this, "Enter date to check", Toast.LENGTH_SHORT).show();
            } else {
                checkHoliday(date);
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                    dateInput.setText(formattedDate);
                },
                year, month, day);
        picker.show();
    }

    private void addHoliday(String date, String reason) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.AddHoliday,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(this, json.optString("message", "Added"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "add");
                params.put("date", date);
                params.put("reason", reason);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deleteHoliday(String date) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.DeleteHoliday,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        Toast.makeText(this, json.optString("message", "Deleted"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "delete");
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void checkHoliday(String date) {
        String url = endpoints.CheckHoliday;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean isHoliday = json.getBoolean("isHoliday");
                        Toast.makeText(this, isHoliday ? "It's a holiday!" : "Not a holiday.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "check");
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
