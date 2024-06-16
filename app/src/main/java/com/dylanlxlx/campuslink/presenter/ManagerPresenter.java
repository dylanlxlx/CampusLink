package com.dylanlxlx.campuslink.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    private JSONObject userData;

    public ManagerPresenter(ManagerContract.View view) {
        this.view = view;
        this.apiClient = new ApiClient();
        userData = loadUserData();
    }


    @Override
    public JSONObject loadUserData() {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                future.complete(apiClient.getUserSelf().getJSONObject("data"));
            } catch (IOException | JSONException e) {
                new Handler(Looper.getMainLooper()).post(() -> view.showError(e.getMessage()));
            }
        }).start();
        return future.join();
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
        new Thread(() -> {
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
        new Thread(() -> future.complete(apiClient.queryUser(userId))).start();
        return future.join();
    }

    @Override
    public JSONObject queryUsers(String name) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                future.complete(apiClient.queryUsers(name));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return future.join();
    }

    public void newDialog(int sendId, int receiveId, String content, String msgType) {
        new Thread(() -> {
            try {
                JSONObject newDialog = new JSONObject();
                newDialog.put("sendId", sendId);
                newDialog.put("receiveId", receiveId);
                newDialog.put("content", content);
                newDialog.put("msgType", msgType);
                apiClient.newDialog(newDialog, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Dialog added"));
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

    public void withdrawDialog(int dialogId) {
        new Thread(() -> {
            try {
                JSONObject id = new JSONObject();
                id.put("id", dialogId);
                apiClient.withdrawDialog(id, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("Dialog withdrawn"));
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

    public JSONObject queryDialog(int sendId, int receiveId) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                JSONObject data = new JSONObject();
                data.put("sendId", sendId);
                data.put("receiveId", receiveId);
                future.complete(apiClient.queryDialog(data));
            } catch (JSONException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return future.join();

    }

    public JSONObject getChatList() {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();
        new Thread(() -> future.complete(apiClient.getChatList())).start();
        return future.join();
    }

    @Override
    public void submitReport(String type, int ownId, int otherId, String content) {
        new Thread(() -> {
            try {
                JSONObject newBulletin = new JSONObject();
                newBulletin.put("type", type);
                newBulletin.put("infoId", ownId);
                newBulletin.put("complainId", otherId);
                newBulletin.put("reason", content);
                apiClient.submitReport(newBulletin, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("submitReportSuccess"));
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

    @Override
    public void searchReport(int userId,int userRole) {
        new Thread(() -> {
            try {
                JSONObject newBulletin = new JSONObject();
                if (userRole != 2) {
                    newBulletin.put("infoId", userId);
                }
                Log.d("submitReport", "submitReport: " + newBulletin + "//");
                apiClient.searchReport(newBulletin, new ApiClient.MyReportCallback() {
                    @Override
                    public void onSuccess(String data) {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess(data));
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

    @Override
    public void withdrawReport(int reportId) {
        new Thread(() -> {
            try {
                JSONObject newBulletin = new JSONObject();
                newBulletin.put("id", reportId);
                apiClient.withdrawReport(newBulletin, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("withdrawReport"));
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

    @Override
    public void manageReport(int managerId, int reportId, String content) {
        new Thread(() -> {
            try {
                JSONObject newBulletin = new JSONObject();
                newBulletin.put("solveId", managerId);
                newBulletin.put("id", reportId);
                newBulletin.put("remarks", content);
                apiClient.manageReport(newBulletin, new ApiClient.Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler(Looper.getMainLooper()).post(() -> view.showSuccess("handleReportSuccess"));
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

    @Override
    public int getRole() {
        try {
            return userData.getInt("role");

        } catch (JSONException e) {
            return 0;
        }
    }

    @Override
    public int getUserId() {
        try {
            return userData.getInt("id");

        } catch (JSONException e) {
            return 0;
        }
    }
}
