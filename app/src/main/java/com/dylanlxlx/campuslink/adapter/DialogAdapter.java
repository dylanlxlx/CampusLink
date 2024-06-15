package com.dylanlxlx.campuslink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.model.Dialog;
import com.dylanlxlx.campuslink.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DialogViewHolder> {
    private List<Dialog> dialogList;
    private OnItemClickListener onItemClickListener;

    public DialogAdapter(List<Dialog> dialogList, OnItemClickListener onItemClickListener) {
        this.dialogList = dialogList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, parent, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        Dialog dialog = dialogList.get(position);
        holder.name.setText(dialog.getName());
        holder.content.setText(dialog.getContent());
        holder.time.setText(dialog.getTime());
        Picasso.get().load(dialog.getAvatar()).transform(new CircleTransform()).into(holder.avatar);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return dialogList.size();
    }

    public void removeItem(int position) {
        dialogList.remove(position);
        notifyItemRemoved(position);
    }

    public static class DialogViewHolder extends RecyclerView.ViewHolder {
        TextView name, content, time;
        ImageView avatar;

        public DialogViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imv_avatar);
            name = itemView.findViewById(R.id.tv_name);
            content = itemView.findViewById(R.id.tv_last_message);
            time = itemView.findViewById(R.id.tv_last_message_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
