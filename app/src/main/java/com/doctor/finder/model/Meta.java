package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("data_type")
    private String dataType;

    @SerializedName("total")
    private int total;

    @SerializedName("count")
    private int count;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
