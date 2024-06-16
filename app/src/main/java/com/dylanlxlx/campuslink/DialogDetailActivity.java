package com.dylanlxlx.campuslink;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.dylanlxlx.campuslink.utils.TimeHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogDetailActivity extends AppCompatActivity implements View.OnClickListener, ManagerContract.View {
    private int targetId;
    private int userId;
    private String name;

    private List<DetailDialog> detailDialogList;
    private RecyclerView recyclerView;
    private ManagerPresenter presenter;
    private DialogDetailAdapter dialogDetailAdapter;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable checkNewMessagesRunnable;
    private EditText dialogInput;
    private LinearLayout rootLayout;

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
        findViewById(R.id.btn_send_message).setOnClickListener(this);
        dialogInput = findViewById(R.id.et_message);
        rootLayout = findViewById(R.id.rootLayout);

        TextView dialogName = findViewById(R.id.tv_dialog_name);
        dialogName.setText(name);

        recyclerView = findViewById(R.id.rev_dialog);
        presenter = new ManagerPresenter(this);
        detailDialogList = new ArrayList<>();
        try {
            refreshDialog();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        startCheckNewMessages();
        setKeyBoard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_dialog:
                finish();
                break;
            case R.id.btn_send_message:
                sendDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkNewMessagesRunnable);
    }

    public void sendDialog() {
        String content = dialogInput.getText().toString();
        if (!content.isEmpty()) {
            try {
                presenter.newDialog(userId, targetId, content, "text");
                dialogInput.setText("");
                hideKeyboard();
                refreshDialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private void setKeyBoard() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootLayout.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootLayout.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) { // 键盘弹出
                adjustLayoutForKeyboard(keypadHeight);
            } else { // 键盘收起
                adjustLayoutForKeyboard(0);
            }
        });
    }

    private void adjustLayoutForKeyboard(int keypadHeight) {
        // 在这里调整布局，根据键盘高度调整输入框的位置
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialogInput.getLayoutParams();
        params.bottomMargin = keypadHeight;
        dialogInput.setLayoutParams(params);
    }

    private void startCheckNewMessages() {
        checkNewMessagesRunnable = new Runnable() {
            @Override
            public void run() {
                if (checkNewMessage()) {
                    try {
                        refreshDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(checkNewMessagesRunnable);
    }

    private Boolean checkNewMessage() {
        try {
            JSONObject jsonObject = presenter.queryDialog(targetId, userId);
            JSONArray jsonArray = JsonDeal.reverseJSONArray(jsonObject.getJSONArray("data"));
            return jsonArray.length() != detailDialogList.size();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void refreshDialog() throws JSONException {
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
                if (TimeHandle.isDifferenceGreaterThanFiveMinutes(detailDialogList.get(position).getTime())) {
                    try {
                        presenter.withdrawDialog(detailDialogList.get(position).getId());
                        refreshDialog();
                        Toast.makeText(this, "撤回了一条消息", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "只能撤回5分钟内的消息", Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(dialogDetailAdapter);
            recyclerView.scrollToPosition(0);

        } catch (JSONException ignored) {

        }
    }

    @Override
    public void showError(String errorMessage) {
//        Toast.makeText(this, "errorMessage: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
//        Toast.makeText(this, "successMessage: " + successMessage, Toast.LENGTH_SHORT).show();
    }
}