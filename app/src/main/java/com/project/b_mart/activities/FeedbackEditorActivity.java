package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.b_mart.R;
import com.project.b_mart.models.Feedback;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;
import com.project.b_mart.utils.ImagePickerUtils;

public class FeedbackEditorActivity extends AppCompatActivity {
    private static String creatorId;
    private static String creatorEmail;
    private static String recipientId;
    private Feedback feedback;

    private ImageView img;
    private EditText edtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_editor);

        if (TextUtils.isEmpty(creatorId)) {
            Toast.makeText(this, "Creator's Id is required!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (TextUtils.isEmpty(creatorEmail)) {
            Toast.makeText(this, "Creator's email is required!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (TextUtils.isEmpty(recipientId)) {
            Toast.makeText(this, "Recipient's Id is required!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        feedback = new Feedback();
        feedback.setCreatorId(creatorId);
        feedback.setCreatorEmail(creatorEmail);
        feedback.setRecipientId(recipientId);

        img = findViewById(R.id.img_screen_shot);
        edtFeedback = findViewById(R.id.edt_feedback);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.img_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        findViewById(R.id.layout_screen_shot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.pickImage(FeedbackEditorActivity.this, ImagePickerUtils.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        creatorId = null;
        creatorEmail = null;
        recipientId = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePickerUtils.REQUEST_CODE) {
                Bitmap bm = BitmapUtils.resize(ImagePickerUtils.parseBitmap(this, data));
                img.setImageBitmap(bm);
                feedback.setPhotoString(BitmapUtils.bitmapToBase64String(bm));
            }
        }
    }

    public static void setCreatorId(String creatorId) {
        FeedbackEditorActivity.creatorId = creatorId;
    }

    public static void setCreatorEmail(String creatorEmail) {
        FeedbackEditorActivity.creatorEmail = creatorEmail;
    }

    public static void setRecipientId(String recipientId) {
        FeedbackEditorActivity.recipientId = recipientId;
    }

    private void send() {
        String feedbackMessage = edtFeedback.getText().toString();
        if (TextUtils.isEmpty(feedbackMessage)) {
            Toast.makeText(this, "Please enter feedback!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference feedbackTable = FirebaseDatabase.getInstance().getReference(Constants.FEEDBACK_TABLE);
        feedback.setFeedbackMessage(feedbackMessage);

        if (TextUtils.isEmpty(feedback.getId())) {
            feedback.setId(feedbackTable.push().getKey());
        }

        Helper.showProgressDialog(this, "Loading...");
        feedbackTable.child(feedback.getId())
                .setValue(feedback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Helper.dismissProgressDialog();
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(FeedbackEditorActivity.this, "Fail to send feedback!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
