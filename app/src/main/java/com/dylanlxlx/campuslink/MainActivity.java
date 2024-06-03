package com.dylanlxlx.campuslink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.TransitionInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dylanlxlx.campuslink.adapter.ImageSliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    List<Integer> images;

    private final EditText[] titleEditTexts = new EditText[4];
    private final EditText[] descriptionEditTexts = new EditText[4];
    private final Button[] saveButtons = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = Arrays.asList(R.drawable.jf1, R.drawable.jf3, R.drawable.jf3); // 替换为实际的图片URL
        titleEditTexts[0] = findViewById(R.id.titleEditText1);
        titleEditTexts[1] = findViewById(R.id.titleEditText2);
        titleEditTexts[2] = findViewById(R.id.titleEditText3);
        titleEditTexts[3] = findViewById(R.id.titleEditText4);

        descriptionEditTexts[0] = findViewById(R.id.descriptionEditText1);
        descriptionEditTexts[1] = findViewById(R.id.descriptionEditText2);
        descriptionEditTexts[2] = findViewById(R.id.descriptionEditText3);
        descriptionEditTexts[3] = findViewById(R.id.descriptionEditText4);

        saveButtons[0] = findViewById(R.id.saveButton1);
        saveButtons[1] = findViewById(R.id.saveButton2);
        saveButtons[2] = findViewById(R.id.saveButton3);
        saveButtons[3] = findViewById(R.id.saveButton4);
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        ImageSliderAdapter adapter = new ImageSliderAdapter(images);
        viewPager2.setAdapter(adapter);

        // 设置初始位置为中间，以便两边都可以滑动
        viewPager2.setCurrentItem(Integer.MAX_VALUE / 2);

        // 可以添加定时器，使ViewPager自动滑动
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
        getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class), options.toBundle());
                return true;
            } else if (itemId == R.id.bottom_settings) {
                startActivity(new Intent(getApplicationContext(), SettingsActiviy.class), options.toBundle());
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class), options.toBundle());
                return true;
            }
            return false;
        });
        for (int i = 0; i < saveButtons.length; i++) {
            int finalI = i;
            saveButtons[i].setOnClickListener(v -> saveCard(finalI));
        }
    }

    private void saveCard(int index) {
        String title = titleEditTexts[index].getText().toString().trim();
        String description = descriptionEditTexts[index].getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
        } else if (!isValidInput(title, description)) {
            Toast.makeText(this, "输入包含非法字符", Toast.LENGTH_SHORT).show();
        } else {
            // Save card logic here (e.g., save to database or send to server)
            Toast.makeText(this, "卡片已保存", Toast.LENGTH_SHORT).show();
            titleEditTexts[index].setText("");
            descriptionEditTexts[index].setText("");
        }
    }

    private boolean isValidInput(String title, String description) {
        // 添加输入验证逻辑，例如禁止某些特殊字符
        String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\s]+$";
        return title.matches(regex) && description.matches(regex);
    }
}