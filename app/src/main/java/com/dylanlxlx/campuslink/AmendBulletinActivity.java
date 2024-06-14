package com.dylanlxlx.campuslink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AmendBulletinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitle;
    private EditText editTextContent;
    private TextView textViewTitleCounter;
    private TextView textViewContentCounter;

    final int titleMaxLength = 16;
    final int contentMaxLength = 200;

    private int id;
    private String oldTitle, oldContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_amend_bulletin);
        findViewById(R.id.btn_withdraw_amend).setOnClickListener(this);
        findViewById(R.id.btn_amend_bulletin).setOnClickListener(this);
        editTextTitle = findViewById(R.id.edit_amend_title);
        editTextContent = findViewById(R.id.edit_amend_content);
        textViewTitleCounter = findViewById(R.id.tv_title_counter_amend);
        textViewContentCounter = findViewById(R.id.tv_content_counter_amend);
        counterSet();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getInt("id");
            oldTitle = bundle.getString("title");
            oldContent = bundle.getString("content");
            editTextTitle.setText(oldTitle);
            editTextContent.setText(oldContent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_withdraw_amend:
                finish();
                break;
            case R.id.btn_amend_bulletin:
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
            bundle.putInt("id", id);
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

        editTextContent.addTextChangedListener(new TextWatcher() {
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
}