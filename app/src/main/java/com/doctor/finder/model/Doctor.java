package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctor {

    @SerializedName("practices")
    private List<Practice> practices;

    @SerializedName("educations")
    private List<Education> educations;

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("insurances")
    private List<Insurance> insurances;

    @SerializedName("specialties")
    private List<Specialty> specialties;

    @SerializedName("licenses")
    private List<License> licenses;

    @SerializedName("uid")
    private String uid;

    @SerializedName("npi")
    private String npi;

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Practice> getPractices() {
        return practices;
    }

    public void setPractices(List<Practice> practices) {
        this.practices = practices;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNpi() {
        return npi;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }
}
