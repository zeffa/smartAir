package com.zeffah.smartair.callback;

import com.zeffah.smartair.datamanager.pojo.Schedule;

public interface FlightItemClickCallback {
    void onItemClick(Schedule flight, int position);
}
