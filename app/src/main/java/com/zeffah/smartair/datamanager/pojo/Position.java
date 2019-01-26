package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Position implements Serializable {
    @SerializedName("Coordinate")
    private Coordinate cordinate;

    public Coordinate getCordinate() {
        return cordinate;
    }

    public void setCordinate(Coordinate cordinate) {
        this.cordinate = cordinate;
    }
}
