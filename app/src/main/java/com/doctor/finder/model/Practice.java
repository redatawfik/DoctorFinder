package com.doctor.finder.model;


import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Practice {

    @SerializedName("location_slug")
    private String locationSlug;

    @SerializedName("within_search_area")
    private boolean withinSearchArea;

    @SerializedName("distance")
    private double distance;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    @SerializedName("uid")
    private String uid;

    @SerializedName("name")
    private String name;

    @SerializedName("accepts_new_patients")
    private boolean acceptsNewPatients;

    @SerializedName("insurance_uids")
    private List<String> insuranceUids;

    @SerializedName("visit_address")
    private VisitAddress visitAddress;

    @SerializedName("phones")
    private List<Phone> phones;

    @SerializedName("languages")
    private List<Language> languages;

    public String getLocationSlug() {
        return locationSlug;
    }

    public void setLocationSlug(String locationSlug) {
        this.locationSlug = locationSlug;
    }

    public boolean isWithinSearchArea() {
        return withinSearchArea;
    }

    public void setWithinSearchArea(boolean withinSearchArea) {
        this.withinSearchArea = withinSearchArea;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

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

    public boolean isAcceptsNewPatients() {
        return acceptsNewPatients;
    }

    public void setAcceptsNewPatients(boolean acceptsNewPatients) {
        this.acceptsNewPatients = acceptsNewPatients;
    }

    public List<String> getInsuranceUids() {
        return insuranceUids;
    }

    public void setInsuranceUids(List<String> insuranceUids) {
        this.insuranceUids = insuranceUids;
    }

    public VisitAddress getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(VisitAddress visitAddress) {
        this.visitAddress = visitAddress;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
