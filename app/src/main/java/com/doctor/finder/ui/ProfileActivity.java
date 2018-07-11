package com.doctor.finder.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.database.AppDatabase;
import com.doctor.finder.database.AppExecutors;
import com.doctor.finder.database.DoctorEntry;
import com.doctor.finder.model.Doctor;
import com.doctor.finder.model.DoctorResponse;
import com.doctor.finder.model.Phone;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doctor.finder.Constants.DOCTOR_ENTRY_INTENT_EXTRA;
import static com.doctor.finder.Constants.LAT;
import static com.doctor.finder.Constants.LONG;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.iv_profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.tv_specialty)
    TextView specialtyTextView;
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @BindView(R.id.tv_hospital_name)
    TextView hospitalNameTextView;
    @BindView(R.id.tv_address)
    TextView addressTextView;
    @BindView(R.id.ib_zip)
    ImageButton zipButton;
    @BindView(R.id.ib_fax)
    ImageButton faxButton;
    @BindView(R.id.ib_website)
    ImageButton websiteButton;
    @BindView(R.id.tv_bio)
    TextView bioTextView;
    @BindView(R.id.star_button)
    LikeButton likeButton;
    @BindView(R.id.tv_specialty_second)
    TextView tvSpecialtySecond;

    private AppDatabase mDb;

    private String uid = "";
    private Doctor mDoctor;
    private DoctorEntry mDoctorEntry;
    private GoogleMap mMap;
    private boolean isMapReady = false;
    private boolean isLocationReady = false;

    private static final String DOCTOR_ENTRY_KEY = "doctorEntryKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(DOCTOR_ENTRY_KEY)) {
            mDoctorEntry = savedInstanceState.getParcelable(DOCTOR_ENTRY_KEY);
            assert mDoctorEntry != null;
            uid = mDoctorEntry.getUid();
            initProfile();

        } else {
            if (getIntent().hasExtra(Constants.DOCTOR_UID)) {
                uid = getIntent().getStringExtra(Constants.DOCTOR_UID);
                getDoctorData();
            } else {
                mDoctorEntry = getIntent().getParcelableExtra(DOCTOR_ENTRY_INTENT_EXTRA);
                uid = mDoctorEntry.getUid();
                initProfile();
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        initLikeButton();


    }

    private void initLikeButton() {

        likeButton.setEnabled(true);
        likeButton.setIcon(IconType.Heart);
        likeButton.setIconSizeDp(30);

        checkDoctorState();

        starButtonListener();

    }

    private void checkDoctorState() {

        final List<String> uidList = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            uidList.addAll(mDb.doctorDao().getUids());
            setLike(uidList);

        });
    }

    private void setLike(List<String> uidList) {

        boolean state = false;

        for (String id : uidList) {
            if (id.equals(uid)) {
                state = true;
                break;
            }
        }

        if (state) {
            likeButton.setLiked(true);
        } else {
            likeButton.setLiked(false);
        }
    }

    private void starButtonListener() {

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                saveDoctorProfile();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteDoctorProfile();
            }
        });
    }

    private void getDoctorData() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DoctorResponse> call;
        call = apiService.getDoctorResponse(
                uid,
                Constants.API_KEY);


        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {


                if (response.body() != null && response.body() != null) {
                    mDoctor = response.body().getData();
                    initDoctorEntry();
                    initProfile();

                } else {
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, getString(R.string.connection_failed) + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initProfile() {
        setViews();
        isLocationReady = true;
        initMap();
    }

    private void setViews() {

        //first card
        Picasso.with(this)
                .load(mDoctorEntry.getProfileImage())
                .noFade().
                into(profileImage);

        String name = "Dr. " + mDoctorEntry.getFirstName() + " " + mDoctorEntry.getLastName();
        nameTextView.setText(name);

        String specialty = mDoctorEntry.getTitle() + ", " + mDoctorEntry.getSpecialtyName();
        specialtyTextView.setText(specialty);
        tvSpecialtySecond.setText(specialty);

        String description = mDoctorEntry.getSpecialtyDescription();
        descriptionTextView.setText(description);

        //second card
        String hospitalName = mDoctorEntry.getHospitalName();
        hospitalNameTextView.setText(hospitalName);

        String address =
                mDoctorEntry.getState() + ", " +
                        mDoctorEntry.getCity() + ", " +
                        mDoctorEntry.getStreet();
        if (mDoctorEntry.getCity() == null || mDoctorEntry.getCity().equals("")) {
            addressTextView.setText(getString(R.string.no_location_message));
        } else {
            addressTextView.setText(address);
        }

        //third card
        String bio = mDoctorEntry.getBio();
        bioTextView.setText(bio);

    }

    public void showZipCode(View view) {

        String zipCode = mDoctorEntry.getZip();

        if (zipCode == null || zipCode.equals("")) zipCode = getString(R.string.no_zip_code);

        shoeDialogMessage(Constants.ZIP_CODE_TITLE, zipCode);

    }

    public void showFaxNumber(View view) {

        String faxNumber = mDoctorEntry.getFaxNumber();

        if (faxNumber == null || faxNumber.equals("")) {
            faxNumber = getString(R.string.no_fax_number);
        }

        shoeDialogMessage(Constants.FAX_NUMBER_TITLE, faxNumber);

    }

    private void shoeDialogMessage(String title, String number) {
        new SweetAlertDialog(this)
                .setTitleText(title)
                .setContentText(number)
                .show();
    }

    public void openWebsite(View view) {

        String url = mDoctorEntry.getWebsite();
        String msg = getString(R.string.no_website);

        if (url != null && !url.equals("")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }


    }

    public void saveDoctorProfile() {

        AppExecutors.getInstance().diskIO().execute(() -> mDb.doctorDao().insertDoctor(mDoctorEntry));


    }

    public void deleteDoctorProfile() {

        AppExecutors.getInstance().diskIO().execute(() -> mDb.doctorDao().delete(mDoctorEntry));


    }

    @NonNull
    private void initDoctorEntry() {
        String uid = "";
        String hospitalName = "";
        String website = "";
        String city = "";
        double lat = 0;
        double lon = 0;
        String state = "";
        String street = "";
        String zip = "";
        String landLineNumber = "";
        String faxNumber = "";
        String firstName = "";
        String lastName = "";
        String title = "";
        String profileImage = "";
        String bio = "";
        String specialtyName = "";
        String specialtyDescription = "";

        if (mDoctor.getPractices().size() > 0) {
            hospitalName = mDoctor.getPractices().get(0).getName();
            website = mDoctor.getPractices().get(0).getWebsite();
            city = mDoctor.getPractices().get(0).getVisitAddress().getCity();
            lat = mDoctor.getPractices().get(0).getVisitAddress().getLat();
            lon = mDoctor.getPractices().get(0).getVisitAddress().getLon();
            state = mDoctor.getPractices().get(0).getVisitAddress().getState();
            street = mDoctor.getPractices().get(0).getVisitAddress().getStreet();
            zip = mDoctor.getPractices().get(0).getVisitAddress().getZip();
            landLineNumber = getPhoneNumber(Constants.LAND_LINE);
            faxNumber = getPhoneNumber(Constants.FAX);

        }

        firstName = mDoctor.getProfile().getFirst_name();
        lastName = mDoctor.getProfile().getLast_name();
        title = mDoctor.getProfile().getTitle();
        profileImage = mDoctor.getProfile().getImage_url();
        bio = mDoctor.getProfile().getBio();

        if (mDoctor.getSpecialties().size() > 0) {
            specialtyName = mDoctor.getSpecialties().get(0).getName();
            specialtyDescription = mDoctor.getSpecialties().get(0).getDescription();
        }

        uid = mDoctor.getUid();

        mDoctorEntry = new DoctorEntry(
                uid,
                hospitalName,
                website,
                city,
                lat,
                lon,
                state,
                street,
                zip,
                landLineNumber,
                faxNumber,
                firstName,
                lastName,
                title,
                profileImage,
                bio,
                specialtyName,
                specialtyDescription
        );
    }

    private String getPhoneNumber(String type) {

        for (Phone phone : mDoctor.getPractices().get(0).getPhones()) {
            if (phone.getType().equals(type)) {
                return phone.getNumber();
            }
        }
        return null;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        isMapReady = true;
        mMap = googleMap;
        initMap();

    }

    private void initMap() {

        if (isMapReady && isLocationReady) {

            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setScrollGesturesEnabled(false);

            double lat = mDoctorEntry.getLat();
            double lng = mDoctorEntry.getLon();

            if (lat != 0) {
                LatLng location = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(location)
                        .title(getString(R.string.doctor_location_title)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
    }

    public void call(View view) {

        String phone = mDoctorEntry.getLandLineNumber();

        if (!phone.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
        }

    }

    public void startMapActivity(View view) {

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(LAT, mDoctorEntry.getLat());
        intent.putExtra(LONG, mDoctorEntry.getLon());
        startActivity(intent);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(DOCTOR_ENTRY_KEY, mDoctorEntry);
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
