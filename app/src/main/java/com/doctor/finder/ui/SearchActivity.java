package com.doctor.finder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.MainActivity;
import com.doctor.finder.R;
import com.doctor.finder.model.Doctor;
import com.doctor.finder.model.DoctorSearchResponse;
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

    private static final String API_KEY = "12cb42313db42ce31f000d55c6acde9c";

    private static final String TAG = SearchActivity.class.getSimpleName();
    static public final int REQUEST_LOCATION = 1;
    private int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.myLocation)
    TextView myLocation;

    @BindView(R.id.et_search_query)
    EditText mSearchQueryEt;
    @BindView(R.id.bu_search_location)
    Button mSearchLocationButton;
    @BindView(R.id.radioGroup)
    RadioGroup mGenderGroup;
    private RadioButton mGenderRadioButton;

    SpinnerDialog spinnerDialog;

    private String mQuery = "";
    private String mLocation = "";
    private String mUserLocation = "";
    private String mSpecialtyUid = "";
    private String mGender = "";

    private final String NON = "no preference";


    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<String> specialitiesUidList;
    private ArrayList<String> specialitiesnameList;

    private List<Doctor> doctorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        specialitiesUidList = new ArrayList<>();
        specialitiesnameList = new ArrayList<>();

        buildGoogleApiClientObject();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        doctorList = new ArrayList<>();


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

        Toast.makeText(this, "Connection Succeed", Toast.LENGTH_SHORT).show();
        getLastKnownLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "there is no permission", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);


        } else {

            Toast.makeText(this, "there is a permission", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
        }

        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String latLong = String.valueOf(place.getLatLng());
                latLong = latLong.substring(10, latLong.length() - 1);
                String range = "100";
                mLocation = latLong + "," + range;

            }
        }
    }

    private void setSearchQuery() {
        mQuery = mSearchQueryEt.getText().toString();
    }


    private void getSpecialtiesList() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<SpecialitiesSearchResponse> call = apiService.getSpecialities(
                "12cb42313db42ce31f000d55c6acde9c");

        call.enqueue(new Callback<SpecialitiesSearchResponse>() {
            @Override
            public void onResponse(Call<SpecialitiesSearchResponse> call, Response<SpecialitiesSearchResponse> response) {
                Toast.makeText(SearchActivity.this, "Success", Toast.LENGTH_LONG).show();

                List<Specialty> specialties = response.body().getData();

                setSpecialitiesList(specialties);
                setSpinnerDialog();
                spinnerDialog.showSpinerDialog();

            }

            @Override
            public void onFailure(Call<SpecialitiesSearchResponse> call, Throwable t) {

                Toast.makeText(SearchActivity.this, t + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSpecialitiesList(List<Specialty> specialties) {

        for (Specialty specialty : specialties) {
            specialitiesUidList.add(specialty.getUid());
            specialitiesnameList.add(specialty.getName());
        }
    }

    public void selectSpecialty(View view) {

        if (specialitiesnameList.size() == 0) {
            getSpecialtiesList();
        } else {
            setSpinnerDialog();
            spinnerDialog.showSpinerDialog();
        }

    }

    private void setSpinnerDialog() {

        spinnerDialog = new SpinnerDialog(
                this,
                specialitiesnameList,
                "Select or Search Specialty",
                R.style.DialogAnimations_SmileWindow,
                "Close Button Text");

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                mSpecialtyUid = specialitiesUidList.get(i);
            }
        });
    }

    private void setGender() {

        int selectedId = mGenderGroup.getCheckedRadioButtonId();
        mGenderRadioButton = findViewById(selectedId);
        mGender = String.valueOf(mGenderRadioButton.getText());
        Toast.makeText(this, mGender, Toast.LENGTH_SHORT).show();
    }


    private void getDoctorsList() {

        setSearchQuery();
        setGender();


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DoctorSearchResponse> call;
        if (!mGender.equals(NON)) {
            call = apiService.getDoctorList(
                    mQuery,
                    mSpecialtyUid,
                    mLocation,
                    mUserLocation,
                    mGender,
                    API_KEY);
        } else {
            call = apiService.getDoctorList(
                    mQuery,
                    mSpecialtyUid,
                    mLocation,
                    mUserLocation,
                    API_KEY);
        }


        call.enqueue(new Callback<DoctorSearchResponse>() {
            @Override
            public void onResponse(Call<DoctorSearchResponse> call, Response<DoctorSearchResponse> response) {
                Toast.makeText(SearchActivity.this, "Success==================", Toast.LENGTH_LONG).show();

                if (response.body() != null) {

                    doctorList.addAll(response.body().getData());
                    settext();

                } else
                    Toast.makeText(SearchActivity.this, "no results !!!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<DoctorSearchResponse> call, Throwable t) {


                Toast.makeText(SearchActivity.this, t + "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void settext() {

        for (Doctor doctor : doctorList) {
            myLocation.append(doctor.getProfile().getFirst_name() + "\n");
        }
    }


    public void search(View view) {
        getDoctorsList();
    }
}
