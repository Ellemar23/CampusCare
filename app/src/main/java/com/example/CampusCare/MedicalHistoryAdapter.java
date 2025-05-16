package com.example.campuscare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> {

    private List<MedicalHistory> medicalHistoryList;

    public MedicalHistoryAdapter(List<MedicalHistory> list) {
        this.medicalHistoryList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDetails;

        public ViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvDetails = view.findViewById(R.id.tvDetails);
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

        String fullDetails = "Name: " + history.getName() + "\nDOB: " + history.getDob()
                + "\nBlood Type: " + history.getBloodType()
                + "\nMedical Conditions: " + history.getMedicalConditions()
                + "\nAllergies: " + history.getAllergies()
                + "\nMedications: " + history.getMedications();

        holder.tvDetails.setText(fullDetails);
        holder.tvDetails.setVisibility(history.isExpanded() ? View.VISIBLE : View.GONE);

        holder.tvDate.setOnClickListener(v -> {
            history.setExpanded(!history.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return medicalHistoryList.size();
    }
}
