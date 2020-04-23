package com.project.b_mart.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {
    private static final double MAX_IMAGE_SIZE = 2000000; // 2 MB

    public static String bitmapToBase64String(Bitmap bm) {
        if (bm == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT).replaceAll("\n", "");
    }

    public static Bitmap base64StringToBitmap(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);
        return blob.toByteArray();
    }

    public static Bitmap resize(Bitmap bitmap) {
        return resize(bitmapToByteArray(bitmap));
    }

    private static Bitmap resize(byte[] data) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, scaleOptions);
        return scaleBitmapSize(data, calculateScaleToCompressBitmap(scaleOptions));
    }

    private static Bitmap scaleBitmapSize(byte[] data, int scale) {
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, outOptions);

        if (b.getAllocationByteCount() > MAX_IMAGE_SIZE) {
            return scaleBitmapSize(data, ++scale);
        }

        return b;
    }

    private static int calculateScaleToCompressBitmap(BitmapFactory.Options options) {
        final int reqWidth = 720;
        final int reqHeight = 405;
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
