package com.project.b_mart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.b_mart.R;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.ImagePickerUtils;

public class ItemEditorActivity extends AppCompatActivity {
    private static Item item;
    private ImageView imageView;
    private EditText edtName, edtPrice, edtPhone, edtAddress, edtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = findViewById(R.id.img_view);
        edtName = findViewById(R.id.edt_item);
        edtPrice = findViewById(R.id.edt_price);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        edtDescription = findViewById(R.id.edt_description);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.pickImage(ItemEditorActivity.this, ImagePickerUtils.REQUEST_CODE);
            }
        });

        if (item == null) {
            item = new Item();
        } else {
            fillUpDataToUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == ImagePickerUtils.REQUEST_CODE) {
            Bitmap bm = BitmapUtils.resize(ImagePickerUtils.parseBitmap(this, data));
            imageView.setImageBitmap(bm);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        item = null;
    }

    public static void setItem(Item i) {
        item = i;
    }

    private void fillUpDataToUI() {
        imageView.setImageBitmap(BitmapUtils.base64StringToBitmap(item.getPhotoString()));
        edtName.setText(item.getName());
        edtPrice.setText(item.getPrice());
        edtPhone.setText(item.getPhone());
        edtAddress.setText(item.getAddress());
        edtDescription.setText(item.getDescription());
    }
}
