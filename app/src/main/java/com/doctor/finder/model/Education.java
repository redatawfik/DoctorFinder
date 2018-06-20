package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

class Education {

    @SerializedName("school")
    private String school;

    @SerializedName("degree")
    private String degree;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
