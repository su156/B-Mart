package com.project.b_mart.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.adapters.StringRvAdapter;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private OnSubCategorySelectedListener onSubCategorySelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.VISIBLE);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        rootView.findViewById(R.id.girl_fashion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubCategoryChooserDialog(getString(R.string.girl_fashion),
                        Arrays.asList(getResources().getStringArray(R.array.girl_fashion_sub_categories)));
            }
        });

        rootView.findViewById(R.id.boy_fashion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubCategoryChooserDialog(getString(R.string.boy_fashion),
                        Arrays.asList(getResources().getStringArray(R.array.boy_fashion_sub_categories)));
            }
        });

        rootView.findViewById(R.id.cosmetic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubCategoryChooserDialog(getString(R.string.cosmetic),
                        Arrays.asList(getResources().getStringArray(R.array.cosmetic_sub_categories)));
            }
        });

        rootView.findViewById(R.id.electronic_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubCategoryChooserDialog(getString(R.string.electronic_device),
                        Arrays.asList(getResources().getStringArray(R.array.electronic_device_sub_categories)));
            }
        });

        rootView.findViewById(R.id.others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubCategorySelectedListener.onSubCategorySelected(getString(R.string.others), getString(R.string.all));
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnSubCategorySelectedListener) {
            onSubCategorySelectedListener = (OnSubCategorySelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnSubCategorySelectedListener!");
        }
    }

    private void showSubCategoryChooserDialog(final String topCategory, List<String> subCategories) {
        if (getContext() == null) {
            return;
        }

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sub_categories_chooser);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        RecyclerView rv = dialog.findViewById(R.id.rv);

        StringRvAdapter adapter = new StringRvAdapter(subCategories, new StringRvAdapter.OnListItemClickListener() {
            @Override
            public void onClick(int position, String s) {
                dialog.dismiss();
                onSubCategorySelectedListener.onSubCategorySelected(topCategory, s);
            }
        });

        rv.setAdapter(adapter);

        dialog.show();
    }

    public interface OnSubCategorySelectedListener {
        void onSubCategorySelected(String topCategory, String subCategory);
    }
}
