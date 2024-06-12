package com.dylanlxlx.campuslink.contract;

import java.io.File;

public interface ProfileContract {

    interface View {
        void showName(String name);

        void showAvatar(String avatarUrl);

        void showError(String errorMessage);
    }

    interface Presenter {
        void loadUserData();

        void uploadImage(File tempFile);
    }
}

