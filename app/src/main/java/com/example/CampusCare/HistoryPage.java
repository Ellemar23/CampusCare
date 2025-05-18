package com.example.CampusCare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.HomeDashboard.HomePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_history);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(HistoryPage.this, HomePage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_history) {
                return true;

            } else if (id == R.id.nav_messages) {
                // Navigate to MessagesPage
                Intent intent = new Intent(HistoryPage.this, MessagesPage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                // Navigate to ProfilePage
                Intent intent = new Intent(HistoryPage.this, ProfilePage.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
