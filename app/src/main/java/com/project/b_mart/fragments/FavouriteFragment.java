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

public class FavouriteFragment extends Fragment implements ItemRvAdapter.OnListItemClickListener {
    private ItemRvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_rv, container, false);

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
        Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int position, Item item) {
        Toast.makeText(getContext(), item.getPrice(), Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        adapter.setDataSet(ShoppingFragment.generateItems());
    }
}
