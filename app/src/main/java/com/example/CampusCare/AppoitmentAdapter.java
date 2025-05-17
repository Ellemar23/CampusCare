package com.example.CampusCare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.List;

public class AppoitmentAdapter extends BaseAdapter {

    private Context context;
    private List<AppointmentDetails> appointmentList;

    public AppoitmentAdapter(Context context, List<AppointmentDetails> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        AppointmentDetails appointment = appointmentList.get(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("Doctor: " + appointment.getDoctorName() + " (" + appointment.getType() + ")");
        text2.setText("Date: " + appointment.getDate() + " | Time: " + appointment.getTime());

        return convertView;
    }
}
