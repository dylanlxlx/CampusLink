package com.dylanlxlx.campuslink.data;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 处理身份验证和登录凭据并检索用户信息的类
 */

public class RegisterDataSource {
    private static final String AUTH_URL = "https://your-auth-server.com/auth";

    public Result<LoggedInUser> register(String username, String email, String code, String password) {

        try {

//            // TODO: handle loggedInUser authentication
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
//            return new Result.Success<>(fakeUser);

//            String authToken = getAuthToken(username, password);
//            if (authToken != null) {
//                // 模拟从服务器获取用户信息
//                LoggedInUser user = new LoggedInUser(
//                        java.util.UUID.randomUUID().toString(),
//                        username
//                );
//                return new Result.Success<>(user);
//            } else {
//                return new Result.Error(new IOException("Invalid credentials"));
//            }

            if ("test".equals(username) && "password".equals(password)) {
                LoggedInUser fakeUser = new LoggedInUser(
                        UUID.randomUUID().toString(),
                        "Test User"
                );
                return new Result.Success<>(fakeUser);
            } else {
                // 模拟登录失败
                return new Result.Error(new IOException("Invalid username or password"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private String getAuthToken(String username, String password) throws IOException {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        URL url = new URL(AUTH_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 假设服务器返回一个简单的字符串作为身份验证令牌
            return "valid_auth_token";
        } else {
            return null;
        }
    }

    public void logout() {
        // 在真实环境中，这里可能需要进行一些清理工作
        // 比如清除缓存、令牌等
        // TODO: revoke authentication
    }
}