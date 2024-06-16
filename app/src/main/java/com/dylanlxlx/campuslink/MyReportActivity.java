package com.dylanlxlx.campuslink;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dylanlxlx.campuslink.adapter.ComplaintItem;
import com.dylanlxlx.campuslink.adapter.ManagerUserAdapter;
import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.adapter.ReportAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyReportActivity extends AppCompatActivity implements ManagerContract.View {
    private ManagerPresenter presenter;
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<ComplaintItem> complaints;
    private ImageButton btnBack;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int userId = -1;
    private int userRole = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);

        presenter = new ManagerPresenter(this);
        recyclerView = findViewById(R.id.recyclerView_my_report);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btn_back_report);
        btnBack.setOnClickListener(v -> finish());
        // 设置适配器
        complaints = new ArrayList<>();
        adapter = new ReportAdapter(complaints);
        recyclerView.setAdapter(adapter);
        fitchComplaintData();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int id = complaints.get(position).getId();
                if (direction == ItemTouchHelper.LEFT) {
                    if(!Objects.equals(complaints.get(position).getState(), "撤销")){
                        presenter.withdrawReport(id);
                        complaints.get(position).setState("撤销");
                    }
                    //adapter.removeItem(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle left swipe event here
                    if(userRole == 2){
                        Intent intent = new Intent(MyReportActivity.this, HandleReportActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }
                adapter.notifyItemChanged(position);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void fitchComplaintData() {
        userId = presenter.getUserId();
        userRole = presenter.getRole();
        Log.d("submitReport", "submitReport: " + userId + "//"+userRole);
        if (userId == -1 || userRole == -1) {
            showError("数据异常");
            Log.d("submitReport", "submitReport: " + userId + "//");
            return;
        }
        presenter.searchReport(userId, userRole);
    }

    private void prepareComplaintData(String jsonResponse) {
        Gson gson = new Gson();

        // 使用 Gson 将 JSON 字符串解析为 JsonObject
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        String datasuccess = jsonObject.get("success").toString();
        Log.d("searchReport", "onResponse: "+datasuccess);
        if (!datasuccess.equals("true")) {
            showError("数据异常");
            return;
        }
        Type listType = new TypeToken<List<ComplaintItem>>() {}.getType();
        List<ComplaintItem> complaintList = gson.fromJson(dataArray, listType);

        Log.d("searchReport", "onResponse: "+complaintList);
        complaints.clear();
        complaints.addAll(complaintList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        if(!Objects.equals(successMessage, "withdrawReport")){
            prepareComplaintData(successMessage);
        }
        if(Objects.equals(successMessage, "withdrawReport")){
            Toast.makeText(getApplicationContext(), "成功撤销", Toast.LENGTH_SHORT).show();
        }
        Log.d("searchReport", "onResponse: "+successMessage);
    }
}
