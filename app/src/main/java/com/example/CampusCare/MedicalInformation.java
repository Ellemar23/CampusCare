package com.example.campuscare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

        MedicalHistory history = (MedicalHistory) getIntent().getSerializableExtra("history");
        if (history != null) historyList.add(history);

        adapter = new MedicalHistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
    }
}
