package com.dylanlxlx.campuslink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<ComplaintItem> complaints;

    public ReportAdapter(List<ComplaintItem> complaints) {
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComplaintItem complaint = complaints.get(position);
        holder.report_title_item.setText(complaint.getComplainName());
        holder.report_content_item.setText(complaint.getReason());
        holder.report_state_item.setText(complaint.getState());
    }

    public void removeItem(int position) {
        complaints.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView report_title_item;
        TextView report_content_item;
        TextView report_state_item;

        public ViewHolder(View itemView) {
            super(itemView);
            report_title_item = itemView.findViewById(R.id.edit_report_title_item);
            report_content_item = itemView.findViewById(R.id.edit_report_content_item);
            report_state_item = itemView.findViewById(R.id.edit_report_state_item);
        }
    }
}
