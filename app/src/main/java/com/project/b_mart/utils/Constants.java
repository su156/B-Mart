package com.project.b_mart.utils;

import android.Manifest;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int SIGN_UP_REQUEST_CODE = 1;
    public static final int PERMISSIONS_REQUEST_CODE = 2;
    public static final int REQUEST_CODE_LAT_AND_LONG = 3;

    public static final String RESULT_LAT_AND_LONG = "RESULT_LAT_AND_LONG";
    public static final String ITEM_TABLE = "item-table";
    public static final String FAV_TABLE = "fav-table";

    public static final List<String> PERMISSIONS = Arrays.asList(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    );
}
