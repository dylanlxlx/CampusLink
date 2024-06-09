package com.dylanlxlx.campuslink.presenter;


import com.dylanlxlx.campuslink.ProfileActivity;
import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.ProfileContract;

import org.json.JSONException;
import org.json.JSONObject;

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
}

