package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView titleTxt, descriptionTxt, priceTxt;
    private ImageView productImage, imageBack;
    String title, description, image, price;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initView();
        getIntentData();
        setData();
        imageBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        priceTxt = findViewById(R.id.priceTxt);
        productImage = findViewById(R.id.product_Pic);
        imageBack = findViewById(R.id.imageBack);
    }

    private void setData() {
        titleTxt.setText(title);
        descriptionTxt.setText(description);
        priceTxt.setText(price);
        Picasso.get().load(image).into(productImage);
    }

    private void getIntentData() {
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        image = getIntent().getStringExtra("image");
    }


}
