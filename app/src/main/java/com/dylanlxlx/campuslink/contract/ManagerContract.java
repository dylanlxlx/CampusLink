package com.dylanlxlx.campuslink.contract;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.List;

public interface ManagerContract {
    interface View {
        void showError(String errorMessage);

        void showSuccess(String successMessage);

    }

    interface Presenter {
        int getRole();

        int getUserId();

        JSONObject loadUserData();

        void addBulletin(String title, String content, int userId, int role);

        void deleteBulletin(int bulletinId, int role);

        void amendBulletin(int bulletinId, String title, String content, int role);

        JSONObject queryBulletin(int pageNum, int pageSize);

        void addUsers(String username, String password, @Nullable String name, int role);

        void deleteUsers(int userId);

        JSONObject queryUser(int userId);

        JSONObject queryUsers(String name);

        void submitReport(String type, int ownId, int otherId, String content);

        void searchReport(int userId);

        void withdrawReport(int reportId);

        JSONObject manageReport(int managerId, int reportId, String content);
    }
}
