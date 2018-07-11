package com.doctor.finder.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

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
    @BindView(R.id.bu_specialty)
    Button mSpecialtyButton;

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
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());
                            mUserLocation = latitude + "," + longitude;
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
                mSearchLocationButton.setText(place.getAddress());
            }
        } else {
            Toast.makeText(this, getString(R.string.select_location), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSearchQuery() {
        mQuery = mSearchQueryEt.getText().toString();
    }

    private void getSpecialtiesList() {

        try {
            InputStream is = getAssets().open("specialties.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("element");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    specialitiesUidList.add(getValue("uid", element2));
                    specialitiesNameList.add(getValue("name", element2));
                }
            }

            setSpinnerDialog();
            spinnerDialog.showSpinerDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
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

        spinnerDialog.bindOnSpinerListener((s, i) -> {
            mSpecialtyUid = specialitiesUidList.get(i);
            mSpecialtyButton.setText(s);
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
            Toast.makeText(this, getString(R.string.select_location_or_name_msg), Toast.LENGTH_SHORT).show();
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
