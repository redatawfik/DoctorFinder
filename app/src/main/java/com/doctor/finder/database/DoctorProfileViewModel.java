package com.doctor.finder.database;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class DoctorProfileViewModel extends ViewModel {

    private final LiveData<DoctorEntry> doctor;


    public DoctorProfileViewModel(AppDatabase database, String uid) {
        doctor = database.doctorDao().loadDoctorByUid(uid);
    }

    public LiveData<DoctorEntry> getDoctor() {
        return doctor;
    }
}
