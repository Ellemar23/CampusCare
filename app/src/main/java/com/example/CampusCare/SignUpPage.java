package com.example.CampusCare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {

    Button SignUp;
    EditText email, password, confirmPassword, name, age, gender, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        name = findViewById(R.id.Name);
        age = findViewById(R.id.Age);
        gender = findViewById(R.id.Gender);
        contact = findViewById(R.id.Contact);
        SignUp = findViewById(R.id.SignUp);

        SignUp.setOnClickListener(v -> {
            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String ConfirmPassword = confirmPassword.getText().toString().trim();
            String Name = name.getText().toString().trim();
            String Age = age.getText().toString().trim();
            String Gender = gender.getText().toString().trim();
            String Contact = contact.getText().toString().trim();

            signUp(Email, Password, ConfirmPassword, Name, Age, Gender, Contact);
        });
    }

    private void signUp(String Email, String Password, String ConfirmPassword, String Name, String Age, String Gender, String Contact) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.SIGNUP,
                response -> {
                    if (response.equals("success")) {
                        SharedPreferences prefs = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE);
                        String userName = prefs.getString("user_name", "User");
                        Toast.makeText(this, "Sign Up successful! Welcome " + userName, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUpPage.this, LogInPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Show server message (like "Email already exists", or errors)
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                // keys must match PHP keys (lowercase)
                map.put("name", Name);
                map.put("gender", Gender);
                map.put("age", Age);
                map.put("contact", Contact);
                map.put("email", Email);
                map.put("password", Password);
                map.put("confirmPassword", ConfirmPassword);
                return map;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
