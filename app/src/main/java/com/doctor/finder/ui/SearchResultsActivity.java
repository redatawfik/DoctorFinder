package com.doctor.finder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.adapter.DoctorListAdapter;
import com.doctor.finder.model.models.PreDoctor;
import com.doctor.finder.model.models.PreDoctorSearchResponse;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity implements DoctorListAdapter.DoctorAdapterOnClickHandler {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();

    @BindView(R.id.main_swipe)
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    @BindView(R.id.doctor_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mProgressBar;
    @BindView(R.id.shimmer_layout)
    ShimmerLayout shimmerLayout;
    @BindView(R.id.no_connection_logo)
    TextView noConnectionLogo;

    private String mQuery = "";
    private String mLocation = "";
    private String mUserLocation = "";
    private String mSpecialtyUid = "";
    private String mGender = "";
    private String mSkip = "0";
    private int mTotal;

    private boolean isLoading = false;


    private ArrayList<PreDoctor> doctorList;
    private DoctorListAdapter mAdapter;

    private final static String DOCTOR_LIST_KEY = "doctorListKey";

    private final static String QUERY_KEY = "queryKey";
    private final static String LOCATION_KEY = "locationKey";
    private final static String USER_LOCATION_KEY = "userLocationKey";
    private final static String SPECIALTY_UID_KEY = "specialtyUidKey";
    private final static String GENDER_KEY = "genderKey";
    private final static String TOTAL_KEY = "totalKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        Context context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doctorList = new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DoctorListAdapter(doctorList, context, (DoctorListAdapter.DoctorAdapterOnClickHandler) context);
        mRecyclerView.setAdapter(mAdapter);


        if (savedInstanceState != null) {
            getValueFromSavedInstance(savedInstanceState);
        } else {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmerAnimation();
            getIntentValues();
            getDoctorsList();
        }


        startRecyclerViewListener();
        initWaveToRefreshLayout();
    }

    private void getValueFromSavedInstance(Bundle savedInstanceState) {

        doctorList.addAll(savedInstanceState.getParcelableArrayList(DOCTOR_LIST_KEY));

        mQuery = savedInstanceState.getString(QUERY_KEY);
        mLocation = savedInstanceState.getString(LOCATION_KEY);
        mUserLocation = savedInstanceState.getString(USER_LOCATION_KEY);
        mSpecialtyUid = savedInstanceState.getString(SPECIALTY_UID_KEY);
        mGender = savedInstanceState.getString(GENDER_KEY);
        mTotal = savedInstanceState.getInt(TOTAL_KEY);

    }

    private void initWaveToRefreshLayout() {
        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.white));
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(() -> {

            noConnectionLogo.setVisibility(View.GONE);
            mAdapter.clearData();
            getDoctorsList();

        });
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

        isLoading = true;

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
                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                mWaveSwipeRefreshLayout.setRefreshing(false);
                Log.i(TAG, "Successfully getting doctor list");
                mProgressBar.setVisibility(View.GONE);

                if (response.body() != null) {

                    mTotal = response.body().getMeta().getTotal();
                    doctorList.addAll(response.body().getData());
                    mAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(SearchResultsActivity.this, getString(R.string.no_results), Toast.LENGTH_SHORT).show();
                }

                isLoading = false;

            }

            @Override
            public void onFailure(Call<PreDoctorSearchResponse> call, Throwable t) {
                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                noConnectionLogo.setVisibility(View.VISIBLE);
                mWaveSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(SearchResultsActivity.this, getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to get doctor list" + t.getMessage());
                isLoading = false;
            }
        });
    }


    private void startRecyclerViewListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {

                        if (setSkipValue() && !isLoading) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            getDoctorsList();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DOCTOR_LIST_KEY, doctorList);

        outState.putString(QUERY_KEY, mQuery);
        outState.putString(LOCATION_KEY, mLocation);
        outState.putString(USER_LOCATION_KEY, mUserLocation);
        outState.putString(SPECIALTY_UID_KEY, mSpecialtyUid);
        outState.putString(GENDER_KEY, mGender);
        outState.putInt(TOTAL_KEY, mTotal);

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
