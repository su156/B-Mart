package com.project.b_mart.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.b_mart.R;
import com.project.b_mart.utils.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static LatLng latLng;
    private static boolean setMapChooserListener;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MapsActivity.latLng = null;
        MapsActivity.setMapChooserListener = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latLng != null) {
            addMarkOnMap();
        }

        if (setMapChooserListener) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Intent i = new Intent();
                    i.putExtra(Constants.RESULT_LAT_AND_LONG, latLng);
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
    }

    private void addMarkOnMap() {
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    public static void setLatLng(double latitude, double longitude) {
        if (latitude != 0 && longitude != 0) {
            MapsActivity.latLng = new LatLng(latitude, longitude);
        }
    }

    public static void setSetMapChooserListener(boolean setMapChooserListener) {
        MapsActivity.setMapChooserListener = setMapChooserListener;
    }
}
