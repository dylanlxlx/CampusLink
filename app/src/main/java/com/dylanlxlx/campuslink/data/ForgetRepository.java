package com.dylanlxlx.campuslink.data;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of register status and user credentials information.
 */
public class ForgetRepository {
    private static volatile ForgetRepository instance;
    private ForgetDataSource dataSource;
    private LoggedInUser user = null;
    private String message = null;
    private ForgetRepository(ForgetDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ForgetRepository getInstance(ForgetDataSource dataSource) {
        if (instance == null) {
            instance = new ForgetRepository(dataSource);
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

    public Result<LoggedInUser> forgetPassword(String email, String code, String password) {
        Result<LoggedInUser> result = dataSource.forgetPassword(email, code, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }else{
            IOException error = (IOException) ((Result.Error) result).getError();
            setMessage(error.getMessage());
        }
        return result;
    }

    public Result<LoggedInUser> send(String email) {
        RegisterDataSource dataSource = new RegisterDataSource();
        Result<LoggedInUser> result = dataSource.send(email);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}