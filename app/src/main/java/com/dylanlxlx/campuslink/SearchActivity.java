package com.dylanlxlx.campuslink;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
        getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class), options.toBundle());
                return true;
            } else if (itemId == R.id.bottom_search) {
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
    }

    
}
