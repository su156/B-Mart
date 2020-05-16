package com.project.b_mart.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
    private String id;
    private String creatorId;
    private String creatorEmail;
    private String recipientId;
    private String photoString;
    private String feedbackMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public static List<Feedback> parseFeedbackList(DataSnapshot dataSnapshot) {
        List<Feedback> feedbackList = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            feedbackList.add(snapshot.getValue(Feedback.class));
        }
        return feedbackList;
    }
}
