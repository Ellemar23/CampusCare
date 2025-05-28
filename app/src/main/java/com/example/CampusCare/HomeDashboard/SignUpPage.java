package com.example.CampusCare.HomeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

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
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.SIGNUP,
                response -> {
                    if (response.equals("success")) {
                        Toast.makeText(this, "Sign Up successful! Welcome " + Name, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUpPage.this, LogInPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
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
                  // <-- added role here
                return map;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
