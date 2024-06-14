package com.dylanlxlx.campuslink.contract;

import android.net.Uri;

public interface PublishContract {
    interface View {
        void showError(String errorMessage);
        void showSuccess(String successMessage);
    }

    interface Presenter {
        void uploadImage(Uri imageUri, String title, String description, double price);
    }
}
