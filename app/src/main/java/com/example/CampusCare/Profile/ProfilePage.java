package com.example.CampusCare.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.History.HistoryPage;
import com.example.CampusCare.HomeDashboard.AboutUs;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.example.CampusCare.HomeDashboard.LogInPage;
import com.example.CampusCare.HomeDashboard.SignUpPage;
import com.example.CampusCare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView usernameText, emailText, roleText;
    private Button editProfileButton, aboutButton, accountsButton, logoutButton;
    private Button createAccountButton, deleteAccountButton;
    private Switch darkModeSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        sharedPreferences = getSharedPreferences("CampusCarePrefs", Context.MODE_PRIVATE);

        // Load views
        usernameText = findViewById(R.id.text_username);
        emailText = findViewById(R.id.text_email);
        roleText = findViewById(R.id.text_role);
        aboutButton = findViewById(R.id.btn_about);
        logoutButton = findViewById(R.id.btn_logout);
        createAccountButton = findViewById(R.id.btn_create_account);
        deleteAccountButton = findViewById(R.id.btn_delete_account);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Populate user info
        String name = sharedPreferences.getString("userName", "Username");
        String email = sharedPreferences.getString("email", "user@example.com");
        String role = sharedPreferences.getString("userRole", "user");

        usernameText.setText("Name: " + name);
        emailText.setText("Email: " + email);
        roleText.setText("Role: " + role);



        // About
        aboutButton.setOnClickListener(v ->
                startActivity(new Intent(ProfilePage.this, AboutUs.class))

        );


        // Create Account
        createAccountButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Create Account")
                    .setMessage("Are you sure you want to create another account?\n\nReminder: After creating an account, you need to log in again.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(this, "Account creation started", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(ProfilePage.this, SignUpPage.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Delete Account
        deleteAccountButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePage.this, DeleteAccount.class));
        });


        // Logout
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(ProfilePage.this, LogInPage.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfilePage.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(ProfilePage.this, HistoryPage.class));
                return true;

            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
