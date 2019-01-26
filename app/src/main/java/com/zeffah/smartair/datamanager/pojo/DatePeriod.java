package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class DatePeriod {
    @SerializedName("Effective")
    private String effectiveDate;
    @SerializedName("Expiration")
    private String expirationDate;

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
