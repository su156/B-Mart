package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.b_mart.R;
import com.project.b_mart.activities.ItemEditorActivity;
import com.project.b_mart.activities.ProfileEditorActivity;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.Helper;

public class ProfileFragment extends BaseFragment implements ItemRvAdapter.OnListItemClickListener {
    private ItemRvAdapter adapter;

    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.VISIBLE);

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            TextView tvName = rootView.findViewById(R.id.tv_name);
            tvName.setText(user.getDisplayName());

            TextView tvEmail = rootView.findViewById(R.id.tv_email);
            tvEmail.setText(user.getEmail());

            TextView tvPhone = rootView.findViewById(R.id.tv_phone);
            tvPhone.setText(user.getPhoneNumber());
        }

        RecyclerView rv = rootView.findViewById(R.id.rv);

        adapter = new ItemRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        rootView.findViewById(R.id.layout_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileEditorActivity.class));
            }
        });

        fetchData();

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
}
