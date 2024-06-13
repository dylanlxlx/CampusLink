package com.dylanlxlx.campuslink;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.dylanlxlx.campuslink.ui.login.LoginActivity;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class BulletinActivity extends AppCompatActivity implements ManagerContract.View, View.OnClickListener {
    private ManagerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        try {
            UserPreferenceManager userPreferenceManager = UserPreferenceManager.getInstance(this);
            if (UserPreferenceManager.getInstance(null).getUserId() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        presenter = new ManagerPresenter(this);
        presenter.loadUserData();
        findViewById(R.id.add_bulletin).setOnClickListener(this);
        findViewById(R.id.del_bulletin).setOnClickListener(this);
        findViewById(R.id.amend_bulletin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bulletin:
                Toast.makeText(this, "add_bulletin", Toast.LENGTH_SHORT).show();
                presenter.addBulletin("title", "content", presenter.getUserId(), presenter.getRole());
                break;
            case R.id.del_bulletin:
                Toast.makeText(this, "del_bulletin", Toast.LENGTH_SHORT).show();
                presenter.deleteBulletin(4, presenter.getRole());
                break;
            case R.id.amend_bulletin:
                Toast.makeText(this, "amend_bulletin", Toast.LENGTH_SHORT).show();
//                presenter.amendBulletin(4, "title", "content", presenter.getRole());
                JSONObject object = presenter.queryUsers("胡");
                Log.d("TAG", "onClick: " + object);
                break;
        }
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