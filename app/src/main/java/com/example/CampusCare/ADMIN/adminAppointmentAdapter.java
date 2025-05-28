package com.example.CampusCare.ADMIN;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CampusCare.R;

import java.util.List;

public class adminAppointmentAdapter extends RecyclerView.Adapter<adminAppointmentAdapter.ViewHolder> {
    private List<adminAppointmentModel> appointmentList;

    public adminAppointmentAdapter(List<adminAppointmentModel> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public adminAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adminAppointmentAdapter.ViewHolder holder, int position) {
        adminAppointmentModel appt = appointmentList.get(position);
        holder.username.setText("User: " + appt.username + " (ID: " + appt.userId + ")");
        holder.doctorName.setText("Doctor: " + appt.doctorName);
        holder.dateTime.setText("Date: " + appt.date + " | Time: " + appt.time);
        holder.type.setText("Type: " + appt.type);
        holder.reason.setText("Reason: " + appt.reason);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, doctorName, dateTime, type, reason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernameText);
            doctorName = itemView.findViewById(R.id.doctorText);
            dateTime = itemView.findViewById(R.id.dateTimeText);
            type = itemView.findViewById(R.id.typeText);
            reason = itemView.findViewById(R.id.reasonText);
        }
    }
}

