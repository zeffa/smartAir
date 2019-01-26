package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class NameInfo {
    @SerializedName("$")
    private String airportName;
    @SerializedName("@LanguageCode")
    private String languageCode;

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
