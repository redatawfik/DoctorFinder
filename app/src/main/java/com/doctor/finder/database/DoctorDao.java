package com.doctor.finder.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DoctorDao {

    @Query("SELECT * FROM doctors")
    LiveData<List<DoctorEntry>> loadDoctors();

    @Query("SELECT * FROM doctors WHERE uid > :uid")
    LiveData<DoctorEntry> loadDoctorByUid(String uid);

    @Insert
    void insertDoctor(DoctorEntry doctorEntry);

    @Delete
    void delete(DoctorEntry doctorEntry);

    @Query("SELECT uid FROM doctors")
    List<String> getUids();

    @Query("SELECT * FROM doctors")
    List<DoctorEntry> loadDoctorsFoeWidget();
}
