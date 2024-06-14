package com.dylanlxlx.campuslink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.model.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<Record> recordList;
    private OnItemClickListener onItemClickListener;

    public RecordAdapter(List<Record> recordList, OnItemClickListener onItemClickListener) {
        this.recordList = recordList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.title.setText(record.getTitle());
        holder.content.setText(record.getContent());
        holder.time.setText(record.getTime());
        holder.name.setText(record.getName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public void removeItem(int position) {
        recordList.remove(position);
        notifyItemRemoved(position);
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, time, name;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.record_title);
            content = itemView.findViewById(R.id.record_content);
            time = itemView.findViewById(R.id.record_time);
            name = itemView.findViewById(R.id.record_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
