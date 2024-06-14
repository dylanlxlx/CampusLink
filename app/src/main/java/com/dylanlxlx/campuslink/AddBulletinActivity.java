package com.dylanlxlx.campuslink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddBulletinActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextTitle;
    private EditText editTextContent;
    private TextView textViewTitleCounter;
    private TextView textViewContentCounter;

    final int titleMaxLength = 50;
    final int contentMaxLength = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bulletin);
        findViewById(R.id.btn_withdraw_edit).setOnClickListener(this);
        findViewById(R.id.btn_bulletin_submit).setOnClickListener(this);
        editTextTitle = findViewById(R.id.edit_bulletin_title);
        editTextContent = findViewById(R.id.edit_bulletin_content);
        textViewTitleCounter = findViewById(R.id.tv_title_counter);
        textViewContentCounter = findViewById(R.id.tv_content_counter);
        counterSet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_withdraw_edit:
                finish();
                break;
            case R.id.btn_bulletin_submit:
                submitBulletin();
                break;
        }
    }

    public void submitBulletin() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        if (title.isEmpty()) {
            editTextTitle.requestFocus();
            editTextTitle.setError("Title cannot be empty");
        } else if (content.isEmpty()) {
            editTextContent.requestFocus();
            editTextContent.setError("Content cannot be empty");
        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public void counterSet() {
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                textViewTitleCounter.setText(currentLength + "/" + titleMaxLength);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                textViewContentCounter.setText(currentLength + "/" + contentMaxLength);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}