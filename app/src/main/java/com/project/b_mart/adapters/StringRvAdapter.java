package com.project.b_mart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StringRvAdapter extends RecyclerView.Adapter<StringRvAdapter.ViewHolder> {
    private List<String> dataSet;
    private OnListItemClickListener onListItemClickListener;

    public StringRvAdapter(List<String> dataSet, OnListItemClickListener onListItemClickListener) {
        this.dataSet = dataSet;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int index = holder.getAbsoluteAdapterPosition();

        final String s = dataSet.get(index);

        holder.tv.setText(s);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onClick(index, s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnListItemClickListener {
        void onClick(int position, String s);
    }
}
