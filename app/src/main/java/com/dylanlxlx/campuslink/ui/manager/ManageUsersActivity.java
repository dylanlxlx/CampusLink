package com.dylanlxlx.campuslink.ui.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.BulletinActivity;
import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.adapter.ManagerUserAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.data.User;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.dylanlxlx.campuslink.ui.login.LoginActivity;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements ManagerContract.View, UserInputFragment.OnUserInputListener, DeleteUserFragment.OnUserDeleteListener, SearchUserFragment.OnUserSearchListener {
    private Button addButton;
    private Button deleteButton;
    private Button queryButton;
    private RecyclerView recyclerView;
    private ManagerUserAdapter userAdapter;
    private List<User> userList;

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

        recyclerView = findViewById(R.id.recyclerView);
        userList = new ArrayList<>();
        userAdapter = new ManagerUserAdapter(userList, position -> {
            Toast.makeText(this, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

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
                showSearchUserFragment();
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
        deleteUser(Integer.parseInt(userId));

        // Remove the fragment after receiving the data
        getSupportFragmentManager().popBackStack();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
    }

    @Override
    public void onUserSearchSubmit(String name) {
        // Handle the user search, e.g., query from database
        queryUser(name);

        // Remove the fragment after receiving the data
        getSupportFragmentManager().popBackStack();
        findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
    }


    private void addUser(String username, String password, String name, int newRole) {
        // Add user logic here, e.g., save to database
        Toast.makeText(this, "User added: " + username + password + name + newRole, Toast.LENGTH_SHORT).show();
        managerPresenter.addUsers(username, password, name, newRole);
    }

    private void deleteUser(int userId) {
        // Delete user logic here, e.g., remove from database
        Toast.makeText(this, "User deleted: " + userId, Toast.LENGTH_SHORT).show();
        managerPresenter.deleteUsers(userId);
    }

    private void queryUser(String name) {
        // Query user logic here, e.g., query from database
        Toast.makeText(this, "User queried: " + name, Toast.LENGTH_SHORT).show();
        JSONObject strings = managerPresenter.queryUsers(name);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<User>>() {
        }.getType();
        // 将 JSONObject 转换为 JSON 字符串
        String jsonString = strings.toString();
        // 使用 Gson 将 JSON 字符串解析为 JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        List<User> users = gson.fromJson(dataArray, userListType);

        userList.clear();
        userList.addAll(users);
        userAdapter.notifyDataSetChanged();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int id = userList.get(position).getId();
                if (direction == ItemTouchHelper.LEFT) {
                    userAdapter.removeItem(position);
                    deleteUser(id);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle left swipe event here
                }
                userAdapter.notifyItemChanged(position);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Log.d("ManageUseracitity", "queryUser: " + strings);
    }

    private void showUserInputFragment() {
        UserInputFragment userInputFragment = new UserInputFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, userInputFragment).addToBackStack(null).commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
    }

    private void showDeleteUserFragment() {
        DeleteUserFragment deleteUserFragment = new DeleteUserFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, deleteUserFragment).addToBackStack(null).commit();
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
    }

    private void showSearchUserFragment() {
        SearchUserFragment searchUserFragment = new SearchUserFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, searchUserFragment).addToBackStack(null).commit();
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
