package com.dylanlxlx.campuslink.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.ProductDetailActivity;
import com.dylanlxlx.campuslink.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<Product> productArrayList;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {

        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productArrayList.get(position);
        String description = product.getDescription();
        String title = product.getTitle();
        holder.tv_product_name.setText(title);
        holder.tv_product_descripe.setText(description);
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.default_img)  // 当图片加载时展示的占位图
                .error(R.drawable.default_img)        // 当图片加载失败时展示的默认图片
                .into(holder.siv_product_image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("title", product.getTitle());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image", product.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView siv_product_image;
        TextView tv_product_descripe;
        TextView tv_product_name;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            siv_product_image = itemView.findViewById(R.id.siv_product_image);
            tv_product_descripe = itemView.findViewById(R.id.tv_product_descripe);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
        }
    }
}
