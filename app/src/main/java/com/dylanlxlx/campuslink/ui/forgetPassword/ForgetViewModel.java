package com.dylanlxlx.campuslink.ui.forgetPassword;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.ForgetRepository;
import com.dylanlxlx.campuslink.data.Result;
import com.dylanlxlx.campuslink.data.model.LoggedInUser;

public class ForgetViewModel extends ViewModel {
    private MutableLiveData<ForgetFormState> forgetFormState = new MutableLiveData<>();
    private MutableLiveData<ForgetResult> forgetResult = new MutableLiveData<>();
    private ForgetRepository forgetRepository;

    public ForgetViewModel(ForgetRepository forgetRepository) {
        this.forgetRepository = forgetRepository;
    }

    LiveData<ForgetFormState> getForgetFormState() {
        return forgetFormState;
    }

    LiveData<ForgetResult> getForgetResult() {
        return forgetResult;
    }


    //注册方法，接收邮箱号、验证码和密码作为参数
    public void forgetPassword(String email, String code, String password) {
        Result<LoggedInUser> result = forgetRepository.forgetPassword(email, code, password);

        if (result instanceof Result.Success) {
            forgetResult.setValue(new ForgetResult("修改成功"));
        } else {
            forgetResult.setValue(new ForgetResult(forgetRepository.getMessage()));
        }
    }

    //邮件接收验证码方法，接收邮箱号作为参数
    public void send(String email) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = forgetRepository.send(email);

        if (result instanceof Result.Success) {
            forgetResult.setValue(new ForgetResult("发送成功"));
        } else {
            forgetResult.setValue(new ForgetResult("发送失败"));
        }
    }

    //登录表单数据变化方法，接收用户名、邮箱号、验证码、密码和二次密码作为参数
    public void forgetDataChanged(String email, String code, String password, String password2) {
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgetFormState.setValue(new ForgetFormState(null, R.string.invalid_email, null, null, null));
        } else if (code == null || code.trim().isEmpty()) {
            forgetFormState.setValue(new ForgetFormState(null, null, R.string.invalid_code, null, null));
        } else if (!isPasswordValid(password)) {
            forgetFormState.setValue(new ForgetFormState(null, null, null, R.string.invalid_password, null));
        } else if (!password.equals(password2)) {
            forgetFormState.setValue(new ForgetFormState(null, null, null, null, R.string.invalid_password2));
        } else {
            forgetFormState.setValue(new ForgetFormState(true));
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}