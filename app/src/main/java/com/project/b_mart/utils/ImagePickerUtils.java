package com.project.b_mart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class ImagePickerUtils {

    public static final int REQUEST_CODE = 2;

    public static void pickImage(Activity activity, int requestCode) {
        activity.startActivityForResult(
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                requestCode);
    }

    public static Bitmap parseBitmap(Context context, Intent data) {
        try {
            Uri uri = data.getData();
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
