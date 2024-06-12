package com.dylanlxlx.campuslink.presenter;


import com.dylanlxlx.campuslink.ProfileActivity;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.ProfileContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ProfilePresenter implements ProfileContract.Presenter {

    private final ProfileContract.View view;
    private final ApiClient apiClient;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
    }

    @Override
    public void loadUserData() {
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                JSONObject data = userSelf.getJSONObject("data");

                String name = data.getString("name");
                String avatarUrl = data.getString("avatar");

                // 在 UI 线程中更新视图
                ((ProfileActivity) view).runOnUiThread(() -> {
                    view.showName(name);
                    view.showAvatar(avatarUrl);
                });
            } catch (IOException | JSONException e) {
                ((ProfileActivity) view).runOnUiThread(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    public void uploadImage(File imageFile) {
        ApiClient.uploadImage(imageFile, new ApiClient.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                updateAvatar(imageUrl);
            }

            @Override
            public void onFailure(String errorMessage) {
                ((ProfileActivity) view).runOnUiThread(() -> view.showError(errorMessage));
            }
        });
    }

    private void updateAvatar(String imageUrl) {
        // 获取当前用户信息
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                JSONObject data = userSelf.getJSONObject("data");

                int id = data.getInt("id");
                String username = data.getString("username");
                String name = data.getString("name");
                String phone = data.getString("phone");
                double money = data.getDouble("money");
                String remarks = data.getString("remarks");
                int role = data.getInt("role");

                // 创建更新用户信息的JSON对象
                JSONObject updateUserJson = new JSONObject();
                updateUserJson.put("id", id);
                updateUserJson.put("username", username);
                updateUserJson.put("name", name);
                updateUserJson.put("phone", phone);
                updateUserJson.put("money", money);
                updateUserJson.put("avatar", imageUrl);
                updateUserJson.put("remarks", remarks);
                updateUserJson.put("role", role);

                // 调用更新用户信息接口
                apiClient.updateUser(updateUserJson, new ApiClient.UpdateUserCallback() {
                    @Override
                    public void onSuccess() {
                        ((ProfileActivity) view).runOnUiThread(() -> view.showAvatar(imageUrl));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        ((ProfileActivity) view).runOnUiThread(() -> view.showError(errorMessage));
                    }
                });
            } catch (IOException | JSONException e) {
                ((ProfileActivity) view).runOnUiThread(() -> view.showError(e.getMessage()));
            }
        }).start();
    }



}

