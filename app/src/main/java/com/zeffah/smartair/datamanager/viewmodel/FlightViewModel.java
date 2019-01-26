package com.zeffah.smartair.datamanager.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zeffah.smartair.datamanager.pojo.Flight;
import com.zeffah.smartair.datamanager.pojo.Schedule;

import java.util.List;

public class FlightViewModel extends ViewModel {
    private static MutableLiveData<List<Schedule>> flightList;

    public static void destroyList() {
        flightList = null;
    }

    public MutableLiveData<List<Schedule>> getFlightList() {
        if (flightList != null) {
            return flightList;
        }
        return null;
    }

    public void setFLightList(List<Schedule> flights) {
        flightList = new MutableLiveData<>();
        flightList.setValue(flights);
    }
}
