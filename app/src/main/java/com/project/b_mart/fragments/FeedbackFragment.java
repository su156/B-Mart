package com.project.b_mart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.activities.FeedbackDetailsActivity;
import com.project.b_mart.adapters.FeedbackRvAdapter;
import com.project.b_mart.models.Feedback;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

import java.util.List;

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

        fetchData();

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

    private void fetchData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Fail to get current login user data!", Toast.LENGTH_SHORT).show();
            return;
        }

        Helper.showProgressDialog(getContext(), "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.FEEDBACK_TABLE)
                .orderByChild("recipientId")
                .equalTo(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        List<Feedback> feedbackList = Feedback.parseFeedbackList(dataSnapshot);

                        adapter.setDataSet(feedbackList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }
}
