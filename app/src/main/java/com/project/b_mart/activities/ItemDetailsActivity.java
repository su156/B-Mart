package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.b_mart.R;
import com.project.b_mart.models.Item;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;

public class ItemDetailsActivity extends AppCompatActivity {
    private static Item item;
    private ImageView imageView;
    private TextView tvTopCategory, tvSubCategory, tvStatus, tvPrice, tvPhone, tvAddress, tvDescription;
    private FloatingActionButton fab;

    private DatabaseReference favTable;

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
        tvTopCategory = findViewById(R.id.tv_top_category);
        tvSubCategory = findViewById(R.id.tv_sub_category);
        tvStatus = findViewById(R.id.tv_status);
        tvPrice = findViewById(R.id.tv_price);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvDescription = findViewById(R.id.tv_description);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.getFavList().contains(item.getId())) {
                    removeFromFavList();
                } else {
                    addToFavList();
                }
            }
        });

        changeFavIconAndFavList(Helper.getFavList().contains(item.getId()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Fail to get seller id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        favTable = FirebaseDatabase.getInstance().getReference(Constants.FAV_TABLE)
                .child(user.getUid()).child(item.getId());

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
        tvTopCategory.setText(item.getCategory());
        tvSubCategory.setText(item.getSubCategory());
        tvStatus.setText(item.isNew() ? getString(R.string.new_) : getString(R.string.old));
        tvPrice.setText(item.getPrice());
        tvPhone.setText(item.getPhone());
        tvAddress.setText(item.getAddress());
        tvDescription.setText(item.getDescription());
    }

    private void removeFromFavList() {
        favTable.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        changeFavIconAndFavList(!task.isSuccessful());
                    }
                });
    }

    private void addToFavList() {
        favTable.setValue(item.getId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        changeFavIconAndFavList(task.isSuccessful());
                    }
                });
    }

    private void changeFavIconAndFavList(boolean isFav) {
        fab.setImageDrawable(getResources().getDrawable(isFav
                ? R.drawable.ic_favorite_black_24dp
                : R.drawable.ic_favorite_border_black_24dp));

        if (isFav) {
            Helper.getFavList().add(item.getId());
        } else {
            Helper.getFavList().remove(item.getId());
        }
    }
}
