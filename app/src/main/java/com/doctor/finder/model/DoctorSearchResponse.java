package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorSearchResponse {

    @SerializedName("data")
    private List<Doctor> data;

    public List<Doctor> getData() {
        return data;
    }

    public void setData(List<Doctor> data) {
        this.data = data;
    }
}
