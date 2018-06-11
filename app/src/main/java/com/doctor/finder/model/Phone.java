package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

public class Phone {

    @SerializedName("number")
    private String number;

    @SerializedName("type")
    private String type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
