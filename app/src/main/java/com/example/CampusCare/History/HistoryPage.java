package com.example.CampusCare.History;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.Profile.ProfilePage;
import com.example.CampusCare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPage extends AppCompatActivity {

    RecyclerView recyclerView;
    List<HistoryModel> historyList;
    HistoryAdapter adapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);




        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        String userIdStr = prefs.getString("user_id", null);

        if (userIdStr != null) {
            try {
                userId = Integer.toString(Integer.parseInt(userIdStr));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid user ID.", Toast.LENGTH_SHORT).show();
            }
        }

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfilePage.class));
                return true;
            }
            return false;
        });

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        // Fetch data from API

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchHistory(); // Auto-refresh appointments whenever this activity is visible
    }

    private void fetchHistory() {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.history,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String type = item.getString("type");
                                String date = item.getString("date");
                                String time = item.getString("time");

                                historyList.add(new HistoryModel(type, date, time));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
