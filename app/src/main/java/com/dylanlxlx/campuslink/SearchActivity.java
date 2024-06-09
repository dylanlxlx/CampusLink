package com.dylanlxlx.campuslink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 设置进入和退出的过渡动画
        setupWindowTransitions();

        // 初始化底部导航视图
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            return handleNavigationItemSelected(itemId, options);
        });
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
        if (itemId == R.id.bottom_home) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else if (itemId == R.id.bottom_profile) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        } else if (itemId == R.id.bottom_settings) {
            intent = new Intent(getApplicationContext(), SettingsActivity.class);
        } else return itemId == R.id.bottom_search;
        startActivity(intent, options.toBundle());
        return true;
    }
}
