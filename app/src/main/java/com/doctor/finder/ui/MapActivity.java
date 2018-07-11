package com.doctor.finder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        if (intent.hasExtra(Constants.LAT)) {
            lat = intent.getDoubleExtra(Constants.LAT, 0);
            lon = intent.getDoubleExtra(Constants.LONG, 0);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.full_map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (lat != 0) {
            LatLng location = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(getString(R.string.doctor_location_title)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
    }
}
