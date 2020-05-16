package com.project.b_mart.fragments;

import androidx.fragment.app.Fragment;

import com.project.b_mart.activities.MainActivity;

class BaseFragment extends Fragment {

    void setFavVisibility(int visibility) {
        if (getContext() != null) {
            ((MainActivity) getContext()).setFavVisibility(visibility);
        }
    }
}
