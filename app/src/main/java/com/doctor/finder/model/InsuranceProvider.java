package com.doctor.finder.model;

import com.google.gson.annotations.SerializedName;


public class InsuranceProvider {

    @SerializedName("uid")
    private String uid;

    @SerializedName("name")
    private String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
