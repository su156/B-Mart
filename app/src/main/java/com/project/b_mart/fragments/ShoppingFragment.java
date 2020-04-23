package com.project.b_mart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment implements ItemRvAdapter.OnListItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        RecyclerView rv = rootView.findViewById(R.id.rv);

        ItemRvAdapter adapter = new ItemRvAdapter(this);
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

    private List<Item> generateItems() {
        List<Item> dataSet = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Item item = new Item();
            item.setName("Name " + i);
            item.setPhone("Phone " + i);
            item.setPrice("$" + i);
            item.setAddress("Address " + i);
            dataSet.add(item);
        }

        return dataSet;
    }
}
