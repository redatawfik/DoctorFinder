package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;


public class DoctorResponse {

    @SerializedName("meta")
    private Meta meta;


    @SerializedName("data")
    private Doctor data;

    public Doctor getData() {
        return data;
    }

    public void setData(Doctor data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
