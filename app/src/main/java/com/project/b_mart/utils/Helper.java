package com.project.b_mart.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.project.b_mart.R;

import java.util.HashSet;
import java.util.Set;

public class Helper {
    private static Set<String> favList = new HashSet<>();
    private static ProgressDialog progressDialog;

    public static Set<String> getFavList() {
        return favList;
    }

    public static void setFavList(Set<String> list) {
        favList = list;
    }

    public static Set<String> parseStringList(DataSnapshot dataSnapshot) {
        Set<String> list = new HashSet<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            list.add(snapshot.getValue(String.class));
        }
        return list;
    }

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
