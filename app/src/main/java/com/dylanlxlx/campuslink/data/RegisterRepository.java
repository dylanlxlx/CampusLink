package com.dylanlxlx.campuslink.data;

import android.util.Log;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of register status and user credentials information.
 */
public class RegisterRepository {
    private static volatile RegisterRepository instance;
    private RegisterDataSource dataSource;
    private LoggedInUser user = null;
    private String message = null;
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }
    private void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Result<LoggedInUser> register(String username, String email, String code, String password) {
        Result<LoggedInUser> result = dataSource.register(username, email, code, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }else{
            IOException error = (IOException) ((Result.Error) result).getError();
            setMessage(error.getMessage());
        }
        return result;
    }

    public Result<LoggedInUser> send(String email) {
        Result<LoggedInUser> result = dataSource.send(email);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}