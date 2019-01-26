package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class MarketingCarrier {
    @SerializedName("AirlineID")
    private String airlineId;
    @SerializedName("FlightNumber")
    private Integer flightNumber;

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(Integer flightNumber) {
        this.flightNumber = flightNumber;
    }
}
