package com.doctor.finder.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.adapter.DoctorListAdapter;
import com.doctor.finder.model.searchModels.PreDoctor;
import com.doctor.finder.model.searchModels.PreDoctorSearchResponse;
import com.doctor.finder.rest.ApiClient;
import com.doctor.finder.rest.ApiInterface;
import com.google.android.gms.tasks.Task;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

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
    LinearLayout noConnectionLogo;

    private String mQuery = "";
    private String mLocation = "";
    private String mUserLocation = "";
    private String mSpecialtyUid = "";
    private String mGender = "";
    private String mSkip = "0";

    private boolean isLoading = false;

    private int mTotal;


    private List<PreDoctor> doctorList;
    private DoctorListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        Context context = this;

        doctorList = new ArrayList<>();

        shimmerLayout.startShimmerAnimation();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DoctorListAdapter(doctorList, context, (DoctorListAdapter.DoctorAdapterOnClickHandler) context);
        mRecyclerView.setAdapter(mAdapter);


        getIntentValues();
        getDoctorsList();

        startRecyclerViewListener();

        mWaveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.white));
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                noConnectionLogo.setVisibility(View.GONE);
                mAdapter.clearData();
                getDoctorsList();

            }
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

                    Toast.makeText(SearchResultsActivity.this, "succeed get result", Toast.LENGTH_SHORT).show();
                    mTotal = response.body().getMeta().getTotal();
                    doctorList.addAll(response.body().getData());
                    mAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(SearchResultsActivity.this, "No results!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SearchResultsActivity.this, "Failed to get doctor list", Toast.LENGTH_SHORT).show();
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
