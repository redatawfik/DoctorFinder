package com.doctor.finder.model;


import com.google.gson.annotations.SerializedName;

class Insurance {

    @SerializedName("insurance_plan")
    private InsurancePlan insurance_plan;

    @SerializedName("insurance_provider")
    private InsuranceProvider insurance_provider;

    public InsurancePlan getInsurance_plan() {
        return insurance_plan;
    }

    public void setInsurance_plan(InsurancePlan insurance_plan) {
        this.insurance_plan = insurance_plan;
    }

    public InsuranceProvider getInsurance_provider() {
        return insurance_provider;
    }

    public void setInsurance_provider(InsuranceProvider insurance_provider) {
        this.insurance_provider = insurance_provider;
    }
}
