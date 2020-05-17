package com.project.b_mart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.b_mart.R;
import com.project.b_mart.models.Feedback;
import com.project.b_mart.utils.BitmapUtils;

public class FeedbackDetailsActivity extends AppCompatActivity {
    private static Feedback feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        if (feedback == null) {
            Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView img = findViewById(R.id.img_screen_shot);
        TextView tvCreatorEmail = findViewById(R.id.tv_creator_email);
        TextView tvFeedbackMessage = findViewById(R.id.tv_feedback);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!TextUtils.isEmpty(feedback.getPhotoString())) {
            img.setVisibility(View.VISIBLE);
            img.setImageBitmap(BitmapUtils.base64StringToBitmap(feedback.getPhotoString()));

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoDetailsActivity.setBm(BitmapUtils.base64StringToBitmap(feedback.getPhotoString()));
                    startActivity(new Intent(FeedbackDetailsActivity.this, PhotoDetailsActivity.class));
                }
            });
        }

        tvCreatorEmail.setText(feedback.getCreatorEmail());
        tvFeedbackMessage.setText(feedback.getFeedbackMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        feedback = null;
    }

    public static void setFeedback(Feedback feedback) {
        FeedbackDetailsActivity.feedback = feedback;
    }
}
