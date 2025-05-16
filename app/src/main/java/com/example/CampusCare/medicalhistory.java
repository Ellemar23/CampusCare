package com.example.campuscare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicalHistory extends AppCompatActivity {

    EditText editTextName, editTextBirthDate, editTextMedicalCondition, editTextSymptoms, editTextAllergies, editTextMedications;
    Spinner spinnerBloodType;
    Button btnSave;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextMedicalCondition = findViewById(R.id.editTextMedicalCondition);
        editTextSymptoms = findViewById(R.id.editTextSymptoms);
        editTextAllergies = findViewById(R.id.editTextAllergies);
        editTextMedications = findViewById(R.id.editTextMedications);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btnSave = findViewById(R.id.btnSave);

        // Setup spinner for blood type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.blood_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);

        // Show DatePickerDialog when birth date is clicked
        editTextBirthDate.setFocusable(false);
        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        MedicalHistory.this,
                        (view, year, month, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            editTextBirthDate.setText(dateFormat.format(calendar.getTime()));
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        // Handle Save button click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                // Required fields
                String name = editTextName.getText().toString().trim();
                String birthDate = editTextBirthDate.getText().toString().trim();
                String bloodType = spinnerBloodType.getSelectedItem().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Name is required");
                    isValid = false;
                }

                if (birthDate.isEmpty()) {
                    editTextBirthDate.setError("Birth date is required");
                    isValid = false;
                }

                if (!isValid) {
                    Toast.makeText(MedicalHistory.this, "Please fill in the required fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Optional fields — use "N/A" if empty
                String medicalCondition = editTextMedicalCondition.getText().toString().trim();
                if (medicalCondition.isEmpty()) medicalCondition = "N/A";

                String symptoms = editTextSymptoms.getText().toString().trim();
                if (symptoms.isEmpty()) symptoms = "N/A";

                String allergies = editTextAllergies.getText().toString().trim();
                if (allergies.isEmpty()) allergies = "N/A";

                String medications = editTextMedications.getText().toString().trim();
                if (medications.isEmpty()) medications = "N/A";

                // Pass data to MedicalInformation activity
                Intent intent = new Intent(MedicalHistory.this, MedicalInformation.class);
                intent.putExtra("name", name);
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("bloodType", bloodType);
                intent.putExtra("medicalCondition", medicalCondition);
                intent.putExtra("symptoms", symptoms);
                intent.putExtra("allergies", allergies);
                intent.putExtra("medications", medications);
                startActivity(intent);
            }
        });
    }
}
