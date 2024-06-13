package com.dylanlxlx.campuslink;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanlxlx.campuslink.client.ApiClient;
import com.dylanlxlx.campuslink.contract.PublishContract;
import com.dylanlxlx.campuslink.presenter.PublishPresenter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PublishActivity extends AppCompatActivity implements PublishContract.View {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText mEtTitleText, mEtDescriptionText, mEtPriceValue;
    private ImageView mIvAddImage, mIvCancelButton;
    private TextView mTvAddImage;
    private RelativeLayout mRlAddImage;
    private Button mBtnPost;
    private Uri imageUri;
    private PublishPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        initViews();

        presenter = new PublishPresenter(this);
        mIvCancelButton.setOnClickListener(v -> finish());

        mRlAddImage.setOnClickListener(v -> pickImageFromGallery());

        mBtnPost.setOnClickListener(v -> {
            String title = mEtTitleText.getText().toString().trim();
            String description = mEtDescriptionText.getText().toString().trim();
            String price = mEtPriceValue.getText().toString().trim();
            if (imageUri != null && !title.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
                presenter.uploadImage(imageUri, title, description, Double.parseDouble(price));
            } else {
                showError("Please remember to complete the product information");
            }
        });
    }

    private void initViews() {
        mIvCancelButton = findViewById(R.id.iv_cancel_button);
        mEtTitleText = findViewById(R.id.et_title_text);
        mEtDescriptionText = findViewById(R.id.et_description_text);
        mEtPriceValue = findViewById(R.id.et_price_value);
        mIvAddImage = findViewById(R.id.iv_add_image);
        mTvAddImage = findViewById(R.id.tv_add_image_text);
        mRlAddImage = findViewById(R.id.rl_add_image);
        mBtnPost = findViewById(R.id.btn_post);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1) // 强制裁剪成正方形
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mIvAddImage.setImageBitmap(selectedImage);
                    mIvAddImage.setVisibility(View.VISIBLE);
                    mTvAddImage.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    showError("Failed to load image");
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showError(error.getMessage());
            }
        }
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        finish();
    }
}
