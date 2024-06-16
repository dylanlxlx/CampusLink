package com.dylanlxlx.campuslink.data;

import com.dylanlxlx.campuslink.data.model.LoggedInUser;
import com.dylanlxlx.campuslink.string.DefaultString;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 处理身份验证和登录凭据并检索用户信息的类
 */

public class ForgetDataSource {
    public Result<LoggedInUser> forgetPassword(String email, String code, String password) {
        try {
            String responseData = forgetRequest(email, code, password);
            if(responseData!= null){
                JSONObject jsonResponse = new JSONObject(responseData);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    // 返回一个成功的结果
                    return new Result.Success<>(null);
                } else {
                    //从 JSON 响应中获取错误消息
                    String message = jsonResponse.getString("message");
                    return new Result.Error(new IOException(message));
                }
            }else{
                return new Result.Error(new IOException("Invalid response"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register in", e));
        }
    }

    private String forgetRequest(String email, String code, String password) throws IOException {
        String AUTH_URL = new DefaultString().getUrl() +  "/user/resetPwd?";
        try{
            OkHttpClient client = new OkHttpClient();
            // 构建 URL 并添加查询参数
            HttpUrl.Builder urlBuilder = HttpUrl.parse(AUTH_URL).newBuilder();
            urlBuilder.addQueryParameter("code", code);
            urlBuilder.addQueryParameter("mail", email);
            urlBuilder.addQueryParameter("password", password);
            String url = urlBuilder.build().toString();
            // 手动对邮箱参数进行解码
            url = url.replace("%40", "@");
            RequestBody body = RequestBody.create("{}", MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .build();


            CompletableFuture<String> future = new CompletableFuture<>();
            // 发送请求并处理响应
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    future.complete(response.body().string());
                }
            });
            // 等待异步操作完成并获取结果
            String responseData = future.join();

            if (responseData != null) {
                return responseData;
            } else {
                System.out.println("Failed to receive responseData");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void logout() {
        // 在真实环境中，这里可能需要进行一些清理工作
        // 比如清除缓存、令牌等
        // TODO: revoke authentication
    }
}