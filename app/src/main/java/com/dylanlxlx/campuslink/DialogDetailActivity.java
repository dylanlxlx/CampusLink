package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dylanlxlx.campuslink.adapter.DialogDetailAdapter;
import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.data.model.DetailDialog;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;
import com.dylanlxlx.campuslink.string.DefaultString;
import com.dylanlxlx.campuslink.utils.JsonDeal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DialogDetailActivity extends AppCompatActivity implements View.OnClickListener, ManagerContract.View {
    private int targetId;
    private int userId;
    private String name;

    private List<DetailDialog> detailDialogList;
    private RecyclerView recyclerView;
    private ManagerPresenter presenter;
    private DialogDetailAdapter dialogDetailAdapter;
    private ScheduledExecutorService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dialog_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            targetId = bundle.getInt("targetId");
            userId = bundle.getInt("userId");
            name = bundle.getString("name");
        }
        findViewById(R.id.btn_back_dialog).setOnClickListener(this);
        recyclerView = findViewById(R.id.rev_dialog);
        TextView dialogName = findViewById(R.id.tv_dialog_name);
        dialogName.setText(name);
        presenter = new ManagerPresenter(this);
        detailDialogList = new ArrayList<>();
        try {
            refreshDialog();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (checkNewMessage()) {
                    try {
                        refreshDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_dialog:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (service != null && !service.isShutdown()) {
            service.shutdown();
        }
    }

    public Boolean checkNewMessage() {
        try {
            JSONObject jsonObject = presenter.queryDialog(targetId, userId);
            JSONArray jsonArray = JsonDeal.reverseJSONArray(jsonObject.getJSONArray("data"));
            return jsonArray.length() != detailDialogList.size();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void refreshDialog() throws JSONException {
        detailDialogList.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        JSONObject jsonObject = presenter.queryDialog(targetId, userId);
        Log.d("TAG", "refreshDialog: " + jsonObject.toString());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Log.d("TAG", "refreshDialog: " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Log.d("TAG", "refreshDialog: " + object.toString());
                int id = object.getInt("id");
                String content = object.getString("content");
                String time = object.getString("sendTime");
                int sendId = object.getInt("sendId");
                boolean isLeft = sendId != userId;
                String avatar;
                JSONObject person = presenter.queryUser(sendId).getJSONObject("data");
                try {
                    avatar = person.getString("avatar");
                } catch (JSONException e) {
                    avatar = new DefaultString().getDefaultAvatar();
                }
                detailDialogList.add(new DetailDialog(id, content, time, isLeft, avatar));
                Log.d("TAG", "refreshDialog: " + detailDialogList.get(i).getId() + " " + detailDialogList.get(i).getContent() + " " + detailDialogList.get(i).getTime() + " " + detailDialogList.get(i).getIsLeft() + " " + detailDialogList.get(i).getAvatar());
            }
            dialogDetailAdapter = new DialogDetailAdapter(detailDialogList, position -> {
                Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();
            });
            recyclerView.setAdapter(dialogDetailAdapter);
            recyclerView.scrollToPosition(0);

        } catch (JSONException ignored) {

        }
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