package com.doctor.finder.model.models;


import com.google.gson.annotations.SerializedName;


public class PreMeta {

    @SerializedName("data_type")
    private String dataType;

    @SerializedName("item_type")
    private String itemType;

    @SerializedName("total")
    private int total;

    @SerializedName("count")
    private int count;

    @SerializedName("skip")
    private int skip;

    @SerializedName("limit")
    private int limit;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
