package com.doctor.finder.database;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class DoctorProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String mDoctorUid;

    public DoctorProfileViewModelFactory(AppDatabase database, String doctorUid) {
        mDb = database;
        mDoctorUid = doctorUid;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DoctorProfileViewModel(mDb, mDoctorUid);
    }
}
