package com.dylanlxlx.campuslink.ui.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.dylanlxlx.campuslink.ui.login.LoginActivity;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ManageUsersActivity extends AppCompatActivity implements ManagerContract.View,UserInputFragment.OnUserInputListener, DeleteUserFragment.OnUserDeleteListener {
    private Button addButton;
    private Button deleteButton;
    private Button queryButton;

    private ManagerPresenter managerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

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

        managerPresenter = new ManagerPresenter(this);

        addButton = findViewById(R.id.managerUsers_addUser);
        deleteButton = findViewById(R.id.managerUsers_deleteUser);
        queryButton = findViewById(R.id.managerUsers_searchUser);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserInputFragment();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteUserFragment();
            }
        });

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryUser();
            }
        });
    }

    @Override
    public void onUserInputSubmit(String username, String password, String name, int role) {
        // Handle the user input, e.g., add to database
        addUser(username, password, name, role);

        // Remove the fragment after receiving the data
        getSupportFragmentManager().popBackStack();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
    }

    @Override
    public void onUserDeleteSubmit(String userId) {
        // Handle the user delete, e.g., remove from database
        deleteUser(userId);

        // Remove the fragment after receiving the data
        getSupportFragmentManager().popBackStack();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
    }

    private void addUser(String username, String password, String name, int newRole) {
        // Add user logic here, e.g., save to database
        Toast.makeText(this, "User added: " + username + password + name + newRole, Toast.LENGTH_SHORT).show();
        managerPresenter.addUsers(username, password, name, newRole);
    }

    private void deleteUser(String userId) {
        // Delete user logic here, e.g., remove from database
        Toast.makeText(this, "User deleted: " + userId, Toast.LENGTH_SHORT).show();
        managerPresenter.deleteUsers(Integer.parseInt(userId));
    }

    private void queryUser() {

    }

    private void showUserInputFragment() {
        UserInputFragment userInputFragment = new UserInputFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, userInputFragment)
                .addToBackStack(null)
                .commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
    }

    private void showDeleteUserFragment() {
        DeleteUserFragment deleteUserFragment = new DeleteUserFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, deleteUserFragment)
                .addToBackStack(null)
                .commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
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
