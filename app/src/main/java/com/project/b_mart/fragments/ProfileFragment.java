package com.project.b_mart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.b_mart.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            TextView tvName = rootView.findViewById(R.id.tv_name);
            tvName.setText(user.getDisplayName());

            TextView tvEmail = rootView.findViewById(R.id.tv_email);
            tvEmail.setText(user.getEmail());

            TextView tvPhone = rootView.findViewById(R.id.tv_phone);
            tvPhone.setText(user.getPhoneNumber());
        }

        return rootView;
    }
}
