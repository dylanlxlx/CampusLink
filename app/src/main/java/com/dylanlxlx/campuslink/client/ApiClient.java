package com.dylanlxlx.campuslink.client;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://47.121.131.98:8081";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_VALUE = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoyLCJuYW1lIjoi566h55CG5ZGYIiwiaWQiOjksInVzZXJuYW1lIjoiYWRtaW4iLCJleHAiOjE3MjA0NTczNDZ9.V-1T1bbFSs-AGT5usFCnOjXKIUsblsvLOEDmzSon1Ew";

    private final OkHttpClient client;

    public ApiClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 添加拦截器来设置全局Header
        Interceptor headerInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request requestWithHeaders = originalRequest.newBuilder()
                    .header(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE)
                    .build();
            return chain.proceed(requestWithHeaders);
        };

        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(headerInterceptor)
                .build();
    }

    public JSONObject getUserSelf() throws IOException {
        String url = BASE_URL + "/user/self";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析 JSON 响应体
            assert response.body() != null;
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
