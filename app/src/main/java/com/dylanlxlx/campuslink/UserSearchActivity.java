package com.dylanlxlx.campuslink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.ManagerUserAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.data.User;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity implements ManagerContract.View {
    private List<User> userList;
    private RecyclerView recyclerView;

    private ManagerUserAdapter userAdapter;
    private ManagerPresenter presenter;
    private EditText editText;
    private LinearLayout searchFail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_search);
        presenter = new ManagerPresenter(this);
        recyclerView = findViewById(R.id.recycler_view_user_list);
        searchFail = findViewById(R.id.ll_no_data);
        userList = new ArrayList<>();

        editText = findViewById(R.id.edit_search_user);
        setEditListener();
        findViewById(R.id.btn_cancel_search).setOnClickListener(v -> {
            finish();
        });

    }

    private void setEditListener() {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        String message = editText.getText().toString().trim();
        searchUser(message);
        editText.setText("");
        hideKeyboard();
    }

    private void hideKeyboard() {
        // 获取InputMethodManager服务
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 找到当前聚焦的视图
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void searchUser(String searchContent) {
        userList.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        JSONObject strings = presenter.queryUsers(searchContent);
        Gson gson = new Gson();
        // 将 JSONObject 转换为 JSON 字符串
        String jsonString = strings.toString();
        // 使用 Gson 将 JSON 字符串解析为 JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if (dataArray.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            searchFail.setVisibility(View.GONE);
            for (int i = 0; i < dataArray.size(); i++) {
                JsonObject userObject = dataArray.get(i).getAsJsonObject();
                int id, age, role;
                String username, name, phone, avatar, remarks, createTime, updateTime;
                double money;
                id = userObject.get("id").getAsInt();
                username = userObject.get("username").getAsString();
                try {
                    name = userObject.get("name").getAsString();
                } catch (Exception e) {
                    name = null;
                }
                try {
                    phone = userObject.get("phone").getAsString();
                } catch (Exception e) {
                    phone = null;
                }
                money = userObject.get("money").getAsDouble();
                age = userObject.get("age").getAsInt();
                try {
                    avatar = userObject.get("avatar").getAsString();
                } catch (Exception e) {
                    avatar = null;
                }
                try {
                    remarks = userObject.get("remarks").getAsString();
                } catch (Exception e) {
                    remarks = null;
                }
                role = userObject.get("role").getAsInt();
                createTime = userObject.get("createTime").getAsString();
                updateTime = userObject.get("updateTime").getAsString();
                User user = new User(id, username, name, phone, money, age, avatar, remarks, role, createTime, updateTime);
                userList.add(user);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            searchFail.setVisibility(View.VISIBLE);
        }
        userAdapter = new ManagerUserAdapter(userList, position -> {
            int targetId = userList.get(position).getId();
            int userId = presenter.getUserId();
            if (targetId != userId) {
                Intent intent = new Intent(this, DialogDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("targetId", userList.get(position).getId());
                bundle.putInt("userId", presenter.getUserId());
                bundle.putString("name", userList.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(userAdapter);
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