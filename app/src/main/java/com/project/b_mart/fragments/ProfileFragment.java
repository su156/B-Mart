package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.b_mart.R;
import com.project.b_mart.activities.ItemEditorActivity;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;

public class ProfileFragment extends Fragment implements ItemRvAdapter.OnListItemClickListener {
    private ItemRvAdapter adapter;

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

        RecyclerView rv = rootView.findViewById(R.id.rv);

        adapter = new ItemRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

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
        adapter.setDataSet(ShoppingFragment.generateItems());
    }
}
