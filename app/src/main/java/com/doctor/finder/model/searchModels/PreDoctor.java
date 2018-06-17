package com.doctor.finder.model.searchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreDoctor {


    @Expose
    @SerializedName("profile")
    private PreProfile profile;
    @Expose
    @SerializedName("practices")
    private List<Practices> practices;

    @Expose
    @SerializedName("specialties")
    private List<PreSpecialty> specialties;

    @SerializedName("uid")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PreProfile getProfile() {
        return profile;
    }

    public void setProfile(PreProfile profile) {
        this.profile = profile;
    }

    public List<Practices> getPractices() {
        return practices;
    }

    public void setPractices(List<Practices> practices) {
        this.practices = practices;
    }

    public List<PreSpecialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<PreSpecialty> specialties) {
        this.specialties = specialties;
    }
}
