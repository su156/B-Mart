package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.b_mart.R;
import com.project.b_mart.activities.FeedbackDetailsActivity;
import com.project.b_mart.adapters.FeedbackRvAdapter;
import com.project.b_mart.models.Feedback;
import com.project.b_mart.utils.Helper;

public class FeedbackFragment extends BaseFragment implements FeedbackRvAdapter.OnListItemClickListener {
    private FeedbackRvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setFavVisibility(View.GONE);

        View view = inflater.inflate(R.layout.layout_rv, container, false);

        RecyclerView rv = view.findViewById(R.id.rv);

        adapter = new FeedbackRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        adapter.setDataSet(Helper.getFeedbackList());

        return view;
    }

    @Override
    public void onClick(int position, Feedback feedback) {
        FeedbackDetailsActivity.setFeedback(feedback);
        startActivity(new Intent(getContext(), FeedbackDetailsActivity.class));
    }

    @Override
    public void onLongClick(int position, Feedback feedback) {
    }

    public void onNewFeedback(Feedback feedback) {
        adapter.addItem(feedback);
    }
}
