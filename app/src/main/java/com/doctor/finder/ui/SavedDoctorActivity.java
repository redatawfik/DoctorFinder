package com.doctor.finder.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doctor.finder.Constants;
import com.doctor.finder.R;
import com.doctor.finder.adapter.SavedDoctorListAdapter;
import com.doctor.finder.database.AppDatabase;
import com.doctor.finder.database.DoctorEntry;
import com.doctor.finder.database.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedDoctorActivity extends AppCompatActivity implements SavedDoctorListAdapter.SavedDoctorAdapterOnClickHandler {

    @BindView(R.id.saves_doctor_list_recycler_view)
    RecyclerView savesDoctorListRecyclerView;


    private AppDatabase mDb;
    private SavedDoctorListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_doctor);
        ButterKnife.bind(this);

        mAdapter = new SavedDoctorListAdapter(this, this);
        mDb = AppDatabase.getInstance(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        savesDoctorListRecyclerView.setLayoutManager(layoutManager);
        savesDoctorListRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }



    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getdoctors().observe(this, new Observer<List<DoctorEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<DoctorEntry> doctorEntries) {
                        mAdapter.setDoctors(doctorEntries);
                    }
                });
    }

    @Override
    public void onClick(String uid) {

        Intent intent  = new Intent(this,ProfileActivity.class);
        intent.putExtra(Constants.SAVED_DOCTOR_UID, uid);
        startActivity(intent);
    }
}
