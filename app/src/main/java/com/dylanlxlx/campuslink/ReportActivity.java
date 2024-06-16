package com.dylanlxlx.campuslink;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.data.LoginDataSource;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ReportActivity extends AppCompatActivity implements ManagerContract.View {

    private ManagerPresenter presenter;
    private EditText reportTitleEditText;
    private EditText reportDescriptionEditText;
    private TextView textViewTitleCounter;
    private TextView textViewContentCounter;
    private Button submitReportButton;
    private Button withdrawButton;
    private int goodsId;
    private int userId = -1;

    final int titleMaxLength = 16;
    final int contentMaxLength = 200;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportTitleEditText = findViewById(R.id.edit_report_title);
        reportDescriptionEditText = findViewById(R.id.edit_report_content);
        submitReportButton = findViewById(R.id.btn_report_submit);
        withdrawButton = findViewById(R.id.btn_withdraw_report);
        textViewTitleCounter = findViewById(R.id.tv_title_counter_report);
        textViewContentCounter = findViewById(R.id.tv_content_counter_report);
        withdrawButton.setOnClickListener(v -> finish());
        submitReportButton.setOnClickListener(v -> submitReport());
        goodsId = getIntent().getIntExtra("id", -1);
        presenter = new ManagerPresenter(this);
        counterSet();
    }

    private void submitReport() {
        String title = reportTitleEditText.getText().toString();
        String description = reportDescriptionEditText.getText().toString();
        String content = title + "/" + description;
        userId = presenter.getUserId();
        if (userId == -1 || goodsId == -1) {
            showError("数据异常");
            Log.d("submitReport", "submitReport: " + userId + "/" + goodsId + "/" + content + "/");
            return;
        }
        Log.d("submitReport", "submitReport: " + userId + "/" + goodsId + "/" + content + "/");
        presenter.submitReport("goods", userId, goodsId, content);
    }

    public void counterSet() {
        reportTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                textViewTitleCounter.setText(currentLength + "/" + titleMaxLength);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        reportDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                textViewContentCounter.setText(currentLength + "/" + contentMaxLength);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
    }
}
