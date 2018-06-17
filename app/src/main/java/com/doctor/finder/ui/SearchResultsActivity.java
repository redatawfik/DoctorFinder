package com.doctor.finder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.adapter.DoctorListAdapter;
import com.doctor.finder.model.Doctor;
import com.doctor.finder.model.searchModels.PreDoctor;
import com.doctor.finder.model.searchModels.PreDoctorSearchResponse;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity implements DoctorListAdapter.DoctorAdapterOnClickHandler {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();

    @BindView(R.id.doctor_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mProgressBar;

    private String mQuery = "";
    private String mLocation = "";
    private String mUserLocation = "";
    private String mSpecialtyUid = "";
    private String mGender = "";
    private String mSkip = "0";

    private int mTotal;


    private List<PreDoctor> doctorList;
    DoctorListAdapter mAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        context = this;

        doctorList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DoctorListAdapter(doctorList, context, (DoctorListAdapter.DoctorAdapterOnClickHandler) context);
        mRecyclerView.setAdapter(mAdapter);
        startRecyclerViewListener();

        getIntentValues();
        getDoctorsList();


    }


    private void getIntentValues() {

        Intent intent = getIntent();

        mQuery = intent.getStringExtra(Constants.QUERY);
        mLocation = intent.getStringExtra(Constants.LOCATION);
        mUserLocation = intent.getStringExtra(Constants.USER_LOCATION);
        mSpecialtyUid = intent.getStringExtra(Constants.SPECIALTY_UID);
        mGender = intent.getStringExtra(Constants.GENDER);
    }

    private void getDoctorsList() {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PreDoctorSearchResponse> call;
        if (!mGender.equals(Constants.NO_GENDER_PREFERENCE)) {
            call = apiService.getDoctorList(
                    mQuery,
                    mSpecialtyUid,
                    mLocation,
                    mUserLocation,
                    mGender,
                    mSkip,
                    Constants.API_KEY);
        } else {
            call = apiService.getDoctorList(
                    mQuery,
                    mSpecialtyUid,
                    mLocation,
                    mUserLocation,
                    mSkip,
                    Constants.API_KEY);
        }


        call.enqueue(new Callback<PreDoctorSearchResponse>() {
            @Override
            public void onResponse(Call<PreDoctorSearchResponse> call, Response<PreDoctorSearchResponse> response) {
                Log.i(TAG, "Successfully getting doctor list");
                mProgressBar.setVisibility(View.GONE);

                if (response.body() != null) {

                    Toast.makeText(SearchResultsActivity.this, "succeed get result", Toast.LENGTH_SHORT).show();
                    mTotal = response.body().getMeta().getTotal();
                    doctorList.addAll(response.body().getData());
                    mAdapter.notifyDataSetChanged();


                } else
                    Toast.makeText(SearchResultsActivity.this, "no results !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PreDoctorSearchResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(SearchResultsActivity.this, "no results !====!!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to get doctor list" + t.getMessage());
            }
        });
    }


    private void startRecyclerViewListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {

                        if (setSkipValue()) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getDoctorsList();
                            Toast.makeText(getApplicationContext(), "Reached the end of recycler view", Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }
        });
    }

    private boolean setSkipValue() {
        int skip = Integer.parseInt(mSkip);
        skip += 10;

        if (skip > mTotal) {
            return false;
        }
        mSkip = String.valueOf(skip);
        return true;


    }

    @Override
    public void onClick(int position) {

        String docUid = doctorList.get(position).getUid();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.DOCTOR_UID, docUid);
        startActivity(intent);


    }
}
