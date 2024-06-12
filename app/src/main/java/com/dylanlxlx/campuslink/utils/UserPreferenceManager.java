package com.dylanlxlx.campuslink.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserPreferenceManager {
    private static UserPreferenceManager instance;
    private static final String SHARED_PREFS_FILE = "MyEncryptedPreferences";
    private static final String USER_ID_KEY = "USER_ID";
    private final SharedPreferences encryptedSharedPreferences;

    private UserPreferenceManager(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        encryptedSharedPreferences = EncryptedSharedPreferences.create(
                context,
                SHARED_PREFS_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static synchronized UserPreferenceManager getInstance(Context context) throws GeneralSecurityException, IOException {
        if (instance == null) {
            instance = new UserPreferenceManager(context);
        }
        return instance;
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        if (encryptedSharedPreferences == null) {
            return "";
        }
        return encryptedSharedPreferences.getString(USER_ID_KEY, null);
    }

    // 清除用户ID
    public void clearUserId() {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
    }
}
