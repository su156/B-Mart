package com.project.b_mart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.adapters.UserAdapter;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

public class UserListFragment extends BaseFragment implements UserAdapter.OnListItemClickListener, TextWatcher {
    private UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.GONE);

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        rootView.findViewById(R.id.spn_category).setVisibility(View.GONE);
        rootView.findViewById(R.id.spn_sub_category).setVisibility(View.GONE);

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
        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        adapter.setDataSet(User.parseUserList(dataSnapshot));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }
}
