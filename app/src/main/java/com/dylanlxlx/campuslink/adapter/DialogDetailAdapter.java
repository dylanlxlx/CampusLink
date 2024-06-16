package com.dylanlxlx.campuslink.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.model.DetailDialog;
import com.dylanlxlx.campuslink.utils.CircleTransform;
import com.dylanlxlx.campuslink.utils.TimeHandle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DialogDetailAdapter extends RecyclerView.Adapter<DialogDetailAdapter.DialogDetailViewHolder> {

    private String cacheTime = null;

    private List<DetailDialog> detailDialogList;
    private OnLongClickListener onLongClickListener;

    public DialogDetailAdapter(List<DetailDialog> detailDialogList, OnLongClickListener onLongClickListener) {
        this.detailDialogList = detailDialogList;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public DialogDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_dialog, parent, false);
        return new DialogDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogDetailViewHolder holder, int position) {
        DetailDialog detailDialog = detailDialogList.get(position);
        if (detailDialog.getIsLeft()) {
            holder.rlLeft.setVisibility(View.VISIBLE);
            holder.rlRight.setVisibility(View.GONE);
            holder.leftText.setText(detailDialog.getContent());
            Picasso.get().load(detailDialog.getAvatar()).transform(new CircleTransform()).into(holder.leftImage);
        } else {
            holder.rlLeft.setVisibility(View.GONE);
            holder.rlRight.setVisibility(View.VISIBLE);
            holder.rightText.setText(detailDialog.getContent());
            Picasso.get().load(detailDialog.getAvatar()).transform(new CircleTransform()).into(holder.rightImage);
        }

        if (cacheTime == null || TimeHandle.compareTime(detailDialog.getTime(), cacheTime, 5)) {
            holder.llTime.setVisibility(View.VISIBLE);
            holder.timeText.setText(TimeHandle.formatDateTimeDialog(detailDialog.getTime()));
            Log.d("Time", "Time: " + detailDialog.getTime());
        } else {
            holder.llTime.setVisibility(View.GONE);
        }
        cacheTime = detailDialog.getTime();
        holder.itemView.setOnLongClickListener(v -> {
            onLongClickListener.onLongClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return detailDialogList != null ? detailDialogList.size() : 0;
    }

    public static class DialogDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView leftImage, rightImage;
        TextView leftText, rightText, timeText;
        LinearLayout llTime;
        RelativeLayout rlLeft, rlRight;

        public DialogDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            leftImage = itemView.findViewById(R.id.userImage_left);
            rightImage = itemView.findViewById(R.id.userImage_right);
            leftText = itemView.findViewById(R.id.tvMessage_left);
            rightText = itemView.findViewById(R.id.tvMessage_right);
            timeText = itemView.findViewById(R.id.tv_time);

            llTime = itemView.findViewById(R.id.ll_time_view);
            rlLeft = itemView.findViewById(R.id.left_dialog);
            rlRight = itemView.findViewById(R.id.right_dialog);
        }
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }
}
