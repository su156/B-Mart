package com.project.b_mart.activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.b_mart.R;
import com.project.b_mart.utils.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
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

        if (latLng == null) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                Toast.makeText(this,
                        "Current location is finding...",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
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

    @Override
    public void onLocationChanged(Location location) {
        if (latLng == null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            addMarkOnMap();
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
