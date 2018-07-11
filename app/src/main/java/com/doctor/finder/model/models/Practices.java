package com.doctor.finder.model.models;

import com.doctor.finder.model.VisitAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Practices {
    @Expose
    @SerializedName("distance")
    private double distance;

    @SerializedName("visit_address")
    private VisitAddress visitAddress;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public VisitAddress getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(VisitAddress visitAddress) {
        this.visitAddress = visitAddress;
    }
}
