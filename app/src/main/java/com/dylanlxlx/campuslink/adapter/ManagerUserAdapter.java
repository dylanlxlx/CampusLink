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

    public ManagerUserAdapter(List<User> userList) {
        this.userList = userList;
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
        holder.usernameTextView.setText(user.getUsername());
        holder.nameTextView.setText(user.getName());
        holder.phoneTextView.setText(user.getPhone());
        holder.moneyTextView.setText(String.valueOf(user.getMoney()));
        holder.ageTextView.setText(String.valueOf(user.getAge()));
        holder.remarksTextView.setText(user.getRemarks());
        Glide.with(holder.itemView.getContext()).load(user.getAvatar()).into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
}