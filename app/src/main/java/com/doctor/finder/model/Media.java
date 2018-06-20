package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

class Media {

    @SerializedName("uid")
    private String uid;

    @SerializedName("status")
    private String status;

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private String type;

    @SerializedName("versions")
    private Versions versions;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Versions getVersions() {
        return versions;
    }

    public void setVersions(Versions versions) {
        this.versions = versions;
    }
}
