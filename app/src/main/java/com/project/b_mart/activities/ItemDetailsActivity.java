package com.project.b_mart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.b_mart.R;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.ImagePickerUtils;

public class ItemDetailsActivity extends AppCompatActivity {
    private static Item item;
    private ImageView imageView;
    private TextView tvPrice, tvPhone, tvAddress, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(item.getName());
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.edit).setVisibility(View.GONE);

        imageView = findViewById(R.id.img_view);
        tvPrice = findViewById(R.id.tv_price);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvDescription = findViewById(R.id.tv_description);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/3/2020 save to fav list
            }
        });

        fillUpDataToUI();
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
        tvPrice.setText(item.getPrice());
        tvPhone.setText(item.getPhone());
        tvAddress.setText(item.getAddress());
        tvDescription.setText(item.getDescription());
    }
}
