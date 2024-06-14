package com.dylanlxlx.campuslink.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dylanlxlx.campuslink.contract.MyAccountContract;
import com.dylanlxlx.campuslink.client.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MyAccountPresenter implements MyAccountContract.Presenter {
    private final MyAccountContract.View view;
    private final ApiClient apiClient;
    private int userId; // Store user ID

    private String name;
    private String gender;
    private int age;
    private String phone;
    private String email;
    private String remarks;
    private CompletableFuture<String> future;

    public MyAccountPresenter(MyAccountContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient(); // 使用新的实例而不是单例模式
    }

    @Override
    public void loadUserData() {
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                JSONObject data = userSelf.getJSONObject("data");
                userId = data.getInt("id");
                name = data.optString("name", "N/A");
                gender = data.optString("gender", "N/A");
                // -1 as default value to indicate missing age
                age = data.optInt("age", -1);
                phone = data.optString("phone", "N/A");
                email = data.optString("mail", "N/A");
                remarks = data.optString("remarks", "N/A");
                future.complete(String.valueOf(userId));
            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public void refreshView() {
        future = new CompletableFuture<>();
        loadUserData();
        if (future.join() != null) {
            new Thread(() -> {
                // 在 UI 线程中更新视图
                new Handler(Looper.getMainLooper()).post(() -> {
                    view.showName(name);
                    view.showGender(gender);
                    if (age != -1) {
                        view.showAge(age);
                    } else {
                        view.showAge(0);  // or handle the absence of age as you prefer
                    }
                    view.showPhone(phone);
                    view.showEmail(email);
                    view.showRemarks(remarks);
                });
            }).start();
        }
    }

    @Override
    public void updateUserInfo(int userId, String field, String value) {
        new Thread(() -> {
            try {
                JSONObject userUpdate = new JSONObject();
                userUpdate.put("id", this.userId);
                userUpdate.put(field, value);

                apiClient.updateUser(userUpdate, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Information updated successfully"));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                    }
                });
            } catch (JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public int getUserId() {
        return userId;
    }
}
