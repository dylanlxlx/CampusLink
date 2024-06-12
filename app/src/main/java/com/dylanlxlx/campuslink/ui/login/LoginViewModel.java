package com.dylanlxlx.campuslink.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.dylanlxlx.campuslink.data.LoginRepository;
import com.dylanlxlx.campuslink.data.Result;
import com.dylanlxlx.campuslink.data.model.LoggedInUser;
import com.dylanlxlx.campuslink.R;

public class LoginViewModel extends ViewModel {

    //登录表单状态MutableLiveData，用于存储登录表单的状态
    //LiveData是一个可观察的数据持有者，可以在数据发生变化时通知观察者
    //MutableLiveData是LiveData的子类，可以通过setValue()和postValue()方法更新数据
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    //构造函数，接收一个LoginRepository对象作为参数
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }


    //登录方法，接收用户名和密码作为参数
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), data.getUserId())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    //登录表单数据变化方法，接收用户名和密码作为参数
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
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
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}