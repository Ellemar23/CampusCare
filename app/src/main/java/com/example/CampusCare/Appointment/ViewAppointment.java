package com.example.CampusCare.Appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewAppointment extends AppCompatActivity {

    TextView doctorText, dateText, timeText, typeText, reasonText;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_appointment);

        doctorText = findViewById(R.id.textDoctor);
        dateText = findViewById(R.id.textDate);
        timeText = findViewById(R.id.textTime);
        typeText = findViewById(R.id.textType);
        reasonText = findViewById(R.id.textReason);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = Integer.parseInt(prefs.getString("user_id", "-1"));

        String selectedDate = getIntent().getStringExtra("date");

        if (userId == -1 || selectedDate == null) {
            Toast.makeText(this, "Missing user ID or date.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchAppointmentByDate(selectedDate);
    }

    private void fetchAppointmentByDate(String date) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetAppointment,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            doctorText.setText("Doctor: " + data.getString("doctor_name"));
                            dateText.setText("Date: " + data.getString("date"));
                            timeText.setText("Time: " + data.getString("time"));
                            typeText.setText("Type: " + data.getString("type"));
                            reasonText.setText("Reason: " + data.getString("reason"));
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
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
                params.put("date", date);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
