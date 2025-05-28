package com.example.CampusCare.ADMIN;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.R;
import com.example.CampusCare.ADMIN.adminHoliday;
import com.example.CampusCare.ADMIN.adminDoctorAvail;
public class adminDashboard extends AppCompatActivity {

    private Button btnManageDoctors, btnManageHolidays, btnManageAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        btnManageDoctors = findViewById(R.id.btnManageDoctors);
        btnManageHolidays = findViewById(R.id.btnManageHolidays);
        btnManageAppointments = findViewById(R.id.btnManageAppointments);

        btnManageDoctors.setOnClickListener(v -> {
            Intent intent = new Intent(adminDashboard.this, adminDoctorAvail.class);
            startActivity(intent);
        });

        btnManageHolidays.setOnClickListener(v -> {
            Intent intent = new Intent(adminDashboard.this, adminHoliday.class);
            startActivity(intent);
        });
        btnManageAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(adminDashboard.this, adminAppointment.class);
            startActivity(intent);
        });
    }
}
