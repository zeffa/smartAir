package com.zeffah.smartair.datamanager.pojo;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Airport {
    @SerializedName("AirportCode")
    private String airportCode;
    @SerializedName("CountryCode")
    private String countryCode;
    @SerializedName("Names")
    private AirportName names;
    @SerializedName("Position")
    private Position position;

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public AirportName getNames() {
        return names;
    }

    public void setNames(AirportName names) {
        this.names = names;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override @NonNull
    public String toString() {
        return getNames().getNameInfo().getAirportName();
    }
}
