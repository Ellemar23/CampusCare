package com.example.CampusCare.HomeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;
import com.example.CampusCare.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationPage extends AppCompatActivity {

    EditText otpInput;
    Button verifyButton;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        otpInput = findViewById(R.id.otp_input);
        verifyButton = findViewById(R.id.verify_button);

        email = getIntent().getStringExtra("email");

        verifyButton.setOnClickListener(v -> {
            String otp = otpInput.getText().toString().trim();
            if (otp.isEmpty()) {
                otpInput.setError("Please enter OTP");
                return;
            }
            verifyOtp(email, otp);
        });
    }

    private void verifyOtp(String email, String otp) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.OTP,
                response -> {
                    if (response.startsWith("otp_verified:")) {
                        String[] parts = response.split(":");
                        String userId = parts[1];
                        String userName = parts[2];

                        // Save user session info
                        getSharedPreferences("CampusCarePrefs", MODE_PRIVATE)
                                .edit()
                                .putString("user_id", userId)
                                .putString("user_name", userName)
                                .apply();

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OtpVerificationPage.this, HomePage.class));
                        finish();

                    } else if (response.equals("invalid_or_expired_otp")) {
                        Toast.makeText(this, "Invalid or expired OTP", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unexpected response: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("action", "verify_otp");
                map.put("email", email);
                map.put("otp", otp);
                return map;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}