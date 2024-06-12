package com.dylanlxlx.campuslink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.transition.TransitionInflater;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dylanlxlx.campuslink.contract.ProfileContract;
import com.dylanlxlx.campuslink.presenter.ProfilePresenter;
import com.dylanlxlx.campuslink.ui.login.LoginActivity;
import com.dylanlxlx.campuslink.utils.CircleTransform;
import com.dylanlxlx.campuslink.utils.UserPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ProfileContract.Presenter presenter;
    private TextView nameTextView;
    private ImageView avatarImageView;
    private Button myAccountButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.hello_name);
        avatarImageView = findViewById(R.id.iv_avatar);
        myAccountButton = findViewById(R.id.btn_to_my_account);
        logoutButton = findViewById(R.id.log_out);

        presenter = new ProfilePresenter(this);
        presenter.loadUserData();

        // 设置进入和退出的过渡动画
        setupWindowTransitions();

        // 初始化底部导航视图
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, bottomNavigationView, "bottomNavigationView");
            return handleNavigationItemSelected(itemId, options);
        });

        // 设置头像点击事件
        avatarImageView.setOnClickListener(v -> showAvatarOptions());

        // 设置我的账户按钮点击事件
        myAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyAccountActivity.class);
            startActivity(intent);
        });
        // 设置我的账户按钮点击事件
        logoutButton.setOnClickListener(v -> {
            // 检查用户是否已登录，如果未登录，则跳转到登录页面
            try {
                UserPreferenceManager userPreferenceManager = UserPreferenceManager.getInstance(this);
                UserPreferenceManager.getInstance(null).clearUserId();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadUserData();
    }

    private void showAvatarOptions() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_avatar_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.btn_change_avatar).setOnClickListener(v -> {
            pickImageFromGallery();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btn_save_avatar).setOnClickListener(v -> {
            saveAvatarToLocal();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File tempFile = File.createTempFile("upload", ".jpg", getCacheDir());
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            // 检查文件大小和路径
            if (tempFile.exists() && tempFile.canRead() && tempFile.length() > 0) {
                Log.d("ProfileActivity", "Temp file path: " + tempFile.getAbsolutePath());
                Log.d("ProfileActivity", "Temp file size: " + tempFile.length() + " bytes");
                presenter.uploadImage(tempFile);
            } else {
                showError("Temp file is not accessible or is empty.");
            }
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }


    private void saveAvatarToLocal() {
        Bitmap bitmap = ((BitmapDrawable) avatarImageView.getDrawable()).getBitmap();
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            showError("Avatar saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }


    /**
     * 设置窗口的过渡动画
     */
    private void setupWindowTransitions() {
        getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
        getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_transition));
    }

    /**
     * 处理底部导航栏的点击事件
     *
     * @param itemId  导航项的ID
     * @param options 过渡动画选项
     * @return 如果点击的项目是当前项目，返回true，否则返回false
     */
    private boolean handleNavigationItemSelected(int itemId, ActivityOptions options) {
        Intent intent;
        if (itemId == R.id.bottom_home) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else if (itemId == R.id.bottom_search) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
        } else if (itemId == R.id.bottom_dialog) {
            intent = new Intent(getApplicationContext(), DialogActivity.class);
        } else return itemId == R.id.bottom_profile;
        startActivity(intent, options.toBundle());
        return true;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void showName(String name) {
        nameTextView.setText("Hello, " + name);
    }


    @Override
    public void showAvatar(String avatarUrl) {
        Picasso.get()
                .load(avatarUrl)
                .transform(new CircleTransform())
                .into(avatarImageView);
    }

    @Override
    public void showError(String errorMessage) {
        // 展示错误信息，可以使用Toast或Snackbar
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
