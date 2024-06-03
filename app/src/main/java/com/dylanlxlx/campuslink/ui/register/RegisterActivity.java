package com.dylanlxlx.campuslink.ui.register;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRegister.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString();

            if (isValidRegistration(username, password, confirmPassword)) {
                // 模拟注册成功
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                finish(); // 回到登录界面
            } else {
                Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidRegistration(String username, String password, String confirmPassword) {
        // 这里应该有更复杂的验证逻辑
        return !username.isEmpty() && password.equals(confirmPassword);
    }
}
