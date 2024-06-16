package com.dylanlxlx.campuslink;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.ManagerContract;
import com.dylanlxlx.campuslink.presenter.ManagerPresenter;

public class HandleReportActivity extends AppCompatActivity implements ManagerContract.View {
    private ManagerPresenter presenter;
    private EditText handleTitleEditText;
    private EditText handleDescriptionEditText;
    private TextView textViewTitleCounter;
    private TextView textViewContentCounter;
    private Button submitHandleButton;
    private Button withdrawButton;
    private int otherId;
    private int ownId = -1;

    final int titleMaxLength = 16;
    final int contentMaxLength = 200;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_report);

        presenter = new ManagerPresenter(this);
        handleTitleEditText = findViewById(R.id.edit_handle_title);
        handleDescriptionEditText = findViewById(R.id.edit_handle_content);
        submitHandleButton = findViewById(R.id.btn_handle_submit);
        withdrawButton = findViewById(R.id.btn_withdraw_handle);
        textViewTitleCounter = findViewById(R.id.tv_title_counter_handle);
        textViewContentCounter = findViewById(R.id.tv_content_counter_handle);
        withdrawButton.setOnClickListener(v -> finish());
        submitHandleButton.setOnClickListener(v -> handleReport());
        otherId = getIntent().getIntExtra("id", -1);
        counterSet();
    }

    private void handleReport() {
        String title = handleTitleEditText.getText().toString();
        String description = handleDescriptionEditText.getText().toString();
        String content = title + "/" + description;
        ownId = presenter.getUserId();
        if(ownId == -1 || otherId == -1){
            showError("数据异常");
            return;
        }
        presenter.manageReport(ownId, otherId, content);
    }

    public void counterSet() {
        handleTitleEditText.addTextChangedListener(new TextWatcher() {
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

        handleDescriptionEditText.addTextChangedListener(new TextWatcher() {
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
