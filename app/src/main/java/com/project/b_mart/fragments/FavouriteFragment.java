package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.activities.ItemDetailsActivity;
import com.project.b_mart.adapters.ItemRvAdapter;
import com.project.b_mart.models.Item;

public class FavouriteFragment extends BaseFragment implements ItemRvAdapter.OnListItemClickListener {
    private ItemRvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setFavVisibility(View.GONE);

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
        ItemDetailsActivity.setItem(item);
        startActivity(new Intent(getContext(), ItemDetailsActivity.class));
    }

    @Override
    public void onLongClick(int position, Item item) {
        Toast.makeText(getContext(), item.getPrice(), Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        adapter.setDataSet(ShoppingFragment.generateItems());
    }
}
