package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.models.Item;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.BitmapUtils;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;
import com.project.b_mart.utils.ImagePickerUtils;

public class ItemEditorActivity extends AppCompatActivity implements LocationListener {
    private static Item item;
    private String topCategory;
    private String subCategory;

    private FirebaseUser user;
    private User userData;
    private LocationManager locationManager;

    private ImageView imageView;
    private RadioButton rdbNew;
    private Spinner spnSubCategory;
    private EditText edtName, edtPrice, edtPhone, edtAddress, edtDescription;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Fail to get current user data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (item != null) {
            topCategory = item.getCategory();
            subCategory = item.getSubCategory();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.view).setVisibility(View.GONE);

        imageView = findViewById(R.id.img_view);
        rdbNew = findViewById(R.id.rdb_new);
        Spinner spnCategory = findViewById(R.id.spn_category);
        spnSubCategory = findViewById(R.id.spn_sub_category);
        edtName = findViewById(R.id.edt_item);
        edtPrice = findViewById(R.id.edt_price);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        edtDescription = findViewById(R.id.edt_description);
        tvLocation = findViewById(R.id.tv_location);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.pickImage(ItemEditorActivity.this, ImagePickerUtils.REQUEST_CODE);
            }
        });

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        final String[] topCategories = getResources().getStringArray(R.array.category_array);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                topCategories);
        spnCategory.setAdapter(spnAdapter);

        if (!TextUtils.isEmpty(topCategory)) {
            for (int i = 0; i < topCategories.length; i++) {
                if (topCategory.equals(topCategories[i])) {
                    spnCategory.setSelection(i);
                    break;
                }
            }
        } else {
            topCategory = topCategories[0];
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetailsActivity.setBm(BitmapUtils.base64StringToBitmap(item.getPhotoString()));
                startActivity(new Intent(ItemEditorActivity.this, PhotoDetailsActivity.class));
            }
        });

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topCategory = topCategories[position];

                onTopCategorySelected(topCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategory = (String) spnSubCategory.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.setLatLng(item.getLocationLatitude(), item.getLocationLongitude());
                MapsActivity.setSetMapChooserListener(true);
                startActivityForResult(new Intent(ItemEditorActivity.this, MapsActivity.class), Constants.REQUEST_CODE_LAT_AND_LONG);
            }
        });

        if (item == null) {
            item = new Item();
        }

        fetchUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ImagePickerUtils.REQUEST_CODE) {
                Bitmap bm = BitmapUtils.resize(ImagePickerUtils.parseBitmap(this, data));
                imageView.setImageBitmap(bm);
                item.setPhotoString(BitmapUtils.bitmapToBase64String(bm));
            } else if (requestCode == Constants.REQUEST_CODE_LAT_AND_LONG && data.getExtras() != null) {
                LatLng latLng = (LatLng) data.getExtras().get(Constants.RESULT_LAT_AND_LONG);
                if (latLng != null) {
                    item.setLocationLatitude(latLng.latitude);
                    item.setLocationLongitude(latLng.longitude);

                    tvLocation.setText(String.format("%s, %s", latLng.latitude, latLng.longitude));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        item = null;
        locationManager.removeUpdates(this);
        locationManager = null;
    }

    public static void setItem(Item i) {
        item = i;
    }

    private void fillUpDataToUI() {
        String phone;
        String address;
        if (TextUtils.isEmpty(item.getId())) {
            phone = userData.getPhone();
            address = userData.getAddress();
        } else {
            phone = item.getPhone();
            address = item.getAddress();
        }

        imageView.setImageBitmap(BitmapUtils.base64StringToBitmap(item.getPhotoString()));
        edtName.setText(item.getName());
        edtPrice.setText(item.getPrice());
        edtPhone.setText(phone);
        edtAddress.setText(address);
        tvLocation.setText(String.format("%s, %s", item.getLocationLatitude(), item.getLocationLongitude()));
        edtDescription.setText(item.getDescription());
    }

    private void getDataFromUI() {
        item.setNew(rdbNew.isChecked());
        item.setCategory(topCategory);
        item.setSubCategory(subCategory);
        item.setName(edtName.getText().toString());
        item.setPrice(edtPrice.getText().toString());
        item.setPhone(edtPhone.getText().toString());
        item.setAddress(edtAddress.getText().toString());
        item.setDescription(edtDescription.getText().toString());
    }

    private void saveItem() {
        getDataFromUI();

        if (TextUtils.isEmpty(item.getPhotoString())) {
            Toast.makeText(this, "Please add photo!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!topCategory.equals(getString(R.string.others)) && subCategory.equals(getString(R.string.all))) {
            Toast.makeText(this, "Please choose sub category!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference itemTable = FirebaseDatabase.getInstance().getReference(Constants.ITEM_TABLE);

        if (TextUtils.isEmpty(item.getId())) {
            item.setId(itemTable.push().getKey());
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Fail to get seller id", Toast.LENGTH_SHORT).show();
            return;
        }
        item.setSellerId(user.getUid());

        Helper.showProgressDialog(this, "Uploading...");
        itemTable.child(item.getId()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Helper.dismissProgressDialog();
                if (task.isSuccessful()) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ItemEditorActivity.this, "Fail to save item!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onTopCategorySelected(String topCategory) {
        String[] subCategories = TextUtils.isEmpty(topCategory)
                ? new String[]{}
                : Helper.getSubCategories(this, topCategory);
        ArrayAdapter<String> spnSubAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                subCategories);
        spnSubCategory.setAdapter(spnSubAdapter);

        if (!TextUtils.isEmpty(subCategory)) {
            for (int i = 0; i < subCategories.length; i++) {
                if (subCategory.equals(subCategories[i])) {
                    spnSubCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    private void fetchUserData() {
        Helper.showProgressDialog(this, "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (item == null) {
                            return;
                        }

                        userData = dataSnapshot.getValue(User.class);

                        if (TextUtils.isEmpty(item.getId())) {
                            findCurrentLocation();
                        } else {
                            Helper.dismissProgressDialog();

                            fillUpDataToUI();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private void findCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (item != null && item.getLocationLatitude() == 0 && item.getLocationLongitude() == 0) {
            Helper.dismissProgressDialog();
            item.setLocationLatitude(location.getLatitude());
            item.setLocationLongitude(location.getLongitude());

            fillUpDataToUI();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
