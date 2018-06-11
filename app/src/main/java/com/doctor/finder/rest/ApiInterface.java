package com.doctor.finder.rest;


import com.doctor.finder.model.DoctorSearchResponse;
import com.doctor.finder.model.SpecialitiesSearchResponse;
import com.doctor.finder.model.Specialty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("doctors?")
    Call<DoctorSearchResponse> getDoctorList(@Query("query") String query,
                                             @Query("specialty_uid") String specialtyUid,
                                             @Query("location") String location,
                                             @Query("user_location") String userLocation,
                                             @Query("gender") String gender,
                                             @Query("user_key") String userKey);


    @GET("doctors?")
    Call<DoctorSearchResponse> getDoctorList(@Query("query") String query,
                                             @Query("specialty_uid") String specialtyUid,
                                             @Query("location") String location,
                                             @Query("user_location") String userLocation,
                                             @Query("user_key") String userKey);

    @GET("specialties")
    Call<SpecialitiesSearchResponse> getSpecialities(@Query("user_key") String userKey);

}
