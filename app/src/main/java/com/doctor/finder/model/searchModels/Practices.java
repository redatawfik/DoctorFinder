package com.doctor.finder.model.searchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Practices {
    @Expose
    @SerializedName("distance")
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
