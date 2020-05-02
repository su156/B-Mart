package com.project.b_mart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.project.b_mart.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        rootView.findViewById(R.id.girl_fashion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.girl_fashion), Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.boy_fashion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.boy_fashion), Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.others), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
