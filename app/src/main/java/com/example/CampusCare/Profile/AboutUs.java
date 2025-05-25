package com.example.CampusCare.HomeDashboard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.CampusCare.R;
//MAIN Coder: Pundavela
//Pescadero
public class AboutUs extends AppCompatActivity {

    private TextView aboutText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutText = findViewById(R.id.text_about);
        backButton = findViewById(R.id.button_back);

        // Set about info text
        String aboutInfo = "CampusCare App\n\n" +
                "Version: 1.0\n\n" +
                "CampusCare is dedicated to providing seamless healthcare management for campus communities. " +
                "Our app offers appointment scheduling, medical records management, health information, and more.\n\n" +
                "Contact us:\n" +
                "Email: campuscareapp.com\n" +
                "Phone: +63 991 745 2192\n\n" +
                "© 2025 CampusCare. All rights reserved.";

        aboutText.setText(aboutInfo);

        backButton.setOnClickListener(v -> finish());
    }
}
