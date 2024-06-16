package com.dylanlxlx.campuslink.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.squareup.picasso.Transformation;

public class CropTransformation implements Transformation {

    private final int targetWidth;
    private final int targetHeight;

    public CropTransformation(int targetWidth, int targetHeight) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        float aspectRatio = 350f / 270f;

        int newWidth;
        int newHeight;

        if (width > height * aspectRatio) {
            newHeight = height;
            newWidth = (int) (height * aspectRatio);
        } else {
            newWidth = width;
            newHeight = (int) (width / aspectRatio);
        }

        int x = (width - newWidth) / 2;
        int y = (height - newHeight) / 2;

        Bitmap scaledBitmap = Bitmap.createBitmap(source, x, y, newWidth, newHeight);
        if (scaledBitmap != source) {
            source.recycle();
        }

        Bitmap finalBitmap = Bitmap.createScaledBitmap(scaledBitmap, targetWidth, targetHeight, false);
        if (scaledBitmap != finalBitmap) {
            scaledBitmap.recycle();
        }

        return finalBitmap;
    }

    @Override
    public String key() {
        return "cropTransformation(width=" + targetWidth + ", height=" + targetHeight + ")";
    }
}
