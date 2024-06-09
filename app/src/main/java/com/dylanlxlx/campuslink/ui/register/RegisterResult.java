package com.dylanlxlx.campuslink.ui.register;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {
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

    RegisterResult(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    String getMessage() {
        return message;
    }
}