package com.project.b_mart.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<User> dataSet;
    private List<User> filteredDataSet;
    private OnListItemClickListener onListItemClickListener;

    public UserAdapter(Context context, OnListItemClickListener onListItemClickListener) {
        this.context = context;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int index = holder.getAbsoluteAdapterPosition();
        final User user = filteredDataSet.get(index);

        if (!TextUtils.isEmpty(user.getProfileImageStr())) {
            holder.imgProfile.setImageBitmap(BitmapUtils.base64StringToBitmap(user.getProfileImageStr()));
        } else {
            holder.imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_outline_black_24dp));
        }
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onClick(index, user);
            }
        });

        holder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onDeleteButtonClick(index, user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataSet == null ? 0 : filteredDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<User> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (User user : dataSet) {
                        if (user.getName().toLowerCase().contains(searchTerm)) {
                            filteredList.add(user);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<User>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    public void setDataSet(List<User> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProfile;
        private TextView tvName, tvEmail;
        private ImageButton imgBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.img_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            imgBtn = itemView.findViewById(R.id.img_btn_delete);
        }
    }

    public interface OnListItemClickListener {
        void onClick(int position, User user);

        void onDeleteButtonClick(int position, User user);
    }
}
