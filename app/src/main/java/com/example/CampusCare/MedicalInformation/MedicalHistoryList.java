package com.example.CampusCare.MedicalInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.History.HistoryPage;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.Profile.ProfilePage;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalHistoryList extends AppCompatActivity {

    private Button btnAddMedicalInfo;
    private RecyclerView recyclerView;
    private List<MedicalHistory> historyList;
    private MedicalHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_information);

        setupBottomNavigation();
        setupRecyclerView();
        setupAddButton();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(MedicalHistoryList.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(MedicalHistoryList.this, HistoryPage.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MedicalHistoryList.this, ProfilePage.class));
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();
        adapter = new MedicalHistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        btnAddMedicalInfo = findViewById(R.id.AddMedicalInfo);
        btnAddMedicalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MedicalHistoryList.this, AddMedicalHistory.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyList.clear();
        fetchMedicalHistoryList();
    }

    private void fetchMedicalHistoryList() {
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "-1");

        if (userId.equals("-1")) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.GetMedicalHistoryList,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.optBoolean("success", false);
                        if (!success) {
                            Toast.makeText(this, "Failed to get medical history list", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray array = jsonResponse.optJSONArray("data");
                        if (array == null) {
                            Toast.makeText(this, "No medical history data found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        historyList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.optJSONObject(i);
                            if (item == null) continue;

                            String dateCreated = item.optString("date", "Unknown Date");  // note key is "date" from PHP, not "dateCreated"
                            String name = item.optString("name", "Unknown Name");  // Your PHP does not return "name" here though!

                            // You currently only return 'date' from PHP, no 'name'. So 'name' will be "Unknown Name"
                            MedicalHistory mh = new MedicalHistory(dateCreated, name);
                            historyList.add(mh);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse medical history list", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error fetching medical history list", Toast.LENGTH_SHORT).show()
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
