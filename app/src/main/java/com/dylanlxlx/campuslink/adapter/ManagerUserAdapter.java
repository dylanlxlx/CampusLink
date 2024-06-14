package com.dylanlxlx.campuslink.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.User;

import java.util.List;

public class ManagerUserAdapter extends RecyclerView.Adapter<ManagerUserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnItemClickListener onItemClickListener;

    public ManagerUserAdapter(List<User> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @androidx.annotation.NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText("userName:"+user.getUsername());
        holder.nameTextView.setText("name:"+user.getName());
        holder.phoneTextView.setText("phone:"+user.getPhone());
        holder.moneyTextView.setText("money:"+String.valueOf(user.getMoney()));
        holder.ageTextView.setText("age:"+String.valueOf(user.getAge()));
        holder.remarksTextView.setText("remark:"+user.getRemarks());
        Glide.with(holder.itemView.getContext()).load(user.getAvatar()).into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImageView;
        TextView usernameTextView;
        TextView nameTextView;
        TextView phoneTextView;
        TextView moneyTextView;
        TextView ageTextView;
        TextView remarksTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            moneyTextView = itemView.findViewById(R.id.moneyTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            remarksTextView = itemView.findViewById(R.id.remarksTextView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}