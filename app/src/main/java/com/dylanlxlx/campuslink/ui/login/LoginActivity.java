package com.dylanlxlx.campuslink.ui.login;

import static java.security.AccessController.getContext;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dylanlxlx.campuslink.MainActivity;
import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.databinding.ActivityLoginBinding;
import com.dylanlxlx.campuslink.ui.forgetPassword.ForgetActivity;
import com.dylanlxlx.campuslink.ui.register.RegisterActivity;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final Button forgetButton = binding.forget;
        final ProgressBar loadingProgressBar = binding.loading;

        //表单状态对应的监听器
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });
        //登录结果对应的监听器
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                finish();
            }
            setResult(Activity.RESULT_OK);
            //Complete and destroy login activity once successful

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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        //设置监听器
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        //设置键盘监听器
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            //显示加载进度条
            loadingProgressBar.setVisibility(View.VISIBLE);
            //登录
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
        assert registerButton != null;
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        assert forgetButton != null;
        forgetButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {

        String welcome = getString(R.string.welcome) + model.getDisplayName();

        //保存用户信息
        try {
            // 获取 UserPreferenceManager 实例
            UserPreferenceManager userPreferenceManager = UserPreferenceManager.getInstance(this);
            // 保存用户 ID
            userPreferenceManager.saveUserId(model.getUserId());
            Log.d("LoginActivity1121", userPreferenceManager.getUserId());

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // 处理错误
        }


        // TODO : initiate successful logged in experience
        //跳转到主界面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_NAME", model.getDisplayName());
        startActivity(intent);

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}