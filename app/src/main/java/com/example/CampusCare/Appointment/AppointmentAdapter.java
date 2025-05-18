package com.example.CampusCare.Appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.CampusCare.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<AppointmentDetails> appointmentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AppointmentDetails appointment);
    }

    public AppointmentAdapter(Context context, List<AppointmentDetails> appointmentList, OnItemClickListener listener) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.listener = listener;
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        AppointmentDetails appointment = appointmentList.get(position);
        holder.tvDate.setText(appointment.getDate());
        holder.tvTime.setText(appointment.getTime());
        holder.doctorName.setText(appointment.getDoctorName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, doctorName;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            doctorName = itemView.findViewById(R.id.doctorName);
        }
    }
}
