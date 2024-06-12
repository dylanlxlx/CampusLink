package com.dylanlxlx.campuslink;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.ManagerContract;

import java.util.Objects;

public class BulletinActivity extends AppCompatActivity implements ManagerContract.View {
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulletin);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        role = Objects.requireNonNull(bundle).getString("role");
    }

    @Override
    public void showManagerView() {

    }
}