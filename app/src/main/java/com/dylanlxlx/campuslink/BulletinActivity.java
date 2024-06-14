package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.RecordAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.google.gson.Gson;
import com.dylanlxlx.campuslink.data.model.Record;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BulletinActivity extends AppCompatActivity implements ManagerContract.View, View.OnClickListener {
    private ManagerPresenter presenter;

    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;

    private LinearLayout nullBulletin;

    private List<Record> records;

    JSONObject result;

    private int role;
    private Button newBulletin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);
        recyclerView = findViewById(R.id.bulletin_list);
        newBulletin = findViewById(R.id.new_bulletin);
        newBulletin.setOnClickListener(this);
        findViewById(R.id.bt_refresh_bulletin).setOnClickListener(this);
        nullBulletin = findViewById(R.id.null_bulletin);
        presenter = new ManagerPresenter(this);
        presenter.loadUserData();
        refreshList();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_refresh_bulletin:
                refreshList();
                break;
        }
    }

    public void refreshList() {
        result = presenter.queryBulletin(1, 100);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            JSONObject dataObject = result.getJSONObject("data");
            JSONArray recordsArray = dataObject.getJSONArray("records");

            if (recordsArray.length() != 0) {
                nullBulletin.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                // 使用 Gson 解析 records 数组
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Record>>() {
                }.getType();
                records = gson.fromJson(recordsArray.toString(), listType);
                // 设置适配器
                recordAdapter = new RecordAdapter(records, position -> {
                    Toast.makeText(BulletinActivity.this, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
                });
                recyclerView.setAdapter(recordAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        int id = records.get(position).getId();
                        if (direction == ItemTouchHelper.LEFT) {
                            if (role == 2) {
                                recordAdapter.removeItem(position);
                                presenter.deleteBulletin(id, role);
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                refreshList();
                            }
                        } else if (direction == ItemTouchHelper.RIGHT) {
                            if (role == 2) {

                            }
                            // Handle left swipe event here
                        }
                        recordAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return 0.7f;
                    }
                });
                itemTouchHelper.attachToRecyclerView(recyclerView);
            } else {
                recyclerView.setVisibility(View.GONE);
                nullBulletin.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        role = presenter.getRole();
        if (role == 2) {
            newBulletin.setVisibility(View.VISIBLE);
            newBulletin.setEnabled(true);
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