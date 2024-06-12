package com.dylanlxlx.campuslink;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.adapter.ProductAdapter;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.ui.forgetPassword.ForgetActivity;
import com.dylanlxlx.campuslink.ui.login.LoginActivity;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_product;
    private ProductAdapter productAdapter;
    private ApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_product = findViewById(R.id.rv_product);
        rv_product.setLayoutManager(new LinearLayoutManager(this));

        // 检查用户是否已登录，如果未登录，则跳转到登录页面
        try {
            UserPreferenceManager userPreferenceManager = UserPreferenceManager.getInstance(this);
            if (UserPreferenceManager.getInstance(null).getUserId() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        apiClient = new ApiClient();

        loadProducts();

        // 设置进入和退出的过渡动画
        setupWindowTransitions();

        // 初始化底部导航视图
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            return handleNavigationItemSelected(itemId, options);
        });
    }

    private void loadProducts() {
        new Thread(() -> {
            try {
                ArrayList<Product> products = apiClient.getRandomProducts();
                runOnUiThread(() -> {
                    productAdapter = new ProductAdapter(MainActivity.this, products);
                    rv_product.setAdapter(productAdapter);
                });
            } catch (IOException | JSONException e) {
                Log.e("MainActivity", "Failed to load products");
            }
        }).start();
    }

    /**
     * 设置窗口的过渡动画
     */
    private void setupWindowTransitions() {
        getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
        getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
    }

    /**
     * 处理底部导航栏的点击事件
     *
     * @param itemId  导航项的ID
     * @param options 过渡动画选项
     * @return 如果点击的项目是当前项目，返回true，否则返回false
     */
    private boolean handleNavigationItemSelected(int itemId, ActivityOptions options) {
        Intent intent;
        if (itemId == R.id.bottom_profile) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        } else if (itemId == R.id.bottom_search) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
        } else if (itemId == R.id.bottom_dialog) {
            intent = new Intent(getApplicationContext(), DialogActivity.class);
        } else return itemId == R.id.bottom_home;
        startActivity(intent, options.toBundle());
        return true;
    }
}