package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

public class Versions {

    @SerializedName("small")
    private String small;

    @SerializedName("medium")
    private String medium;

    @SerializedName("large")
    private String large;

    @SerializedName("hero")
    private String hero;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }
}
