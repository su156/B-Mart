package com.project.b_mart.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.firebase.database.DataSnapshot;
import com.project.b_mart.R;
import com.project.b_mart.models.Feedback;
import com.project.b_mart.models.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Helper {
    private static Set<String> favList = new HashSet<>();
    private static List<Item> itemList = new ArrayList<>();
    private static List<Feedback> feedbackList = new ArrayList<>();
    private static ProgressDialog progressDialog;
    private static boolean isSystemAdmin;

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

    public static void setItemList(List<Item> itemList) {
        Helper.itemList = itemList;
    }

    public static List<Item> getFavItemList() {
        List<Item> list = new ArrayList<>();

        for (Item item : itemList) {
            for (String itemId : favList) {
                if (item.getId().equals(itemId)) {
                    list.add(item);
                }
            }
        }

        return list;
    }

    public static List<Item> getItemListByTopCategory(String topCategory) {
        List<Item> list = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getCategory().equals(topCategory)) {
                list.add(item);
            }
        }

        return list;
    }

    public static List<Item> getItemListByTopCategoryAndSubCategory(String topCategory, String subCategory) {
        List<Item> list = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getCategory().equals(topCategory) && item.getSubCategory().equals(subCategory)) {
                list.add(item);
            }
        }

        return list;
    }

    public static List<Item> getItemListBySellerId(String sellerId) {
        List<Item> list = new ArrayList<>();

        for (Item item : itemList) {
            if (item.getSellerId().equals(sellerId)) {
                list.add(item);
            }
        }

        return list;
    }

    public static List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public static void setFeedbackList(List<Feedback> feedbackList) {
        Helper.feedbackList = feedbackList;
    }

    public static void addFeedback(Feedback feedback) {
        if (feedbackList == null || feedbackList.isEmpty()) {
            feedbackList = new ArrayList<>();
        }

        feedbackList.add(feedback);
    }

    public static boolean isContainsInFeedbackList(Feedback feedback) {
        if (feedbackList == null || feedbackList.isEmpty()) {
            return false;
        }

        for (Feedback f : feedbackList) {
            if (f.getId().equals(feedback.getId())) {
                return true;
            }
        }

        return false;
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

    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener onOk, DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, onOk);
        if (onCancel != null) {
            builder.setNegativeButton(R.string.cancel, onCancel);
        }
        builder.create().show();
    }

    public static boolean isIsSystemAdmin() {
        return isSystemAdmin;
    }

    public static void setIsSystemAdmin(boolean isSystemAdmin) {
        Helper.isSystemAdmin = isSystemAdmin;
    }
}
