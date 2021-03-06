package com.project.b_mart.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.adapters.UserAdapter;
import com.project.b_mart.models.Item;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

import java.util.ArrayList;
import java.util.List;

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

        adapter = new UserAdapter(getContext(), this);
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
    }

    @Override
    public void onDeleteButtonClick(final int position, final User user) {
        user.setBlocked(true);

        Helper.showConfirmDialog(getContext(),
                getString(R.string.are_you_sure),
                getString(R.string.block_acc_and_delete_posts_warning_msg, user.getEmail()),
                // OK button callback
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteUser(position, user);
                    }
                },
                // Cancel button callback
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void fetchData() {
        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE)
                .orderByChild("blocked")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        List<User> users = User.parseUserList(dataSnapshot);

                        adapter.setDataSet(removeSystemAdmin(users));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private void deleteUser(final int position, final User user) {
        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        adapter.removeItem(position);

                        clearItemsForBlockedUser(user.getUid());
                    }
                });
    }

    private void clearItemsForBlockedUser(String uid) {
        FirebaseDatabase.getInstance().getReference(Constants.ITEM_TABLE)
                .orderByChild("sellerId")
                .equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Item> items = Item.parseItemList(dataSnapshot);

                        deleteItems(items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private List<User> removeSystemAdmin(List<User> users) {
        List<User> list = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            for (User u : users) {
                if (!u.getEmail().equals(Constants.SYSTEM_ADMIN_EMAIL)) {
                    list.add(u);
                }
            }
        }

        return list;
    }

    private void deleteItems(final List<Item> items) {
        if (items == null || items.isEmpty()) {
            Helper.dismissProgressDialog();
            return;
        }

        FirebaseDatabase.getInstance().getReference(Constants.ITEM_TABLE)
                .child(items.get(0).getId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        items.remove(0);
                        deleteItems(items);
                    }
                });
    }
}
