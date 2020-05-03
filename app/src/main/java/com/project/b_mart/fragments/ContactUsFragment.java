package com.project.b_mart.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.b_mart.R;

public class ContactUsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setFavVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }
}
