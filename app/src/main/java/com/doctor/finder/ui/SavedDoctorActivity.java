package com.doctor.finder.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.doctor.finder.R;
import com.doctor.finder.adapter.SavedDoctorListAdapter;
import com.doctor.finder.database.DoctorEntry;
import com.doctor.finder.database.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.doctor.finder.Constants.DOCTOR_ENTRY_INTENT_EXTRA;

public class SavedDoctorActivity extends AppCompatActivity implements SavedDoctorListAdapter.SavedDoctorAdapterOnClickHandler {

    @BindView(R.id.saves_doctor_list_recycler_view)
    RecyclerView savesDoctorListRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private SavedDoctorListAdapter mAdapter;
    private List<DoctorEntry> mDoctorEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_doctor);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mAdapter = new SavedDoctorListAdapter(this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        savesDoctorListRecyclerView.setLayoutManager(layoutManager);
        savesDoctorListRecyclerView.setAdapter(mAdapter);

        setupViewModel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SavedDoctorActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getDoctors().observe(this, doctorEntries -> {
            mDoctorEntryList = doctorEntries;
            mAdapter.setDoctors(doctorEntries);
        });
    }

    @Override
    public void onClick(int position) {

        DoctorEntry doctorEntry = mDoctorEntryList.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);

        intent.putExtra(DOCTOR_ENTRY_INTENT_EXTRA, doctorEntry);
        startActivity(intent);
    }

}
