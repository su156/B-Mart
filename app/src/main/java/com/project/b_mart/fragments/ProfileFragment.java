package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.activities.ItemEditorActivity;
import com.project.b_mart.activities.ProfileEditorActivity;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

public class ProfileFragment extends BaseFragment implements ItemRvAdapter.OnListItemClickListener {
    private ItemRvAdapter adapter;

    private FirebaseUser user;
    private User userData;

    private ImageView imgProfile;
    private TextView tvName, tvEmail, tvPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.VISIBLE);

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        imgProfile = rootView.findViewById(R.id.img_profile);
        tvName = rootView.findViewById(R.id.tv_name);
        tvEmail = rootView.findViewById(R.id.tv_email);
        tvPhone = rootView.findViewById(R.id.tv_phone);

        RecyclerView rv = rootView.findViewById(R.id.rv);

        adapter = new ItemRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        rootView.findViewById(R.id.layout_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditorActivity.setUser(userData);
                startActivity(new Intent(getContext(), ProfileEditorActivity.class));
            }
        });

        if (!Helper.isIsSystemAdmin()) {
            fetchData();
        }

        fetchUserData();

        return rootView;
    }

    @Override
    public void onClick(int position, Item item) {
        ItemEditorActivity.setItem(item);
        startActivity(new Intent(getContext(), ItemEditorActivity.class));
    }

    @Override
    public void onLongClick(int position, Item item) {
        Toast.makeText(getContext(), item.getPrice(), Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        adapter.setDataSet(Helper.getItemListBySellerId(user.getUid()));
    }

    private void fetchUserData() {
        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        userData = dataSnapshot.getValue(User.class);

                        fillUpDataToUI();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private void fillUpDataToUI() {
        if (!TextUtils.isEmpty(userData.getProfileImageStr())) {
            imgProfile.setImageBitmap(BitmapUtils.base64StringToBitmap(userData.getProfileImageStr()));
        }
        tvName.setText(userData.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(userData.getPhone());
    }
}
