package com.dylanlxlx.campuslink.client;

import android.util.Log;

import com.dylanlxlx.campuslink.adapter.Product;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClient {

//    private static final String BASE_URL = "http://47.121.131.98:8081";
    private static final String BASE_URL = "http://8.130.145.46:8081";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static String AUTHORIZATION_VALUE = null;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;

    public ApiClient() {

        try {
            // 通过 UserPreferenceManager 获取用户 ID
            AUTHORIZATION_VALUE = UserPreferenceManager.getInstance(null).getUserId();
            Log.e("ApiClient", AUTHORIZATION_VALUE);
        } catch (Exception e) {
            Log.e("ApiClient", "Failed to get user ID: " + e.getMessage());

        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 添加拦截器来设置全局Header
        Interceptor headerInterceptor = chain -> {
            Request originalRequest = chain.request();
            Request requestWithHeaders = originalRequest.newBuilder().header(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();
            return chain.proceed(requestWithHeaders);
        };

        client = new OkHttpClient.Builder().addInterceptor(logging).addInterceptor(headerInterceptor).build();
    }

    public JSONObject getUserSelf() throws IOException {
        String url = BASE_URL + "/user/self";
        Request request = new Request.Builder().url(url).build();

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

    public ArrayList<Product> getRandomProducts() throws IOException, JSONException {
        String url = BASE_URL + "/goods/random";
        JSONObject jsonBody = new JSONObject();
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析 JSON 响应体
            String responseBody = response.body().string();
            System.out.println("Response Body: " + responseBody); // 输出完整的 JSON 响应以进行调试

            JSONObject jsonObject = new JSONObject(responseBody);
            if (!jsonObject.has("data")) {
                throw new JSONException("No value for data");
            }
            JSONArray dataArray = jsonObject.getJSONArray("data");

            ArrayList<Product> products = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject productObject = dataArray.getJSONObject(i);
                Product product = new Product(productObject.getInt("id"), productObject.getInt("userId"), productObject.getString("category"), productObject.getString("title"), productObject.getString("description"), productObject.getString("status"), productObject.getDouble("price"), productObject.getInt("num"), productObject.getInt("sales"), productObject.optString("image", null), productObject.getString("createTime"), productObject.getString("updateTime"));
                products.add(product);
            }
            return products;
        }
    }

    public static void uploadImage(File file, UploadCallback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("image", file.getName(), fileBody).build();

        Request request = new Request.Builder().url(BASE_URL + "/file/image").post(requestBody).addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        boolean success = jsonObject.getBoolean("success");

                        Log.e("ApiClient", "Response: " + jsonObject.toString());
                        if (success) {
                            String imageUrl = jsonObject.getString("data");
                            callback.onSuccess(imageUrl);
                        } else {
                            String errorMessage = jsonObject.getString("message");
                            Log.e("ApiClient", "Upload failed: " + errorMessage);
                            callback.onFailure(errorMessage);
                        }
                    } catch (JSONException e) {
                        Log.e("ApiClient", "JSON parsing error: " + e.getMessage());
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    Log.e("ApiClient", "Response error: " + response.message());
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void updateUser(JSONObject userJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(userJson.toString(), JSON);
        Request request = new Request.Builder().url(BASE_URL + "/user/update").post(body).addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    //公告相关功能

    public void addBulletin(JSONObject bulletinJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(bulletinJson.toString(), JSON);
        Request request = new Request.Builder().url(BASE_URL + "/notice/add").post(body).addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void deleteBulletin(int id, Callback callback) throws IOException {
        String url = BASE_URL + "/notice/delete/query?id=" + id;
        Request request = new Request.Builder().url(url).delete().addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public JSONObject queryBulletin(int pageNum, int pageSize) throws JSONException {
        String url = BASE_URL + "/notice/query?" + pageNum + "&" + pageSize;
        JSONObject jsonBody = new JSONObject();
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // 解析 JSON 响应体
            assert response.body() != null;
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void amendBulletin(JSONObject bulletinJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(bulletinJson.toString(), JSON);
        Request request = new Request.Builder().url(BASE_URL + "/notice/update").put(body).addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void addUser(JSONObject userJson, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(userJson.toString(), JSON);
        Request request = new Request.Builder().url(BASE_URL + "/user").post(body).addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void deleteUser(int id, Callback callback) {
        String url = BASE_URL + "/user/" + id;
        Request request = new Request.Builder().url(url).delete().addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public JSONObject queryUser(int id) {
        String url = BASE_URL + "/user/" + id;
        Request request = new Request.Builder().url(url).get().addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析 JSON 响应体
            assert response.body() != null;
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject queryUsers(String name) {
        String url = BASE_URL + "/user/vague?" + name;
        Request request = new Request.Builder().url(url).get().addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 解析 JSON 响应体
            assert response.body() != null;
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Callback {
        void onSuccess();

        void onFailure(String errorMessage);
    }


    public interface UploadCallback {
        void onSuccess(String imageUrl);

        void onFailure(String errorMessage);
    }

}
