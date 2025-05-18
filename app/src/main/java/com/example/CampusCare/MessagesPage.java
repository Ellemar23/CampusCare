package com.example.CampusCare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.CampusCare.Endpoints.endpoints;
import com.example.CampusCare.HomeDashboard.HomePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessagesPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private LinearLayout messageContainer;
    private EditText etMessage;
    private Button btnSend;
    private ScrollView scrollView;

    private String userId;
    private final String adminId = "1"; // admin user id fixed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        messageContainer = findViewById(R.id.messageContainer);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        scrollView = findViewById(R.id.scrollView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get user id from SharedPreferences
        userId = getSharedPreferences("CampusCarePrefs", MODE_PRIVATE).getString("user_id", null);
        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.nav_messages);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(MessagesPage.this, HomePage.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(MessagesPage.this, HistoryPage.class));
                return true;
            } else if (id == R.id.nav_messages) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MessagesPage.this, ProfilePage.class));
                return true;
            }
            return false;
        });

        loadMessagesFromServer();

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToServer(message);
                etMessage.setText("");
            } else {
                Toast.makeText(MessagesPage.this, "Please type a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMessage(String message, boolean isSender) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(20, 10, 20, 10);
        textView.setBackgroundResource(isSender ? R.drawable.bubble_user : R.drawable.bubble_admin);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        params.gravity = isSender ? Gravity.END : Gravity.START;

        textView.setLayoutParams(params);
        messageContainer.addView(textView);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void sendMessageToServer(String message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoints.SendMessage,
                response -> {
                    if (response.contains("success")) {
                        displayMessage(message, true);
                        loadMessagesFromServer();
                    } else {
                        Toast.makeText(MessagesPage.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MessagesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "send");
                params.put("sender_id", userId);
                params.put("receiver_id", adminId);
                params.put("message", message);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadMessagesFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoints.GetMessage,
                response -> {
                    messageContainer.removeAllViews();

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray messages = jsonResponse.getJSONArray("messages");

                            for (int i = 0; i < messages.length(); i++) {
                                JSONObject msg = messages.getJSONObject(i);
                                String senderId = msg.getString("sender_id");
                                String msgText = msg.getString("message");

                                boolean isSender = senderId.equals(userId);
                                displayMessage(msgText, isSender);
                            }

                        } else {
                            Toast.makeText(MessagesPage.this, "No messages found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MessagesPage.this, "Failed to parse messages", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MessagesPage.this, "Error loading messages", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "list");
                params.put("user_id", userId);
                params.put("admin_id", adminId);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
