package com.dylanlxlx.campuslink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreActivity extends AppCompatActivity {

    private Button btnFirstFragment, btnSecondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        btnFirstFragment = findViewById(R.id.btnFirstFragment);
        btnSecondFragment = findViewById(R.id.btnSecondFragment);

        initFragment(savedInstanceState);

        // 初始化底部导航视图
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_explore);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            return handleNavigationItemSelected(itemId, options);
        });
    }

    private void initFragment(Bundle savedInstanceState) {
        btnFirstFragment.setOnClickListener(v -> loadFragment(new ProductFragment()));
        btnSecondFragment.setOnClickListener(v -> loadFragment(new MyFragment()));
        if (savedInstanceState == null) {
            loadFragment(new ProductFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        } else if (itemId == R.id.bottom_dialog) {
            intent = new Intent(getApplicationContext(), DialogActivity.class);
        } else return itemId == R.id.bottom_explore;
        startActivity(intent, options.toBundle());
        return true;
    }
}
