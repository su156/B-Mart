package com.project.b_mart.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.b_mart.R;

public class LogoutFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.GONE);

        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Logging Out...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    closeApp();
                }
            });
        } else {
            progressDialog.dismiss();
            closeApp();
        }

        return rootView;
    }

    private void closeApp() {
        if (getActivity() != null) {
            getActivity().finishAffinity();
        }
    }
}
