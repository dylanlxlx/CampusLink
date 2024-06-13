package com.dylanlxlx.campuslink.presenter;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.ManagerContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

public class ManagerPresenter implements ManagerContract.Presenter {

    private final ManagerContract.View view;
    private final ApiClient apiClient;
    private int userId;
    private int role;

    public ManagerPresenter(ManagerContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
    }


    @Override
    public void loadUserData() {
        new Thread(() -> {
            try {
                JSONObject userSelf = apiClient.getUserSelf();
                JSONObject data = userSelf.getJSONObject("data");
                userId = data.getInt("id");
                role = data.getInt("role");
            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
    }

    @Override
    public void addBulletin(String title, String content, int userId, int role) {
        if (role == 2) {
            new Thread(() -> {
                try {
                    JSONObject newBulletin = new JSONObject();
                    newBulletin.put("title", title);
                    newBulletin.put("content", content);
                    newBulletin.put("userId", userId);
                    apiClient.addBulletin(newBulletin, new ApiClient.Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Bulletin added"));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    @Override
    public void deleteBulletin(int bulletinId, int role) {
        if (role == 2) {
            new Thread(() -> {
                try {
                    apiClient.deleteBulletin(bulletinId, new ApiClient.Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Bulletin deleted"));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                        }
                    });
                } catch (IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
                }
            }).start();
        }
    }

    @Override
    public void amendBulletin(int bulletinId, String title, String content, int role) {
        if (role == 2) {
            new Thread(() -> {
                try {
                    JSONObject newBulletin = new JSONObject();
                    newBulletin.put("id", bulletinId);
                    newBulletin.put("title", title);
                    newBulletin.put("content", content);
                    apiClient.amendBulletin(newBulletin, new ApiClient.Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Bulletin amended"));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    @Override
    public JSONObject queryBulletin(int pageNum, int pageSize) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(()->{
            try {
                future.complete(apiClient.queryBulletin(pageNum, pageSize));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return future.join();
    }



    @Override
    public void addUsers(String username, String password, @Nullable String name, int role) {
        if (role == 2) {
            new Thread(() -> {
                try {
                    JSONObject newUser = new JSONObject();
                    newUser.put("username", username);
                    newUser.put("password", password);
                    newUser.put("role", role);
                    if (name != null) {
                        newUser.put("name", name);
                    }
                    apiClient.addUser(newUser, new ApiClient.Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("User added"));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    @Override
    public void deleteUsers(int userId) {
        new Thread(() -> apiClient.deleteUser(userId, new ApiClient.Callback() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("User deleted"));
            }

            @Override
            public void onFailure(String errorMessage) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(errorMessage));
            }
        })).start();
    }

    @Override
    public JSONObject queryUser(int userId) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(()-> future.complete(apiClient.queryUser(userId))).start();
        return future.join();
    }

    @Override
    public JSONObject queryUsers(String name) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(()-> {
            try {
                future.complete(apiClient.queryUsers(name));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return future.join();
    }


    @Override
    public int getRole() {
        return role;
    }

    @Override
    public int getUserId() {
        return userId;
    }
}
