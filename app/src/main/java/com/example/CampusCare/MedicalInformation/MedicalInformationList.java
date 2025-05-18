package com.example.CampusCare.MedicalInformation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalInformationList extends AppCompatActivity {

    Button AddMedicalInfo;
    RecyclerView recyclerView;
    List<MedicalHistory> historyList;
    MedicalHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_information);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();
        adapter = new MedicalHistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        AddMedicalInfo = findViewById(R.id.AddMedicalInfo);
        AddMedicalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalInformationList.this, AddMedicalInfornation.class);
            startActivity(intent);
        });

        fetchMedicalHistoryList();
    }

    private void fetchMedicalHistoryList() {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        // Only block access if not logged in
        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetMedicalHistoryList,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            String dateCreated = item.getString("dateCreated");
                            String name = item.getString("name");

                            MedicalHistory mh = new MedicalHistory(dateCreated, name, "", "", "", "", "");
                            historyList.add(mh);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse list data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching list", Toast.LENGTH_SHORT).show()
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
