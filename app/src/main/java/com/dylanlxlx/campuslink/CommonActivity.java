package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.MyAccountContract;
import com.dylanlxlx.campuslink.presenter.MyAccountPresenter;
import com.dylanlxlx.campuslink.utils.ActivityFactory;

public class CommonActivity extends AppCompatActivity implements MyAccountContract.View {
    private EditText editText;
    private TextView btnCancle, btnConfirm;
    private MyAccountPresenter presenter;
    private ActivityFactory.ActivityType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        editText = findViewById(R.id.editText);
        btnCancle = findViewById(R.id.btn_cancle);
        btnConfirm = findViewById(R.id.btn_confirm);

        presenter = new MyAccountPresenter(this);
        presenter.loadUserData();

        type = (ActivityFactory.ActivityType) getIntent().getSerializableExtra(ActivityFactory.EXTRA_TYPE);
        if (type != null) {
            switch (type) {
                case CHANGE_NICKNAME:
                    editText.setHint("Enter new nickname");
                    break;
                case CHANGE_PHONE_NUMBER:
                    editText.setHint("Enter new phone number");
                    break;
                case CHANGE_EMAIL:
                    editText.setHint("Enter new email");
                    break;
                case SET_INTRO:
                    editText.setHint("Enter new intro");
                    break;
            }
        }

        btnCancle.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            String input = editText.getText().toString().trim();
            if (!input.isEmpty()) {
                updateUserInfo(input);
            }
        });
    }

    private void updateUserInfo(String input) {
        String field = "";
        switch (type) {
            case CHANGE_NICKNAME:
                field = "name";
                break;
            case CHANGE_PHONE_NUMBER:
                field = "phone";
                break;
            case CHANGE_EMAIL:
                field = "mail";
                break;
            case SET_INTRO:
                field = "remarks";
                break;
        }
        Log.e("CommonActivity", "UserId: " + presenter.getUserId());
        presenter.updateUserInfo(presenter.getUserId(), field, input);
    }

    @Override
    public void showError(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showSuccess(String successMessage) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Info Update Successful！", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void showName(String name) {

    }

    @Override
    public void showGender(String gender) {

    }

    @Override
    public void showAge(int age) {

    }

    @Override
    public void showPhone(String phone) {

    }

    @Override
    public void showEmail(String email) {

    }

    @Override
    public void showRemarks(String remarks) {

    }
}
