package com.dylanlxlx.campuslink.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductInMyAdpter extends RecyclerView.Adapter<ProductInMyAdpter.ProductViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;

    public ProductInMyAdpter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_explore_item, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        String title = product.getTitle();
        if (title.length() > 20) {
            title = title.substring(0, 20) + "...";
        }
        holder.tvProductTitle.setText(title);
        holder.tvProductFee.setText("￥" + product.getPrice());
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.default_img)  // 当图片加载时展示的占位图
                .error(R.drawable.default_img)        // 当图片加载失败时展示的默认图片
                .into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductTitle, tvProductFee;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_pic_e);
            tvProductTitle = itemView.findViewById(R.id.tv_title_e);
            tvProductFee = itemView.findViewById(R.id.tv_fee);
        }
    }
}
