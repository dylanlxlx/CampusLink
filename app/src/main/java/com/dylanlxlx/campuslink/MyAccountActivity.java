package com.dylanlxlx.campuslink;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.contract.MyAccountContract;
import com.dylanlxlx.campuslink.presenter.MyAccountPresenter;
import com.dylanlxlx.campuslink.utils.ActivityFactory;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener, MyAccountContract.View {

    private MyAccountPresenter presenter;
    private ImageButton btnBack;
    private RelativeLayout btnChangeNickname, btnSelectGender, btnSetAge, btnChangePhoneNumber, btnChangeEmail, btnSetIntro;
    ;
    private TextView nicknameHint, genderHint, ageHint, phoneNumberHint, emailHint, introHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        presenter = new MyAccountPresenter(this);
        presenter.loadUserData();

        initViews();
        setClickEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadUserData(); // 重新加载用户数据
    }


    public void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnChangeNickname = findViewById(R.id.btn_change_nickname);
        btnSelectGender = findViewById(R.id.btn_select_gender);
        btnSetAge = findViewById(R.id.btn_set_age);
        btnChangePhoneNumber = findViewById(R.id.btn_change_phone_number);
        btnChangeEmail = findViewById(R.id.btn_change_email);
        btnSetIntro = findViewById(R.id.btn_set_intro);

        nicknameHint = findViewById(R.id.btn_change_nickname_hint);
        genderHint = findViewById(R.id.btn_select_gender_hint);
        ageHint = findViewById(R.id.btn_set_age_hint);
        phoneNumberHint = findViewById(R.id.btn_change_phone_number_hint);
        emailHint = findViewById(R.id.btn_change_email_hint);
        introHint = findViewById(R.id.btn_set_intro_hint);
    }

    //设置点击事件
    public void setClickEvent() {
        btnBack.setOnClickListener(v -> finish());
        btnChangeNickname.setOnClickListener(this);
        btnSelectGender.setOnClickListener(this);
        btnSetAge.setOnClickListener(this);
        btnChangePhoneNumber.setOnClickListener(this);
        btnChangeEmail.setOnClickListener(this);
        btnSetIntro.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_change_nickname) {
            ActivityFactory.startActivity(this, ActivityFactory.ActivityType.CHANGE_NICKNAME);
        } else if (id == R.id.btn_select_gender) {
            showGenderDialog();
        } else if (id == R.id.btn_set_age) {
            showAgeDialog();
        } else if (id == R.id.btn_change_phone_number) {
            ActivityFactory.startActivity(this, ActivityFactory.ActivityType.CHANGE_PHONE_NUMBER);
        } else if (id == R.id.btn_change_email) {
            ActivityFactory.startActivity(this, ActivityFactory.ActivityType.CHANGE_EMAIL);
        } else if (id == R.id.btn_set_intro) {
            ActivityFactory.startActivity(this, ActivityFactory.ActivityType.SET_INTRO);
        }
    }

    private void showGenderDialog() {
        final String[] genders = {"male", "female", "none"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender");
        builder.setItems(genders, (dialog, which) -> {
            String selectedGender = genders[which];
            genderHint.setText(selectedGender);
            presenter.updateUserInfo(presenter.getUserId(), "gender", selectedGender);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        // 设置对话框尺寸
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 900; // 对话框宽度，单位为像素
        layoutParams.height = 600; // 对话框高度，单位为像素
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void showAgeDialog() {
        final ArrayList<String> ages = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ages.add(String.valueOf(i));
        }
        final CharSequence[] ageArray = ages.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Age");
        builder.setItems(ageArray, (dialog, which) -> {
            int selectedAge = Integer.parseInt(ageArray[which].toString());
            ageHint.setText(String.valueOf(selectedAge));
            presenter.updateUserInfo(presenter.getUserId(), "age", String.valueOf(selectedAge));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        // 设置对话框尺寸
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 900; // 对话框宽度，单位为像素
        layoutParams.height = 650; // 对话框高度，单位为像素
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void showName(String name) {
        nicknameHint.setText(name);
    }

    @Override
    public void showGender(String gender) {
        genderHint.setText(gender);
    }

    @Override
    public void showAge(int age) {
        ageHint.setText(String.valueOf(age));
    }

    @Override
    public void showPhone(String phone) {
        phoneNumberHint.setText(phone);
    }

    @Override
    public void showEmail(String email) {
        emailHint.setText(email);
    }

    @Override
    public void showRemarks(String remarks) {
        introHint.setText(remarks);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, "errorMessage" + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        Toast.makeText(this, "Update successful！", Toast.LENGTH_SHORT).show();
    }
}