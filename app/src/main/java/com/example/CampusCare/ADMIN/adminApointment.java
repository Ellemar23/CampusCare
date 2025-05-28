package com.example.CampusCare.ADMIN;

    import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

    public class adminApointment {

        public static void setAppointmentLimit(Context context, String date, String time, int max) {
            String url = "http://yourdomain.com/appointments.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(context, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "set_limit");
                    params.put("date", date);
                    params.put("time", time);
                    params.put("max", String.valueOf(max));
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(request);
        }

        public interface AppointmentFetchListener {
            void onFetched(JSONArray appointments);
            void onError(String message);
        }

        public static void fetchAllAppointments(Context context, AppointmentFetchListener listener) {
            String url = "http://yourdomain.com/appointments.php?action=fetch_all";

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("success")) {
                                JSONArray data = obj.getJSONArray("data");
                                listener.onFetched(data);
                            } else {
                                listener.onError(obj.getString("message"));
                            }
                        } catch (Exception e) {
                            listener.onError("JSON Parsing error: " + e.getMessage());
                        }
                    },
                    error -> listener.onError("Volley error: " + error.getMessage())
            );

            Volley.newRequestQueue(context).add(request);
        }
    }

