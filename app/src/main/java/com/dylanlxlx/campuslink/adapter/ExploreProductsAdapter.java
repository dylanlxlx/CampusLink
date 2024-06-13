package com.dylanlxlx.campuslink.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.ProductDetailActivity;
import com.dylanlxlx.campuslink.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExploreProductsAdapter extends RecyclerView.Adapter<ExploreProductsAdapter.ExploreProductViewHolder> {

    Context context;
    ArrayList<Product> productList;

    public ExploreProductsAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ExploreProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_pop_list_item, parent, false);
        return new ExploreProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExploreProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String description = product.getDescription();
        if (description.length() > 25) {
            description = description.substring(0, 15) + "...";
        }
        holder.titleTxt.setText(product.getTitle() + "\n" + description);
        holder.feeTxt.setText("￥" + product.getPrice());
        Picasso.get().load(product.getImage()).into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("title", product.getTitle());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image", product.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ExploreProductViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView titleTxt, feeTxt;

        public ExploreProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
        }
    }
}

