package com.dylanlxlx.campuslink.data;


import androidx.annotation.NonNull;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;
import com.dylanlxlx.campuslink.string.DefaultString;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 处理身份验证和登录凭据并检索用户信息的类
 */

public class LoginDataSource {
    private static final String AUTH_URL = new DefaultString().getUrl() + "/user/login";

    public Result<LoggedInUser> login(String username, String password) {
        String authToken = getAuthToken(username, password);
        if (authToken != null) {
            // 模拟从服务器获取用户信息
            LoggedInUser user = new LoggedInUser(authToken, username);
            return new Result.Success<>(user);
        } else {
            return new Result.Error(new IOException("Invalid credentials"));
        }
    }

    private String getAuthToken(String username, String password) {
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        // 创建请求
        Request request = new Request.Builder().url(AUTH_URL).post(body).build();

        // 发送请求并处理响应
        CompletableFuture<String> future = new CompletableFuture<>();
        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("HTTP error: " + response.code());
                    return;
                }
                // 读取响应体
                assert response.body() != null;
                String responseBody = response.body().string();
                String dataToken = null;
                // 解析响应
                if (responseBody.contains("\"success\":true")) {
                    dataToken = responseBody.substring(responseBody.indexOf("\"data\":\"") + 8, responseBody.indexOf("\"}", responseBody.indexOf("\"data\":\"")));
                    // 在这里添加其他操作
                }
                future.complete(dataToken); // 成功时完成 future 返回 dataToken
            }
        });

        // 等待异步操作完成并获取结果
        String dataToken = future.join();
        if (dataToken != null) {
            System.out.println("Received data token: " + dataToken);
            // 在这里可以对 dataToken 进行进一步的处理
            return dataToken;
        } else {
            System.out.println("Failed to receive data token");
        }
        return null;
    }

    public void logout() {
        // 在真实环境中，这里可能需要进行一些清理工作
        // 比如清除缓存、令牌等
        // TODO: revoke authentication
    }
}