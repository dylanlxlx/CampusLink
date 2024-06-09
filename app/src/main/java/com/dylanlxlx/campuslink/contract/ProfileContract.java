package com.dylanlxlx.campuslink.contract;

public interface ProfileContract {

    interface View {
        void showName(String name);

        void showAvatar(String avatarUrl);

        void showError(String errorMessage);
    }

    interface Presenter {
        void loadUserData();
    }
}

