package com.example.CampusCare.MedicalInformation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMedical extends AppCompatActivity {

    EditText etFullName, etDob, etBloodType, etConditions, etAllergies, etMedications;
    Button btnUpdate;

    String dateCreated;
    int userId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);

        etFullName = findViewById(R.id.etFullName);
        etDob = findViewById(R.id.etDOB);
        etBloodType = findViewById(R.id.etBloodType);
        etConditions = findViewById(R.id.etMedicalConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnUpdate = findViewById(R.id.btnUpdateMedical);

        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        userId = Integer.parseInt(prefs.getString("user_id", "-1"));

        dateCreated = getIntent().getStringExtra("dateCreated");
        if (userId == -1 || dateCreated == null) {
            Toast.makeText(this, "Invalid access", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchCurrentMedicalInfo();

        btnUpdate.setOnClickListener(v -> updateMedicalInfo());
    }

    private void fetchCurrentMedicalInfo() {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.MedicalInfo,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            etFullName.setText(data.getString("name"));
                            etDob.setText(data.getString("dob"));
                            etBloodType.setText(data.getString("bloodType"));
                            etConditions.setText(data.getString("medicalConditions"));
                            etAllergies.setText(data.getString("allergies"));
                            etMedications.setText(data.getString("medications"));
                        } else {
                            Toast.makeText(this, "Data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("date", dateCreated);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateMedicalInfo() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.UpdateMedicalInfo,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateMedical.this, MedicalInformationList.class));

                        } else {
                            Toast.makeText(this, "Update failed: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "update");
                params.put("user_id", String.valueOf(userId));
                params.put("date", dateCreated);
                params.put("name", etFullName.getText().toString());
                params.put("dob", etDob.getText().toString());
                params.put("bloodType", etBloodType.getText().toString());
                params.put("medicalConditions", etConditions.getText().toString());
                params.put("allergies", etAllergies.getText().toString());
                params.put("medications", etMedications.getText().toString());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
