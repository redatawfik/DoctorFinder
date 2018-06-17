package com.doctor.finder.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.model.Doctor;
import com.doctor.finder.model.DoctorResponse;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

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
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.ib_zip)
    ImageButton zipButton;
    @BindView(R.id.ib_fax)
    ImageButton faxButton;
    @BindView(R.id.ib_website)
    ImageButton websiteButton;
    @BindView(R.id.tv_bio)
    TextView bioTextView;


    private String uid = "";
    private Doctor mDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        uid = getIntent().getStringExtra(Constants.DOCTOR_UID);
        getDoctorData();

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
                    setViews();

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
                .load(mDoctor.getProfile().getImage_url())
                .noFade().
                into(profileImage);

        String name = mDoctor.getProfile().getFirst_name() + " " + mDoctor.getProfile().getLast_name();
        nameTextView.setText(name);

        String specialty = mDoctor.getProfile().getTitle() + ", " + mDoctor.getSpecialties().get(0).getName();
        specialtyTextView.setText(specialty);

        String description = mDoctor.getSpecialties().get(0).getDescription();
        descriptionTextView.setText(description);

        //second card
        String hospitalName = mDoctor.getPractices().get(0).getName();
        hospitalNameTextView.setText(hospitalName);

        String address =
                mDoctor.getPractices().get(0).getVisitAddress().getState() + ", " +
                        mDoctor.getPractices().get(0).getVisitAddress().getCity() + ", " +
                        mDoctor.getPractices().get(0).getVisitAddress().getStreet() + ", " +
                        mDoctor.getPractices().get(0).getVisitAddress().getStreet2();
        addressTextView.setText(address);


        String bio = mDoctor.getProfile().getBio();
        bioTextView.setText(bio);

    }

    public void showZipCode(View view) {

        String zipCode = mDoctor.getPractices().get(0).getVisitAddress().getZip();

        Toast.makeText(this, zipCode, Toast.LENGTH_SHORT).show();
//        new SweetAlertDialog(this)
//                .setTitleText("Zip Code")
//                .setContentText(zipCode)
//                .show();
    }

    public void showFaxNumber(View view) {

        String faxNumber = mDoctor.getPractices().get(0).getPhones().get(0).getNumber();
        Toast.makeText(this, faxNumber, Toast.LENGTH_SHORT).show();
//
//        new SweetAlertDialog(this)
//                .setTitleText("Zip Code")
//                .setContentText(zipCode)
//                .show();
    }

    public void openWebsite(View view) {

        String url = mDoctor.getPractices().get(0).getWebsite();
        String trx = mDoctor.getPractices().get(0).getUid();
        Toast.makeText(this, url + trx, Toast.LENGTH_SHORT).show();

//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);

//        Uri webPage = Uri.parse(mDoctor.getPractices().get(0).getWebsite());
//        if (webPage!=null){
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }
//        }else {
//            Toast.makeText(this, "no webPage provided", Toast.LENGTH_SHORT).show();
//        }
//

    }
}
