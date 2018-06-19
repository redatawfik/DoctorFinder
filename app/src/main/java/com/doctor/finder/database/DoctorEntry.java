package com.doctor.finder.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "doctors")
public class DoctorEntry {


    @NonNull
    @PrimaryKey
    private String uid;

    private String hospitalName;
    private String website;
    private String city;
    private double lat;
    private double lon;
    private String state;
    private String street;
    private String zip;
    private String landLineNumber;
    private String faxNumber;
    private String firstName;
    private String lastName;
    private String title;
    private String profileImage;
    private String bio;
    private String specialtyName;
    private String specialtyDescription;


    public DoctorEntry(
            String uid,
            String hospitalName,
            String website,
            String city,
            double lat,
            double lon,
            String state,
            String street,
            String zip,
            String landLineNumber,
            String faxNumber,
            String firstName,
            String lastName,
            String title,
            String profileImage,
            String bio,
            String specialtyName,
            String specialtyDescription) {

        this.uid = uid;
        this.hospitalName = hospitalName;
        this.website = website;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.state = state;
        this.street = street;
        this.zip = zip;
        this.landLineNumber = landLineNumber;
        this.faxNumber = faxNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.profileImage = profileImage;
        this.bio = bio;
        this.specialtyName = specialtyName;
        this.specialtyDescription = specialtyDescription;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getSpecialtyDescription() {
        return specialtyDescription;
    }

    public void setSpecialtyDescription(String specialtyDescription) {
        this.specialtyDescription = specialtyDescription;
    }
}
