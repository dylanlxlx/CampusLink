package com.dylanlxlx.campuslink.ui.forgetPassword;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class ForgetResult {
//    @Nullable
//    private Integer value;
//
//    RegisterResult(@Nullable Integer value) {
//        this.value = value;
//    }
//
//    @Nullable
//    Integer getValue() {
//        return value;
//    }


    @Nullable
    private String message;

    ForgetResult(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    String getMessage() {
        return message;
    }
}