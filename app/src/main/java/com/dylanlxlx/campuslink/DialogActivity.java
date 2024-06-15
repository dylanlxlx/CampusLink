package com.dylanlxlx.campuslink;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.DialogAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.data.model.Dialog;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.dylanlxlx.campuslink.ui.manager.ManageUsersActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener, ManagerContract.View {
    private ManagerPresenter presenter;
    private RecyclerView recyclerView;

    private List<Dialog> dialogList;

    private DialogAdapter dialogAdapter;
    private LinearLayout nullDialog;

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
        recyclerView = findViewById(R.id.dialog_recycler_view);
        nullDialog = findViewById(R.id.null_dialog);
        presenter = new ManagerPresenter(this);
        presenter.loadUserData();
        dialogList = new ArrayList<>();

        try {
            refreshDialogList();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.dialog_button_clear_unread:
                Toast.makeText(this, "dialog_button_clear_unread", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_setting:
                intent = new Intent(this, ManageUsersActivity.class);
                startActivity(intent);
                Toast.makeText(this, "dialog_setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_search_button:
                Toast.makeText(this, "dialog_search_button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_bulletin:
                intent = new Intent(this, BulletinActivity.class);
                startActivity(intent);
                break;
            case R.id.dialog_likes:
                Toast.makeText(this, "dialog_likes", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void refreshDialogList() throws JSONException {
        dialogList.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        JSONObject chatList = presenter.getChatList();
        JSONArray dataList = filterData(chatList.getJSONArray("data"));
        Log.d("TAG", "refreshDialogList: " + dataList);
        if (dataList.length() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            nullDialog.setVisibility(View.GONE);
            for (int i = 0; i < dataList.length(); i++) {
                if (dataList.get(i) == null) {
                    continue;
                }
                JSONObject person = dataList.getJSONObject(i);
                Log.d("TAG", "refreshDialogList: " + person);
                int id = person.getInt("id");
                String avatar = person.getString("avatar");
                String name = person.getString("name");
                String lastMessage, time;
                JSONArray messages = presenter.queryDialog(presenter.getUserId(), id).getJSONArray("data");
                Log.d("TAG", "refreshDialogList: " + presenter.getUserId() + " " + id + " " + messages);
                if (messages.length() != 0) {
                    JSONObject message = messages.getJSONObject(0);
                    lastMessage = message.getString("content");
                    time = message.getString("sendTime");
                } else {
                    lastMessage = "没有聊天记录";
                    time = "10:00";
                }
                dialogList.add(new Dialog(avatar, name, lastMessage, time, id));
                Log.d("TAG", "refreshDialogList: " + dialogList.get(i).getId());
            }
            dialogAdapter = new DialogAdapter(dialogList, position -> {
                Toast.makeText(DialogActivity.this, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
            });
            recyclerView.setAdapter(dialogAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    int id = dialogList.get(position).getId();
                    if (direction == ItemTouchHelper.LEFT) {
                        dialogAdapter.removeItem(position);
                    }
                    dialogAdapter.notifyItemChanged(position);
                }

                @Override
                public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                    return 0.7f;
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } else {
            recyclerView.setVisibility(View.GONE);
            nullDialog.setVisibility(View.VISIBLE);
        }
    }

    public JSONArray filterData(JSONArray data) {
        JSONArray dataList = new JSONArray();
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject item = data.getJSONObject(i);
                if (item != null) {
                    dataList.put(item);
                }
            } catch (JSONException e) {
            }
        }
        return dataList;
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
        } else if (itemId == R.id.bottom_explore) {
            intent = new Intent(getApplicationContext(), ExploreActivity.class);
        } else if (itemId == R.id.bottom_profile) {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
        } else return itemId == R.id.bottom_dialog;
        startActivity(intent, options.toBundle());
        return true;
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
