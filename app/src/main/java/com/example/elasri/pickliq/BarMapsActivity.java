package com.example.elasri.pickliq;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class BarMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String placeLat;
    private String placeLong;
    private String placeName;
    private  LatLng place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        placeLat = getIntent().getExtras().get("lat").toString();
        placeLong = getIntent().getExtras().get("long").toString();
        placeName = getIntent().getExtras().get("name").toString();

        // Add a marker in Sydney and move the camera
        place = new LatLng(Double.valueOf(placeLat), Double.valueOf(placeLong));
        mMap.addMarker(new MarkerOptions().position(place).title(placeName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }

}
