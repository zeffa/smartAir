package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class ScheduledTimeLocal {
    @SerializedName("DateTime")
    private String arrivalDateTime;

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
}
