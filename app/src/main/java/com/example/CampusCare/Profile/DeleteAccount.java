package com.example.CampusCare.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.HomeDashboard.LogInPage;
import com.example.CampusCare.R;

import java.util.HashMap;
import java.util.Map;

public class DeleteAccount extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button deleteBtn;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    // Change this URL to your server endpoint that handles deletion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        deleteBtn = findViewById(R.id.buttonDelete);
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("CampusCarePrefs", Context.MODE_PRIVATE);

        // Optionally pre-fill email
        String savedEmail = sharedPreferences.getString("email", "");
        emailInput.setText(savedEmail);

        deleteBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty()) {
                emailInput.setError("Email required");
                emailInput.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordInput.setError("Password required");
                passwordInput.requestFocus();
                return;
            }

            // Show confirmation dialog before deleting account
            new AlertDialog.Builder(DeleteAccount.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        deleteAccount(email, password);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void deleteAccount(String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.Delete,
                response -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    if (response.trim().equals("account_deleted")) {
                        Toast.makeText(DeleteAccount.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();
                        sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(DeleteAccount.this, LogInPage.class));
                        finish();
                    } else {
                        Toast.makeText(DeleteAccount.this, "Delete failed: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(DeleteAccount.this, "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("action", "delete");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }
}
