package com.dylanlxlx.campuslink.data;

import android.util.Log;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

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
    private static final String AUTH_URL = "http://47.121.131.98:8081/user/login";
    public Result<LoggedInUser> login(String username, String password) {

        try {
            String authToken = getAuthToken(username, password);
            Log log = null;
            log.d("LoginDataSource", authToken);
            if (authToken != null) {
                // 模拟从服务器获取用户信息
                LoggedInUser user = new LoggedInUser(
                        UUID.randomUUID().toString(),
                        username
                );

                log.d("LoginDataSource", "Login success");
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new IOException("Invalid credentials"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private String getAuthToken(String username, String password) throws IOException {
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        String json = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        // 创建请求
        Request request = new Request.Builder()
                .url(AUTH_URL)
                .post(body)
                .build();

        // 发送请求并处理响应
        CompletableFuture<String> future = new CompletableFuture<>();
        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("HTTP error: " + response.code());
                    return;
                }

                // 读取响应体
                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);

                String dataToken = null;
                // 解析响应
                if (responseBody.contains("\"success\":true")) {
                    dataToken = responseBody.substring(responseBody.indexOf("\"data\":\"") + 8, responseBody.indexOf("\"}", responseBody.indexOf("\"data\":\"")));

                    Log log = null;
                    log.d("LoginDataSource", dataToken);
                    // 在这里添加其他操作
                } else {
                    System.out.println("Login failed");
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