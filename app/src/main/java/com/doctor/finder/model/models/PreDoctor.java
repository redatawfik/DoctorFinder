package com.doctor.finder.model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreDoctor implements Parcelable {


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

    private PreDoctor(Parcel in) {
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreDoctor> CREATOR = new Creator<PreDoctor>() {
        @Override
        public PreDoctor createFromParcel(Parcel in) {
            return new PreDoctor(in);
        }

        @Override
        public PreDoctor[] newArray(int size) {
            return new PreDoctor[size];
        }
    };

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
