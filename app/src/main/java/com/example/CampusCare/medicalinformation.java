package com.example.campuscare;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicalInformation extends AppCompatActivity {

    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_information);

        textViewResult = findViewById(R.id.textViewResult);

        // Retrieve data from Intent
        String name = getIntent().getStringExtra("name");
        String birthDate = getIntent().getStringExtra("birthDate");
        String bloodType = getIntent().getStringExtra("bloodType");
        String medicalCondition = getIntent().getStringExtra("medicalCondition");
        String symptoms = getIntent().getStringExtra("symptoms");
        String allergies = getIntent().getStringExtra("allergies");
        String medications = getIntent().getStringExtra("medications");

        // Display the data
        String result = "Name: " + name + "\n"
                + "Birth Date: " + birthDate + "\n"
                + "Blood Type: " + bloodType + "\n"
                + "Medical Condition: " + medicalCondition + "\n"
                + "Symptoms: " + symptoms + "\n"
                + "Allergies: " + allergies + "\n"
                + "Medications: " + medications;

        textViewResult.setText(result);
    }
}
