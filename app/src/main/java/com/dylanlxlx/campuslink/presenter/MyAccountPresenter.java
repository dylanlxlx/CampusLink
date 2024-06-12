package com.dylanlxlx.campuslink.presenter;

import android.os.Handler;
import android.os.Looper;

import com.dylanlxlx.campuslink.contract.MyAccountContract;
import com.dylanlxlx.campuslink.client.ApiClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MyAccountPresenter implements MyAccountContract.Presenter {
    private final MyAccountContract.View view;
    private final ApiClient apiClient;
    private int userId; // Store user ID

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
                String name = data.optString("name", "N/A");
                String gender = data.optString("gender", "N/A");
                int age = data.optInt("age", -1);  // -1 as default value to indicate missing age
                String phone = data.optString("phone", "N/A");
                String email = data.optString("mail", "N/A");
                String remarks = data.optString("remarks", "N/A");

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
            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public void loadUserId() {
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                JSONObject data = userSelf.getJSONObject("data");

                userId = data.getInt("id");

            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public void updateUserInfo(int userId, String field, String value) {
        new Thread(() -> {
            try {
                JSONObject userUpdate = new JSONObject();
                userUpdate.put("id", this.userId);
                userUpdate.put(field, value);

                apiClient.updateUser(userUpdate, new ApiClient.UpdateUserCallback() {
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
