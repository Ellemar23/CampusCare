package com.example.CampusCare;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ViewmedicalInfo extends AppCompatActivity {

    TextView fullname, dob, bloodType, medicalConditions, allergies, medications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmedicalinfo);

        fullname = findViewById(R.id.fullname);
        dob = findViewById(R.id.birthdate);
        bloodType = findViewById(R.id.address);
        medicalConditions = findViewById(R.id.illnesses);
        allergies = findViewById(R.id.past_surgeries);
        medications = findViewById(R.id.currentmed);

        // Receive ID from intent
       // int id = getIntent().getIntExtra("id", -1);
        int id = 0;

        if (id != -1) {
            fetchMedicalInfo(id);
        } else {
            Toast.makeText(this, "Invalid entry ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMedicalInfo(int id) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.MedicalInfo + "?action=get",
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            fullname.setText(data.getString("name"));
                            dob.setText(data.getString("dob"));
                            bloodType.setText(data.getString("bloodType"));
                            medicalConditions.setText(data.getString("medicalConditions"));
                            allergies.setText(data.getString("allergies"));
                            medications.setText(data.getString("medications"));
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching details", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(id));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
