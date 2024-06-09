package com.dylanlxlx.campuslink.ui.forgetPassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.databinding.ActivityForgetpasswordBinding;
import com.dylanlxlx.campuslink.databinding.ActivityRegisterBinding;
import com.dylanlxlx.campuslink.ui.login.LoginViewModelFactory;
import com.dylanlxlx.campuslink.ui.register.RegisterViewModel;

import java.util.Objects;


public class ForgetActivity extends AppCompatActivity {
    private ActivityForgetpasswordBinding binding;
    private ForgetViewModel forgetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgetpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgetViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(ForgetViewModel.class);

        final EditText emailEditText = binding.editTextEmail;
        final EditText codeEditText = binding.editverificationCode;
        final EditText passwordEditText = binding.editTextPassword;
        final EditText password2EditText = binding.editTextConfirmPassword;
        final Button codeButton = binding.buttonVerifiedCode;
        final Button forgetButton = binding.buttonForgetPassword;
        final ProgressBar loadingProgressBar = binding.loading;

        //表单状态对应的监听器
        forgetViewModel.getForgetFormState().observe(this, new Observer<ForgetFormState>() {
            @Override
            public void onChanged(@Nullable ForgetFormState forgetFormState) {
                if (forgetFormState == null) {
                    return;
                }
                forgetButton.setEnabled(forgetFormState.isDataValid());
                if (forgetFormState.getemailError() != null) {
                    emailEditText.setError(getString(forgetFormState.getemailError()));
                }
                if (forgetFormState.getcodeError() != null) {
                    codeEditText.setError(getString(forgetFormState.getcodeError()));
                }
                if (forgetFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(forgetFormState.getPasswordError()));
                }
                if (forgetFormState.getPassword2Error() != null) {
                    password2EditText.setError(getString(forgetFormState.getPassword2Error()));
                }
            }
        });
        //修改结果对应的监听器
        forgetViewModel.getForgetResult().observe(this, new Observer<ForgetResult>() {
            @Override
            public void onChanged(@Nullable ForgetResult forgetResult) {
                if (forgetResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);

                showToast(forgetResult.getMessage());
                if (Objects.equals(forgetResult.getMessage(), "注册成功")) {
                    finish();
                }
                //setResult(Activity.RESULT_OK);
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
                forgetViewModel.forgetDataChanged(
                        emailEditText.getText().toString(),
                        codeEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        password2EditText.getText().toString());
            }
        };

        //设置监听器
        emailEditText.addTextChangedListener(afterTextChangedListener);
        codeEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.addTextChangedListener(afterTextChangedListener);

        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    emailEditText.setError(getString(R.string.invalid_email));
                    return;
                }
                //显示加载进度条
                loadingProgressBar.setVisibility(View.VISIBLE);
                //发送验证码
                forgetViewModel.send(emailEditText.getText().toString());
            }
        });

        binding.buttonForgetPassword.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String code = binding.editverificationCode.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            //显示加载进度条
            loadingProgressBar.setVisibility(View.VISIBLE);
            //修改密码
            forgetViewModel.forgetPassword(email, code, password);
        });
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

