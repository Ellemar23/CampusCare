package com.example.CampusCare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.HomeDashboard.LogInPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private EditText editName, editEmail, editCity;
    private Button btnAboutUs, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfilePage.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(ProfilePage.this, HistoryPage.class));
                return true;
            } else if (id == R.id.nav_messages) {
                startActivity(new Intent(ProfilePage.this, MessagesPage.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true; // already here
            }
            return false;
        });

        // EditText fields
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editCity = findViewById(R.id.edit_city);

        // Buttons
        btnAboutUs = findViewById(R.id.btn_about_us);
        btnLogout = findViewById(R.id.btn_logout);

        btnAboutUs.setOnClickListener(v -> {
            // Example: Open AboutUsPage (you must create this activity)
            Intent intent = new Intent(ProfilePage.this, AboutUsPage.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // Example: Return to login screen (you must create LoginPage)
            Intent intent = new Intent(ProfilePage.this, LogInPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
        });
    }
}
