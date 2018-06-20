package com.doctor.finder.model;

import com.google.gson.annotations.SerializedName;


class License {

    @SerializedName("number")
    private String number;

    @SerializedName("state")
    private String state;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
