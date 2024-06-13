package com.dylanlxlx.campuslink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;

import java.util.Objects;

public class BulletinActivity extends AppCompatActivity implements ManagerContract.View {
    String role;
    private ManagerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulletin);
        presenter = new ManagerPresenter(this);
        presenter.loadUserData();
    }

    @Override
    public void showManagerView() {

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, "errorMessage: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        Toast.makeText(this, "successMessage: " + successMessage, Toast.LENGTH_SHORT).show();
    }
}