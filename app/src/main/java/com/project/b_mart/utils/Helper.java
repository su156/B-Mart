package com.project.b_mart.utils;

import android.content.Context;

import com.project.b_mart.R;

public class Helper {

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
