package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

public class ProfileDetailsActivity extends AppCompatActivity {
    private static String uid;

    private User user;

    private ImageView imgProfile;
    private TextView tvName, tvPhone, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        if (TextUtils.isEmpty(uid)) {
            finish();
            return;
        }

        imgProfile = findViewById(R.id.img_profile);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.img_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetailsActivity.setBm(BitmapUtils.base64StringToBitmap(user.getProfileImageStr()));
                startActivity(new Intent(ProfileDetailsActivity.this, PhotoDetailsActivity.class));
            }
        });

        fetchUserData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        uid = null;
    }

    public static void setUid(String uid) {
        ProfileDetailsActivity.uid = uid;
    }

    private void fetchUserData() {
        Helper.showProgressDialog(this, "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            fillUpDataToUI(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private void fillUpDataToUI(User user) {
        imgProfile.setImageBitmap(BitmapUtils.base64StringToBitmap(user.getProfileImageStr()));
        tvName.setText(user.getName());
        tvAddress.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
    }
}
