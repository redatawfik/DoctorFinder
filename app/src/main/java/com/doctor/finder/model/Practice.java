package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Practice {

    @SerializedName("location_slug")
    private String locationSlug;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    @SerializedName("uid")
    private String uid;

    @SerializedName("name")
    private String name;

    @SerializedName("website")
    private String website;

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

    @SerializedName("media")
    private List<Media> media;

    public String getLocationSlug() {
        return locationSlug;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public void setLocationSlug(String locationSlug) {
        this.locationSlug = locationSlug;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
