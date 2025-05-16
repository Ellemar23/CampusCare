package com.example.CampusCare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MedicalInformation extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MedicalHistory> historyList = new ArrayList<>();
    MedicalHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_information);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchMedicalHistoryList();
    }

    private void fetchMedicalHistoryList() {
        StringRequest request = new StringRequest(Request.Method.GET, endpoints.GetMedicalHistoryList,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        historyList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String dateCreated = obj.getString("dateCreated");
                            String name = obj.getString("name");
                            // Only date and name for the list item
                            MedicalHistory mh = new MedicalHistory(name, "", "", "", "", "", dateCreated);
                            historyList.add(mh);
                        }
                        adapter = new MedicalHistoryAdapter(historyList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to parse medical history list", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load medical history list", Toast.LENGTH_SHORT).show()
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
