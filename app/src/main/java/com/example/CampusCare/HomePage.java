package com.example.CampusCare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private ImageButton manIcon, notificationIcon;
    private Button btnBookAppointment, btnMyDocuments, btnMessages, btnHealthInfo;
    private TextView welcomeText, appointmentInfo, documentTitle, documentUploadInfo ,name;
    private BottomNavigationView bottomNavigationView; // Bottom nav

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homedashboard);

        // Initialize views
        manIcon = findViewById(R.id.man_Icon);
        notificationIcon = findViewById(R.id.notification_icon);

        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        btnMyDocuments = findViewById(R.id.btn_my_documents);
        btnMessages = findViewById(R.id.btn_messages);
        btnHealthInfo = findViewById(R.id.btn_health_info);

        welcomeText = findViewById(R.id.welcome_text);
        name = findViewById(R.id.name);
        appointmentInfo = findViewById(R.id.appointment_info);
        documentTitle = findViewById(R.id.document_title);
        documentUploadInfo = findViewById(R.id.document_upload_info);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load user name from SharedPreferences and set to welcome text
        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        String UserId = prefs.getString("user_id", "User");
        welcomeText.setText("Welcome " + UserId);


        name.setText(userName);


        // Top icon click
        manIcon.setOnClickListener(v -> {
            Toast.makeText(HomePage.this, "Profile icon clicked", Toast.LENGTH_SHORT).show();
        });

        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Notification.class);
            startActivity(intent);
        });

        // Main buttons click
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, AppointmentPage.class);
            startActivity(intent);
        });

        btnMyDocuments.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MedicalInformationList.class);
            startActivity(intent);
        });

        btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MessagesPage.class);
            startActivity(intent);
        });

        btnHealthInfo.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, HealthInfoPage.class);
            startActivity(intent);
        });

        // Bottom Navigation click
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(HomePage.this, "Home selected", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == R.id.nav_history) {
                Toast.makeText(HomePage.this, "History selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePage.this, HistoryPage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_messages) {
                Toast.makeText(HomePage.this, "Messages selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePage.this, MessagesPage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                Toast.makeText(HomePage.this, "Profile selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePage.this, ProfilePage.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
