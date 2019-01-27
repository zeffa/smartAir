package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Arrival implements Serializable {
    @SerializedName("AirportCode")
    private String airportCode;
    @SerializedName("ScheduledTimeLocal")
    private ScheduledTimeLocal scheduledTimeLocal;

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public ScheduledTimeLocal getScheduledTimeLocal() {
        return scheduledTimeLocal;
    }

    public void setScheduledTimeLocal(ScheduledTimeLocal scheduledTimeLocal) {
        this.scheduledTimeLocal = scheduledTimeLocal;
    }
}
