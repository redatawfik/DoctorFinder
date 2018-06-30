package com.doctor.finder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.model.SpecialitiesSearchResponse;
import com.doctor.finder.model.Specialty;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 1;
    private final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.et_search_query)
    MaterialEditText mSearchQueryEt;
    @BindView(R.id.bu_search_location)
    Button mSearchLocationButton;
    @BindView(R.id.radioGroup)
    RadioGroup mGenderGroup;

    private SpinnerDialog spinnerDialog;

    private String mQuery = "";
    private String mLocation = "";
    private String mUserLocation = "";
    private String mSpecialtyUid = "";
    private String mGender = "";


    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<String> specialitiesUidList;
    private ArrayList<String> specialitiesNameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        specialitiesUidList = new ArrayList<>();
        specialitiesNameList = new ArrayList<>();

        buildGoogleApiClientObject();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }

    private void buildGoogleApiClientObject() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(TAG, "Connection to GoogleApiClient Succeed");
        getLastKnownLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection to GoogleApiClient Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to GoogleApiClient failed");
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);


        } else {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String latitude = String.valueOf(location.getLatitude());
                                String longitude = String.valueOf(location.getLongitude());
                                mUserLocation = latitude + "," + longitude;
                            }
                        }
                    });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            }
        } else {
            Toast.makeText(this, "Permission to location denied", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Permission to location denied");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void pickALocation(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent = null;
        try {
            intent = builder.build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String latLong = String.valueOf(place.getLatLng());
                latLong = latLong.substring(10, latLong.length() - 1);
                String range = "100";
                mLocation = latLong + "," + range;

            }
        } else {
            Toast.makeText(this, "please select a location", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No location selected");
        }
    }

    private void setSearchQuery() {
        mQuery = mSearchQueryEt.getText().toString();
    }

    private void getSpecialtiesList() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<SpecialitiesSearchResponse> call = apiService.getSpecialities(
                Constants.API_KEY);

        call.enqueue(new Callback<SpecialitiesSearchResponse>() {
            @Override
            public void onResponse(Call<SpecialitiesSearchResponse> call, Response<SpecialitiesSearchResponse> response) {
                Log.i(TAG, "Getting specialties list succeed");

                List<Specialty> specialties = response.body().getData();

                setSpecialitiesList(specialties);
                setSpinnerDialog();
                spinnerDialog.showSpinerDialog();

            }

            @Override
            public void onFailure(Call<SpecialitiesSearchResponse> call, Throwable t) {

                Toast.makeText(SearchActivity.this, "Failed to get specialties list", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to get specialties list" + t.getMessage());
            }
        });
    }

    private void setSpecialitiesList(List<Specialty> specialties) {

        for (Specialty specialty : specialties) {
            specialitiesUidList.add(specialty.getUid());
            specialitiesNameList.add(specialty.getName());
        }
    }

    public void selectSpecialty(View view) {

        if (specialitiesNameList.size() == 0) {
            getSpecialtiesList();
        } else {
            setSpinnerDialog();
            spinnerDialog.showSpinerDialog();
        }

    }

    private void setSpinnerDialog() {

        spinnerDialog = new SpinnerDialog(
                this,
                specialitiesNameList,
                "Select or Search Specialty",
                R.style.DialogAnimations_SmileWindow,
                "Close");

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                mSpecialtyUid = specialitiesUidList.get(i);
            }
        });
    }

    private void setGender() {

        int selectedId = mGenderGroup.getCheckedRadioButtonId();
        RadioButton mGenderRadioButton = findViewById(selectedId);
        mGender = String.valueOf(mGenderRadioButton.getText()).toLowerCase();
    }

    public void search(View view) {

        setSearchQuery();
        setGender();

        if (mQuery.equals("") && mLocation.equals("")) {
            Toast.makeText(this, "At least select name or location", Toast.LENGTH_SHORT).show();
        } else {
            startSearchResultsActivity();
        }

    }

    private void startSearchResultsActivity() {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(Constants.QUERY, mQuery);
        intent.putExtra(Constants.LOCATION, mLocation);
        intent.putExtra(Constants.USER_LOCATION, mUserLocation);
        intent.putExtra(Constants.SPECIALTY_UID, mSpecialtyUid);
        intent.putExtra(Constants.GENDER, mGender);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
