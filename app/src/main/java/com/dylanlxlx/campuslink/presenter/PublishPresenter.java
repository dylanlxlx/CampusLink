package com.dylanlxlx.campuslink.presenter;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.PublishContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class PublishPresenter implements PublishContract.Presenter {
    private final PublishContract.View view;
    private final ApiClient apiClient;

    public PublishPresenter(PublishContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
    }

    @Override
    public void uploadImage(Uri imageUri, String title, String description, double price) {
        File imageFile = new File(imageUri.getPath());
        apiClient.uploadImage(imageFile, new ApiClient.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                try {
                    JSONObject userSelf = apiClient.getUserSelf();
                    int userId = userSelf.getJSONObject("data").getInt("id");

                    JSONObject product = new JSONObject();
                    product.put("id", 0); // 设置一个默认值，服务器会生成ID
                    product.put("userId", userId); // 假设当前用户ID为1
                    product.put("category", "nisi"); // 假设一个默认类别
                    product.put("title", title);
                    product.put("description", description);
                    product.put("status", "正常");
                    product.put("price", price);
                    product.put("image", imageUrl);

                    apiClient.addProduct(product, new ApiClient.UpdateUserCallback() {
                        @Override
                        public void onSuccess() {
                            new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Product added successfully"));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                        }
                    });
                } catch (IOException | JSONException e) {
                    new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
            }
        });
    }
}
