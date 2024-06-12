package com.dylanlxlx.campuslink;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // 设置进入和退出的过渡动画
        setupWindowTransitions();

        // 初始化底部导航视图
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_dialog);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            return handleNavigationItemSelected(itemId, options);
        });


        findViewById(R.id.dialog_button_clear_unread).setOnClickListener(this);
        findViewById(R.id.dialog_setting).setOnClickListener(this);
        findViewById(R.id.dialog_search_button).setOnClickListener(this);
        findViewById(R.id.dialog_bulletin).setOnClickListener(this);
        findViewById(R.id.dialog_likes).setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_button_clear_unread:
                Toast.makeText(this, "dialog_button_clear_unread", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_setting:
                Toast.makeText(this, "dialog_setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_search_button:
                Toast.makeText(this, "dialog_search_button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_bulletin:
                Intent intent = new Intent(this, BulletinActivity.class);
                startActivity(intent);
                Toast.makeText(this, "dialog_bulletin", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_likes:
                Toast.makeText(this, "dialog_likes", Toast.LENGTH_SHORT).show();
                break;
        }
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
        } else if (itemId == R.id.bottom_search) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
        } else if (itemId == R.id.bottom_profile) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        } else return itemId == R.id.bottom_dialog;
        startActivity(intent, options.toBundle());
        return true;
    }
}
