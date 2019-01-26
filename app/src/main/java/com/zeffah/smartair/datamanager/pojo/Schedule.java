package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {
    @SerializedName("TotalJourney")
    private TotalJourney totalJourney;
    @SerializedName("Flight")
    private List<Flight> flight;

    public TotalJourney getTotalJourney() {
        return totalJourney;
    }

    public void setTotalJourney(TotalJourney totalJourney) {
        this.totalJourney = totalJourney;
    }

    public List<Flight> getFlight() {
        return flight;
    }

    public void setFlight(List<Flight> flight) {
        this.flight = flight;
    }
}
