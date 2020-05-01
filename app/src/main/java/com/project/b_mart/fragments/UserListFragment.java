package com.project.b_mart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.adapters.UserAdapter;
import com.project.b_mart.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment implements UserAdapter.OnListItemClickListener, TextWatcher {
    private UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        rootView.findViewById(R.id.spn_category).setVisibility(View.GONE);

        EditText edtSearch = rootView.findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(this);

        RecyclerView rv = rootView.findViewById(R.id.rv);

        adapter = new UserAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        fetchData();

        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        adapter.getFilter().filter(s);
    }

    @Override
    public void onClick(int position, User user) {
        Toast.makeText(getContext(), user.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteButtonClick(int position, User user) {
        Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        adapter.setDataSet(getUsers());
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUid("Uid " + i);
            user.setName("Name " + i);
            users.add(user);
        }

        return users;
    }
}
