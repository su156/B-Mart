package com.project.b_mart.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.b_mart.R;
import com.project.b_mart.adapters.DrawerItemCustomAdapter;
import com.project.b_mart.fragments.ContactUsFragment;
import com.project.b_mart.fragments.HomeFragment;
import com.project.b_mart.fragments.ProfileFragment;
import com.project.b_mart.fragments.FavouriteFragment;
import com.project.b_mart.fragments.ShoppingFragment;
import com.project.b_mart.fragments.UserListFragment;
import com.project.b_mart.models.NavigationItem;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;
import com.project.b_mart.utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnSubCategorySelectedListener {
    private CharSequence title;
    private String[] mNavigationDrawerItemTitles;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        fragmentManager = getSupportFragmentManager();

        title = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        setupToolbar();

        NavigationItem[] drawerItem = new NavigationItem[7];
        drawerItem[0] = new NavigationItem(R.drawable.ic_home_black_24dp, mNavigationDrawerItemTitles[0]);
        drawerItem[1] = new NavigationItem(R.drawable.ic_person_black_24dp, mNavigationDrawerItemTitles[1]);
        drawerItem[2] = new NavigationItem(R.drawable.ic_group_black_24dp, mNavigationDrawerItemTitles[2]);
        drawerItem[3] = new NavigationItem(R.drawable.ic_favorite_black_24dp, mNavigationDrawerItemTitles[3]);
        drawerItem[4] = new NavigationItem(R.drawable.ic_shopping_cart_black_24dp, mNavigationDrawerItemTitles[4]);
        drawerItem[5] = new NavigationItem(R.drawable.ic_info_black_24dp, mNavigationDrawerItemTitles[5]);
        drawerItem[6] = new NavigationItem(R.drawable.ic_exit_to_app_black_24dp, mNavigationDrawerItemTitles[6]);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.layout_drawer_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ItemEditorActivity.class));
            }
        });

        selectItem(0);

        requestRequirePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSIONS_REQUEST_CODE) {
            requestRequirePermissions();
        }
    }

    @Override
    public void onSubCategorySelected(String topCategory, String subCategory) {
        // 4 is Shopping fragment's position
        selectItem(4, topCategory, subCategory);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(this.title);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        selectItem(position, null, null);
    }

    private void selectItem(int position, String topCategory, String subCategory) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ProfileFragment();
                break;
            case 2:
                fragment = new UserListFragment();
                break;
            case 3:
                fragment = new FavouriteFragment();
                break;
            case 4:
                fragment = new ShoppingFragment(topCategory, subCategory);
                break;
            case 5:
                fragment = new ContactUsFragment();
                break;
            default:
                signOut();
                return;
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(mNavigationDrawerItemTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    public void setFavVisibility(int visibility) {
        fab.setVisibility(visibility);
    }

    private void signOut() {
        SharedPreferencesUtils.saveString(this, SharedPreferencesUtils.EMAIL, "");
        SharedPreferencesUtils.saveString(this, SharedPreferencesUtils.PASSWORD, "");

        startActivity(new Intent(this, SignInActivity.class));
        finishAffinity();
    }

    private boolean isAllPermissionsGranted() {
        boolean hasPermissions = true;
        for (String permission : Constants.PERMISSIONS) {
            hasPermissions &= ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermissions;
    }

    private void requestRequirePermissions() {
        if (!isAllPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, Constants.PERMISSIONS.toArray(new String[0]), Constants.PERMISSIONS_REQUEST_CODE);
        } else {
            fetchFavList();
        }
    }

    private void fetchFavList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Fail to get seller id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Helper.showProgressDialog(this, "Loading...");
        FirebaseDatabase.getInstance().getReference(Constants.FAV_TABLE).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Helper.dismissProgressDialog();

                        Helper.setFavList(Helper.parseStringList(dataSnapshot));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Helper.dismissProgressDialog();
                    }
                });
    }
}
