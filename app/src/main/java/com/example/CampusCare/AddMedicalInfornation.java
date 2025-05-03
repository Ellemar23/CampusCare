package com.example.CampusCare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddMedicalInfornation extends AppCompatActivity {
    EditText etPatientName, etDOB, etBloodType, etMedicalConditions, etAllergies, etMedications;
    Button btnUploadFile, btnSave;
    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmedicalinformation);


        etPatientName = findViewById(R.id.etPatientName);
        etDOB = findViewById(R.id.etDOB);
        etBloodType = findViewById(R.id.etBloodType);
        etMedicalConditions = findViewById(R.id.etMedicalConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        btnSave = findViewById(R.id.btnSave);


        etDOB.setOnClickListener(v -> showDatePicker());


        btnUploadFile.setOnClickListener(v -> openFilePicker());


        btnSave.setOnClickListener(v -> saveMedicalHistory());
    }


    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    String dob = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etDOB.setText(dob);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            fileUri = data.getData();
            Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveMedicalHistory() {
        String name = etPatientName.getText().toString();
        String dob = etDOB.getText().toString();
        String bloodType = etBloodType.getText().toString();
        String conditions = etMedicalConditions.getText().toString();
        String allergies = etAllergies.getText().toString();
        String medications = etMedications.getText().toString();


        if (name.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Medical History Saved!", Toast.LENGTH_LONG).show();
        }
    }
}
