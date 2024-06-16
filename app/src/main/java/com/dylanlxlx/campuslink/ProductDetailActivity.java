package com.dylanlxlx.campuslink;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.utils.CropTransformation;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView titleTxt, descriptionTxt, priceTxt;
    private ImageView productImage, imageBack, imageReport;
    String title, description, image, price;
    int id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initView();
        getIntentData();
        setData();
        imageBack.setOnClickListener(v -> finish());
        imageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到举报界面
                Intent intent = new Intent(ProductDetailActivity.this, ReportActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        priceTxt = findViewById(R.id.priceTxt);
        productImage = findViewById(R.id.product_Pic);
        imageBack = findViewById(R.id.imageBack);
        imageReport = findViewById(R.id.imageReport);
    }

    private void setData() {
        titleTxt.setText(title);
        descriptionTxt.setText(description);
        priceTxt.setText(price);
        Picasso.get()
                .load(image)
                .transform(new CropTransformation(350, 270))
                .into(productImage);
    }

    private void getIntentData() {
        id = getIntent().getIntExtra("id",-1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        image = getIntent().getStringExtra("image");
    }
}
