package com.project.b_mart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment implements ItemRvAdapter.OnListItemClickListener,
        TextWatcher {
    private Spinner spnCategory;
    private EditText edtSearch;
    private ItemRvAdapter adapter;
    private String topCategory;
    private String subCategory;

    public ShoppingFragment(String topCategory, String subCategory) {
        this.topCategory = topCategory;
        this.subCategory = subCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        spnCategory = rootView.findViewById(R.id.spn_category);
        edtSearch = rootView.findViewById(R.id.edt_search);
        RecyclerView rv = rootView.findViewById(R.id.rv);

        if (getContext() != null) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.category_array));
            spnCategory.setAdapter(spnAdapter);
        }
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

        adapter.setDataSet(generateItems());

        return rootView;
    }

    @Override
    public void onClick(int position, Item item) {
        Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int position, Item item) {
        Toast.makeText(getContext(), item.getPrice(), Toast.LENGTH_SHORT).show();
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
}
