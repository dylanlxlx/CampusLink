package com.dylanlxlx.campuslink.ui.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.databinding.ActivityRegisterBinding;
import com.dylanlxlx.campuslink.ui.login.LoginViewModelFactory;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText usernameEditText = binding.editTextUsername;
        final EditText emailEditText = binding.editTextEmail;
        final EditText codeEditText = binding.editverificationCode;
        final EditText passwordEditText = binding.editTextPassword;
        final EditText password2EditText = binding.editTextConfirmPassword;
        final Button codeButton = binding.buttonVerifiedCode;
        final Button registerButton = binding.buttonRegister;

        //表单状态对应的监听器
        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getemailError() != null) {
                    emailEditText.setError(getString(registerFormState.getemailError()));
                }
                if (registerFormState.getcodeError() != null) {
                    codeEditText.setError(getString(registerFormState.getcodeError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getPassword2Error() != null) {
                    password2EditText.setError(getString(registerFormState.getPassword2Error()));
                }
            }
        });

        //文字变化对应的监听器
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        codeEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        password2EditText.getText().toString());
            }
        };

        //设置监听器
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        codeEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.addTextChangedListener(afterTextChangedListener);

        binding.buttonRegister.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String code = binding.editverificationCode.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString();

            if (isValidRegistration(username, email, code, password, confirmPassword)) {
                // 模拟注册成功
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                finish(); // 回到登录界面
            } else {
                Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidRegistration(String username, String email, String code, String password, String confirmPassword) {
        // 这里应该有更复杂的验证逻辑
        return !username.isEmpty() && password.equals(confirmPassword);
    }
}
