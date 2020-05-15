package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.project.b_mart.R;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;
import com.project.b_mart.utils.ImagePickerUtils;

public class ProfileEditorActivity extends AppCompatActivity {
    private static User user;

    private ImageView imgProfile;
    private EditText edtName, edtPhone, edtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        if (user == null) {
            Toast.makeText(this, "No user data to show", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imgProfile = findViewById(R.id.img_profile);
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetailsActivity.setBm(BitmapUtils.base64StringToBitmap(user.getProfileImageStr()));
                startActivity(new Intent(ProfileEditorActivity.this, PhotoDetailsActivity.class));
            }
        });

        findViewById(R.id.img_pick_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.pickImage(ProfileEditorActivity.this, ImagePickerUtils.REQUEST_CODE);
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        fillUpDataToUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == ImagePickerUtils.REQUEST_CODE) {
            Bitmap bm = BitmapUtils.resize(ImagePickerUtils.parseBitmap(this, data));
            imgProfile.setImageBitmap(bm);
            user.setProfileImageStr(BitmapUtils.bitmapToBase64String(bm));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        user = null;
    }

    public static void setUser(User user) {
        ProfileEditorActivity.user = user;
    }

    private void fillUpDataToUI() {
        imgProfile.setImageBitmap(BitmapUtils.base64StringToBitmap(user.getProfileImageStr()));
        edtName.setText(user.getName());
        edtPhone.setText(user.getPhone());
        edtAddress.setText(user.getAddress());
    }

    private void getDataFromUI() {
        user.setName(edtName.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setAddress(edtAddress.getText().toString());
    }

    private void saveUser() {
        getDataFromUI();

        Helper.showProgressDialog(this, "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Helper.dismissProgressDialog();
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(ProfileEditorActivity.this, "Fail to save! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
