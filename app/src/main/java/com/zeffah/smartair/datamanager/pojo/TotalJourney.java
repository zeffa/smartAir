package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class TotalJourney {
    @SerializedName("Duration")
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
