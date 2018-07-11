package com.doctor.finder.model.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreDoctorSearchResponse {
    @Expose
    @SerializedName("data")
    private List<PreDoctor> data;

    public List<PreDoctor> getData() {
        return data;
    }

    public void setData(List<PreDoctor> data) {
        this.data = data;
    }


    @SerializedName("meta")
    private PreMeta meta;

    public PreMeta getMeta() {
        return meta;
    }

    public void setMeta(PreMeta meta) {
        this.meta = meta;
    }

}
