package com.zeffah.smartair.datamanager.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zeffah.smartair.datamanager.pojo.Airport;

import java.util.List;

public class AirportViewModel extends ViewModel {
    private static MutableLiveData<List<Airport>> airportList;

    public static void destroyList() {
        airportList = null;
    }

    public MutableLiveData<List<Airport>> getAirportList() {
        if (airportList != null) {
            return airportList;
        }
        return null;
    }

    public void setAirportList(List<Airport> airports) {
        airportList = new MutableLiveData<>();
        airportList.setValue(airports);
    }
}
