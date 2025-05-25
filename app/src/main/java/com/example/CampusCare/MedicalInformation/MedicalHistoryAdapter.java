package com.example.CampusCare.MedicalInformation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.CampusCare.R;

import java.util.List;
//MAIN Coder: Pundavela
//Magallon
public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> {

    private List<MedicalHistory> medicalHistoryList;

    public MedicalHistoryAdapter(List<MedicalHistory> list) {
        this.medicalHistoryList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvName = view.findViewById(R.id.tvName);
        }
    }

    @Override
    public MedicalHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicalHistoryAdapter.ViewHolder holder, int position) {
        MedicalHistory history = medicalHistoryList.get(position);
        holder.tvDate.setText(history.getDateCreated());
        holder.tvName.setText(history.getName());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ViewmedicalHistory.class);
            intent.putExtra("dateCreated", history.getDateCreated());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicalHistoryList.size();
    }
}
