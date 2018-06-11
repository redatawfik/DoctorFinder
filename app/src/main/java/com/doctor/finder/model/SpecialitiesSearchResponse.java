package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpecialitiesSearchResponse {

    @SerializedName("data")
    private List<Specialty> data;

    public List<Specialty> getData() {
        return data;
    }

    public void setData(List<Specialty> data) {
        this.data = data;
    }
}
