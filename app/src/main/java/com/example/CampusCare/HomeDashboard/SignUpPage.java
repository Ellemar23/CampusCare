package com.example.CampusCare.HomeDashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.R;
import com.example.CampusCare.Endpoints.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
//MAIN Coder: Pundavela

public class SignUpPage extends AppCompatActivity {

    Button SignUp;
    EditText email, password, confirmPassword, name, age, contact;
    RadioGroup genderGroup, roleGroup;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating...");
        progressDialog.setCancelable(false);

        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        genderGroup = findViewById(R.id.GenderTypeGroup);
        roleGroup = findViewById(R.id.RoleTypeGroup);
        name = findViewById(R.id.Name);
        age = findViewById(R.id.Age);
        contact = findViewById(R.id.Contact);
        SignUp = findViewById(R.id.SignUp);

        SignUp.setOnClickListener(v -> {
            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String ConfirmPassword = confirmPassword.getText().toString().trim();
            String Name = name.getText().toString().trim();
            String Age = age.getText().toString().trim();
            String Contact = contact.getText().toString().trim();

            int AGE = Integer.parseInt(Age);
            if (AGE < 1 || AGE >120){
                age.setError("INVALID AGE");
                return;
            }

            // Get selected gender RadioButton text
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            if (selectedGenderId == -1) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGenderBtn = findViewById(selectedGenderId);
            String Gender = selectedGenderBtn.getText().toString();

            // Get selected role RadioButton text
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Please select role", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRoleBtn = findViewById(selectedRoleId);
            String Role = selectedRoleBtn.getText().toString();

            signUp(Email, Password, ConfirmPassword, Name, Age, Gender, Contact, Role);
        });
    }

    private void signUp(String Email, String Password, String ConfirmPassword, String Name, String Age, String Gender, String Contact, String Role) {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.SIGNUP,
                response -> {
                    if (response.startsWith("otp_sent:")) {
                        progressDialog.dismiss();
                        String[] parts = response.split(":");
                        if (parts.length >= 4) {
                            String userId = parts[1];
                            String userName = parts[2];
                            String userRole = parts[3];

                            Toast.makeText(this, "OTP sent to your email. Please verify.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUpPage.this, OtpVerificationPage.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("userName", userName);
                            intent.putExtra("userRole", userRole);
                            intent.putExtra("email", Email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Unexpected response from server", Toast.LENGTH_LONG).show();
                        }
                    } else if (response.equals("email_exists")) {
                        Toast.makeText(this, "Email is already registered", Toast.LENGTH_LONG).show();
                    } else if (response.equals("password_mismatch")) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                },
                error -> {
                    progressDialog.dismiss();
                    if (error.networkResponse != null) {
                        String body = new String(error.networkResponse.data);
                        Toast.makeText(this, "Server Error: " + body, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("role", Role);
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                20000, // ⬅️ 20 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
