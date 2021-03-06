package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coordinate implements Serializable {
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
