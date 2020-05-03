package com.project.b_mart.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.b_mart.R;

public class SharedPreferencesUtils {
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }
}
