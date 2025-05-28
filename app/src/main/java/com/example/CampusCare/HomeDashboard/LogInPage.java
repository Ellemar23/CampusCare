package com.example.CampusCare.HomeDashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.ADMIN.adminDashboard;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;
import com.example.CampusCare.Endpoints.endpoints;

import java.util.HashMap;
import java.util.Map;
//MAIN Coder: Pundavela
//Delfin
public class LogInPage extends AppCompatActivity {

    private Button logInButton;
    private EditText emailInput, passwordInput;
    private TextView signUpLink;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        logInButton = findViewById(R.id.login_button);
        signUpLink = findViewById(R.id.signup_link);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        // Open SignUp page on clicking signup link
        signUpLink.setOnClickListener(v ->
                startActivity(new Intent(LogInPage.this, SignUpPage.class))
        );

        logInButton.setOnClickListener(v -> {
            String emailStr = emailInput.getText().toString().trim();
            String passStr = passwordInput.getText().toString().trim();

            // Basic validation
            if (emailStr.isEmpty()) {
                emailInput.setError("Please enter your email");
                emailInput.requestFocus();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                emailInput.setError("Please enter a valid email");
                emailInput.requestFocus();
                return;
            }
            if (passStr.isEmpty()) {
                passwordInput.setError("Please enter your password");
                passwordInput.requestFocus();
                return;
            }

            if (!isNetworkAvailable()) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(emailStr, passStr);
        });


        // Save to SharedPreferences


    }

    private void loginUser(String emailStr, String passStr) {
        progressDialog.show(); //  Show loading

        StringRequest request = new StringRequest(Request.Method.POST, endpoints.LOGIN,
                response -> {
                    progressDialog.dismiss(); //  Hide loading



                    if (response.startsWith("otp_sent:")) {
                        String[] parts = response.split(":");
                        if (parts.length >= 4) {
                            String userId = parts[1];
                            String userName = parts[2];
                            String userRole = parts[3];

                            SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userName", userName);
                            editor.putString("userRole", userRole);
                            editor.putString("email", emailStr);
                            editor.apply();

                            Toast.makeText(this, "Welcome, " + userName, Toast.LENGTH_SHORT).show();

                            Intent intent;
                           if (userRole.equalsIgnoreCase("Admin")) {
                                intent = new Intent(LogInPage.this,adminDashboard.class);
                            } else {
                                intent = new Intent(LogInPage.this, OtpVerificationPage.class);
                                intent.putExtra("userId", userId);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userRole", userRole);
                                intent.putExtra("email", emailStr);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.equals("invalid_credentials")) {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("missing_parameters")) {
                        Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Server response: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "login");
                params.put("email", emailStr);
                params.put("password", passStr);
                return params;
            }
        };

        // Prevent duplicate requests due to retry
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                0, // no retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
