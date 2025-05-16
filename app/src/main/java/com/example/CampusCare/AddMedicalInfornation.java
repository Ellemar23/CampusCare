package com.example.campuscare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.util.Calendar;

public class AddMedicalInfornation extends AppCompatActivity {

    EditText etPatientName, etDOB, etBloodType, etMedicalConditions, etAllergies, etMedications;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_information);

        etPatientName = findViewById(R.id.etPatientName);
        etDOB = findViewById(R.id.etDOB);
        etBloodType = findViewById(R.id.etBloodType);
        etMedicalConditions = findViewById(R.id.etMedicalConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnSave = findViewById(R.id.btnSave);

        etDOB.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String name = etPatientName.getText().toString().trim();
            String dob = etDOB.getText().toString().trim();
            String bloodType = etBloodType.getText().toString().trim();
            String medicalConditions = etMedicalConditions.getText().toString().trim();
            String allergies = etAllergies.getText().toString().trim();
            String medications = etMedications.getText().toString().trim();

            if (name.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Please enter Name and Date of Birth.", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateCreated = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            MedicalHistory history = new MedicalHistory(name, dob, bloodType, medicalConditions, allergies, medications, dateCreated);

            Intent intent = new Intent(AddMedicalInfornation.this, MedicalInformation.class);
            intent.putExtra("history", history);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
            String dob = dayOfMonth + "/" + (month + 1) + "/" + year;
            etDOB.setText(dob);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
