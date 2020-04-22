package com.project.b_mart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.project.b_mart.R;
import com.project.b_mart.models.NavigationItem;

public class DrawerItemCustomAdapter extends ArrayAdapter<NavigationItem> {

    private final Context mContext;
    private final int layoutResourceId;
    private final NavigationItem[] data;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, NavigationItem[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = LayoutInflater.from(mContext)
                .inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem.findViewById(R.id.textViewName);

        NavigationItem navigationItem = data[position];

        imageViewIcon.setImageResource(navigationItem.getIcon());
        textViewName.setText(navigationItem.getName());

        return listItem;
    }
}
