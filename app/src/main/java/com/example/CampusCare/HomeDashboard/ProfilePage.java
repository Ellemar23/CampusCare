package com.example.CampusCare.HomeDashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.History.HistoryPage;
import com.example.CampusCare.MessagesPage;
import com.example.CampusCare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);



        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(ProfilePage.this, HomePage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_history) {
                Intent intent = new Intent(ProfilePage.this, HistoryPage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_messages) {
                Intent intent = new Intent(ProfilePage.this, MessagesPage.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}