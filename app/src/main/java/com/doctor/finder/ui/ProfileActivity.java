package com.doctor.finder.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.database.AppExecutors;
import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.database.AppDatabase;
import com.doctor.finder.database.DoctorEntry;
import com.doctor.finder.database.DoctorProfileViewModel;
import com.doctor.finder.database.DoctorProfileViewModelFactory;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private AppDatabase mDb;

    private String uid = "";
    private Doctor mDoctor;
    private DoctorEntry mDoctorEntry;
    private GoogleMap mMap;
    private boolean isMapReady = false;
    private boolean isLocationRady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (getIntent().hasExtra(Constants.DOCTOR_UID)) {
            uid = getIntent().getStringExtra(Constants.DOCTOR_UID);
            getDoctorData();
        } else {
            uid = getIntent().getStringExtra(Constants.SAVED_DOCTOR_UID);
            retrieveDoctor(uid);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);


    }

    private void retrieveDoctor(String uid) {

        DoctorProfileViewModelFactory factory = new DoctorProfileViewModelFactory(mDb, uid);
        final DoctorProfileViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DoctorProfileViewModel.class);
        viewModel.getDoctor().observe(this,
                new Observer<DoctorEntry>() {
                    @Override
                    public void onChanged(@Nullable DoctorEntry doctorEntry) {
                        mDoctorEntry = doctorEntry;
                        viewModel.getDoctor().removeObserver(this);
                        setViews();
                        isLocationRady = true;
                        initMap();
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


                if (response.body() != null) {
                    Toast.makeText(ProfileActivity.this, "Doctor response is sucusssed", Toast.LENGTH_SHORT).show();
                    mDoctor = response.body().getData();
                    initDoctorEntry();
                    setViews();
                    isLocationRady = true;
                    initMap();

                } else {

                    Toast.makeText(ProfileActivity.this, "Doctor response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Failed to get doctor response" + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setViews() {

        //first card
        Picasso.with(this)
                .load(mDoctorEntry.getProfileImage())
                .noFade().
                into(profileImage);

        String name = mDoctorEntry.getFirstName() + " " + mDoctorEntry.getLastName();
        nameTextView.setText(name);

        String specialty = mDoctorEntry.getTitle() + ", " + mDoctorEntry.getSpecialtyName();
        specialtyTextView.setText(specialty);

        String description = mDoctorEntry.getSpecialtyDescription();
        descriptionTextView.setText(description);

        //second card
        String hospitalName = mDoctorEntry.getHospitalName();
        hospitalNameTextView.setText(hospitalName);

        String address =
                mDoctorEntry.getState() + ", " +
                        mDoctorEntry.getCity() + ", " +
                        mDoctorEntry.getStreet();
        addressTextView.setText(address);

        //third card
        String bio = mDoctorEntry.getBio();
        bioTextView.setText(bio);

    }

    public void showZipCode(View view) {

        String zipCode = mDoctorEntry.getZip();

        Toast.makeText(this, zipCode, Toast.LENGTH_SHORT).show();

    }

    public void showFaxNumber(View view) {

        String faxNumber = mDoctorEntry.getFaxNumber();
        Toast.makeText(this, faxNumber, Toast.LENGTH_SHORT).show();

    }

    public void openWebsite(View view) {

        String url = mDoctorEntry.getWebsite();

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();


    }

    public void saveDoctorProfile(View view) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.doctorDao().insertDoctor(mDoctorEntry);
            }
        });


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
            landLineNumber = getPhoneNumber(Constants.LANDLINE);
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
        if (isMapReady && isLocationRady) {

            double lat = mDoctorEntry.getLat();
            double lng = mDoctorEntry.getLon();
            LatLng location = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(location)
                    .title("Marker in Doctor's Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
    }
}
