package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleResource {
    @SerializedName("Schedule")
    public List<Schedule> scheduleList;
}
