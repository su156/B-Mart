package com.project.b_mart.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.models.Feedback;

import java.util.List;

public class FeedbackRvAdapter extends RecyclerView.Adapter<FeedbackRvAdapter.ViewHolder> {
    private List<Feedback> dataSet;
    private OnListItemClickListener onListItemClickListener;

    public FeedbackRvAdapter(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setDataSet(List<Feedback> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_feedback_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int index = holder.getAbsoluteAdapterPosition();
        final Feedback feedback = dataSet.get(index);

        holder.tvEmail.setText(feedback.getCreatorEmail());
        holder.tvFeedbackMessage.setText(feedback.getFeedbackMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onClick(index, feedback);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onListItemClickListener.onClick(index, feedback);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmail, tvFeedbackMessage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.tv_email);
            tvFeedbackMessage = itemView.findViewById(R.id.tv_feedback_message);
        }
    }

    public interface OnListItemClickListener {
        void onClick(int position, Feedback feedback);

        void onLongClick(int position, Feedback feedback);
    }
}
