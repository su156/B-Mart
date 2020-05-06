package com.project.b_mart.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.project.b_mart.R;

public class Helper {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String title) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public static String[] getSubCategories(Context context, String topCategory) {
        int subCategoriesId = 0;
        if (topCategory.equals(context.getString(R.string.girl_fashion))) {
            subCategoriesId = R.array.girl_fashion_sub_categories;
        } else if (topCategory.equals(context.getString(R.string.boy_fashion))) {
            subCategoriesId = R.array.boy_fashion_sub_categories;
        } else if (topCategory.equals(context.getString(R.string.cosmetic))) {
            subCategoriesId = R.array.cosmetic_sub_categories;
        } else if (topCategory.equals(context.getString(R.string.electronic_device))) {
            subCategoriesId = R.array.electronic_device_sub_categories;
        } else if (topCategory.equals(context.getString(R.string.others))) {
            subCategoriesId = R.array.others_sub_categories;
        }
        return context.getResources().getStringArray(subCategoriesId);
    }
}
