package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.activities.ItemDetailsActivity;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends BaseFragment implements ItemRvAdapter.OnListItemClickListener,
        TextWatcher {
    private Spinner spnSubCategory;
    private EditText edtSearch;
    private ItemRvAdapter adapter;
    private String topCategory;
    private String subCategory;

    private boolean fetchItems;
    private boolean isAuto;
    private List<Item> items;

    public ShoppingFragment(String topCategory, String subCategory) {
        this.topCategory = topCategory;
        this.subCategory = subCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.VISIBLE);

        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        Spinner spnCategory = rootView.findViewById(R.id.spn_category);
        spnSubCategory = rootView.findViewById(R.id.spn_sub_category);
        edtSearch = rootView.findViewById(R.id.edt_search);
        RecyclerView rv = rootView.findViewById(R.id.rv);

        final String[] topCategories = getResources().getStringArray(R.array.category_array);

        if (getContext() != null) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1,
                    topCategories);
            spnCategory.setAdapter(spnAdapter);

            if (!TextUtils.isEmpty(topCategory)) {
                for (int i = 0; i < topCategories.length; i++) {
                    if (topCategory.equals(topCategories[i])) {
                        spnCategory.setSelection(i);
                        break;
                    }
                }
            } else {
                topCategory = topCategories[0];
            }
        }

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchItems = true;

                topCategory = topCategories[position];

                onTopCategorySelected(topCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategory = (String) spnSubCategory.getItemAtPosition(position);
                isAuto = true;
                edtSearch.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        edtSearch.addTextChangedListener(this);

        adapter = new ItemRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

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
        if (isAuto) {
            if (fetchItems) {
                fetchItems = false;
                fetchItems();
            } else {
                filterItems();
            }
        } else {
            adapter.getFilter().filter(s);
        }
    }

    @Override
    public void onClick(int position, Item item) {
        ItemDetailsActivity.setItem(item);
        startActivity(new Intent(getContext(), ItemDetailsActivity.class));
    }

    @Override
    public void onLongClick(int position, Item item) {
        Toast.makeText(getContext(), item.getPrice(), Toast.LENGTH_SHORT).show();
    }

    private void onTopCategorySelected(String topCategory) {
        if (getContext() == null) {
            return;
        }

        String[] subCategories = TextUtils.isEmpty(topCategory)
                ? new String[]{}
                : Helper.getSubCategories(getContext(), topCategory);
        ArrayAdapter<String> spnSubAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                subCategories);
        spnSubCategory.setAdapter(spnSubAdapter);

        if (!TextUtils.isEmpty(subCategory)) {
            for (int i = 0; i < subCategories.length; i++) {
                if (subCategory.equals(subCategories[i])) {
                    spnSubCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    static List<Item> generateItems() {
        List<Item> dataSet = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Item item = new Item();
            item.setName("Name " + i);
            item.setPhone("Phone " + i);
            item.setPrice("$" + i);
            item.setAddress("Address " + i);
            item.setDescription("Description " + i);
            dataSet.add(item);
        }

        return dataSet;
    }

    private void fetchItems() {
        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.ITEM_TABLE)
                .orderByChild("category").equalTo(topCategory)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        items = Item.parseItemList(dataSnapshot);

                        filterItems();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void filterItems() {
        List<Item> filteredItems = new ArrayList<>();
        if (subCategory.equals(getString(R.string.all))) {
            filteredItems.addAll(items);
        } else {
            for (Item i : items) {
                if (subCategory.equals(i.getSubCategory())) {
                    filteredItems.add(i);
                }
            }
        }
        adapter.setDataSet(filteredItems);
    }
}
