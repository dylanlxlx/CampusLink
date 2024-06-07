package com.dylanlxlx.campuslink.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.dylanlxlx.campuslink.data.LoginDataSource;
import com.dylanlxlx.campuslink.data.LoginRepository;
import com.dylanlxlx.campuslink.data.RegisterDataSource;
import com.dylanlxlx.campuslink.data.RegisterRepository;
import com.dylanlxlx.campuslink.ui.register.RegisterViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel.class)){
            return (T) new RegisterViewModel(RegisterRepository.getInstance(new RegisterDataSource()));
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}