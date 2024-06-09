package com.dylanlxlx.campuslink.ui.register;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dylanlxlx.campuslink.R;
import com.dylanlxlx.campuslink.data.RegisterRepository;
import com.dylanlxlx.campuslink.data.Result;
import com.dylanlxlx.campuslink.data.model.LoggedInUser;

public class RegisterViewModel extends ViewModel {

    //登录表单状态MutableLiveData，用于存储登录表单的状态
    //LiveData是一个可观察的数据持有者，可以在数据发生变化时通知观察者
    //MutableLiveData是LiveData的子类，可以通过setValue()和postValue()方法更新数据
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    public RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }


    //注册方法，接收用户名、邮箱号、验证码和密码作为参数
    public void register(String username, String email, String code, String password) {
        Result<LoggedInUser> result = registerRepository.register(username, email, code, password);

        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult("注册成功"));
        } else {
            registerResult.setValue(new RegisterResult(registerRepository.getMessage()));
        }
    }

    //邮件接收验证码方法，接收邮箱号作为参数
    public void send(String email) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = registerRepository.send(email);

        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult("发送成功"));
        } else {
            registerResult.setValue(new RegisterResult("发送失败"));
        }
    }

    //登录表单数据变化方法，接收用户名、邮箱号、验证码、密码和二次密码作为参数
    public void registerDataChanged(String username, String email, String code, String password, String password2) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null, null));
        } else if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null, null));
        } else if (code == null || code.trim().isEmpty()) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_code, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password, null));
        } else if (!password.equals(password2)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, null, R.string.invalid_password2));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty()&& username.trim().length() >= 5;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}