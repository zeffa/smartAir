package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Flight implements Serializable {
    @SerializedName("Departure")
    private Departure departure;
    @SerializedName("Arrival")
    private Arrival arrival;
    @SerializedName("MarketingCarrier")
    private MarketingCarrier marketingCarrier;
    @SerializedName("Details")
    private Detail detail;

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public MarketingCarrier getMarketingCarrier() {
        return marketingCarrier;
    }

    public void setMarketingCarrier(MarketingCarrier marketingCarrier) {
        this.marketingCarrier = marketingCarrier;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}
