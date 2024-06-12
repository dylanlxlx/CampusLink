package com.dylanlxlx.campuslink.contract;

public interface MyAccountContract {
    interface View {
        void showName(String name);
        void showGender(String gender);
        void showAge(int age);
        void showPhone(String phone);
        void showEmail(String email);
        void showRemarks(String remarks);
        void showError(String errorMessage);
        void showSuccess(String successMessage);
    }

    interface Presenter {
        void refreshView();
        void loadUserData();
        void updateUserInfo(int userId, String field, String value);
        int getUserId();
    }
}
