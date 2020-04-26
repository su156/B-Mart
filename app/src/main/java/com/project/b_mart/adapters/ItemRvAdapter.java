package com.project.b_mart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemRvAdapter extends RecyclerView.Adapter<ItemRvAdapter.ViewHolder>
        implements Filterable {
    private List<Item> dataSet;
    private List<Item> filterDataSet;
    private OnListItemClickListener onListItemClickListener;

    public ItemRvAdapter(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setDataSet(List<Item> dataSet) {
        this.dataSet = dataSet;
        this.filterDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_shopping_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int index = holder.getAbsoluteAdapterPosition();
        final Item item = filterDataSet.get(index);

        holder.img.setImageBitmap(BitmapUtils.base64StringToBitmap(item.getPhotoString()));
        holder.tvItem.setText(item.getName());
        holder.tvPhone.setText(item.getPhone());
        holder.tvPrice.setText(item.getPrice());
        holder.tvAddress.setText(item.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onClick(index, item);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onListItemClickListener.onLongClick(index, item);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<Item> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (Item item : dataSet) {
                        if (item.getName().toLowerCase().contains(searchTerm) ||
                                item.getPrice().toLowerCase().contains(searchTerm) ||
                                item.getPhone().toLowerCase().contains(searchTerm) ||
                                item.getAddress().toLowerCase().contains(searchTerm)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterDataSet = (List<Item>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, imgFav;
        private TextView tvItem, tvPhone, tvPrice, tvAddress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            imgFav = itemView.findViewById(R.id.img_fav);
            tvItem = itemView.findViewById(R.id.tv_item);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }
    }

    public interface OnListItemClickListener {
        void onClick(int position, Item item);

        void onLongClick(int position, Item item);
    }
}
