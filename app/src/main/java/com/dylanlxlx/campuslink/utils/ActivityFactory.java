package com.dylanlxlx.campuslink.utils;


import android.content.Context;
import android.content.Intent;

import com.dylanlxlx.campuslink.CommonActivity;

public class ActivityFactory {

    public static final String EXTRA_TYPE = "extra_type";

    public static void startActivity(Context context, ActivityType type) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    public enum ActivityType {
        CHANGE_NICKNAME,
        CHANGE_PHONE_NUMBER,
        CHANGE_EMAIL,
        SET_INTRO
    }
}

