package com.example.CampusCare.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.CampusCare.R;

import java.util.List;
//MAIN Coder: Pundavela
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryModel> historyList;

    public HistoryAdapter(List<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName;
        TextView tvDate;
        TextView tvTime;
        TextView tvDetails;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryModel item = historyList.get(position);

        holder.doctorName.setText(item.getType());

        // Set date and time
        holder.tvDate.setText(item.getDate());
        holder.tvTime.setText(item.getTime());


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
