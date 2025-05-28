package com.example.CampusCare.ADMIN;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.DefaultRetryPolicy;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;

import java.util.HashMap;
import java.util.Map;

public class adminDoctorAvail extends AppCompatActivity {

    private EditText nameInput, timeInput, newTimeInput, fetchTimeInput, doctorIdInput;
    private Button insertBtn, updateBtn, fetchBtn;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_doctor);

        nameInput = findViewById(R.id.nameInput);
        timeInput = findViewById(R.id.timeInput);
        newTimeInput = findViewById(R.id.newTimeInput);
        fetchTimeInput = findViewById(R.id.fetchTimeInput);
        doctorIdInput = findViewById(R.id.doctorIdInput); // You need to add this in XML
        insertBtn = findViewById(R.id.insertBtn);
        updateBtn = findViewById(R.id.updateBtn);
        fetchBtn = findViewById(R.id.fetchBtn);
        resultText = findViewById(R.id.resultText);

        insertBtn.setOnClickListener(v -> insertDoctor());
        updateBtn.setOnClickListener(v -> updateDoctorTime());
        fetchBtn.setOnClickListener(v -> fetchDoctorByTime());
    }

    private void insertDoctor() {
        String name = nameInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();

        if (name.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please enter name and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.InsertDoctor,
                response -> resultText.setText(response),
                error -> resultText.setText("Insert failed: " + error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "insert");
                params.put("name", name);
                params.put("time", time);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateDoctorTime() {
        String id = doctorIdInput.getText().toString().trim();
        String newTime = newTimeInput.getText().toString().trim();

        if (id.isEmpty() || newTime.isEmpty()) {
            Toast.makeText(this, "Please enter Doctor ID and new time.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.UpdateDoctorAvailability,
                response -> resultText.setText(response),
                error -> resultText.setText("Update failed: " + error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "update");
                params.put("id", id);
                params.put("new_time", newTime);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchDoctorByTime() {
        String time = fetchTimeInput.getText().toString().trim();

        if (time.isEmpty()) {
            Toast.makeText(this, "Please enter time to fetch.", Toast.LENGTH_SHORT).show();
            return;
        }


        StringRequest request = new StringRequest(Request.Method.POST,endpoints.FetchDoctors,
                response -> resultText.setText(response),
                error -> resultText.setText("Fetch failed: " + error.toString()));

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
