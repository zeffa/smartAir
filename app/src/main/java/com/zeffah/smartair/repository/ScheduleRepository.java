package com.zeffah.smartair.repository;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.callback.ServerRequestCallback;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.dialog.ProgressDialog;
import com.zeffah.smartair.helper.AppHelper;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepository {
    private final ApiInterface api;

    public ScheduleRepository(ApiInterface api) {
        this.api = api;
    }

    public void getFlightSchedules(final Bundle flightData, boolean isDirectFlight, final SchedulesResponseCallback requestCallback) {
        String token = flightData.getString(ProgressDialog.AUTH_TOKEN);
        String originAirport = flightData.getString(ProgressDialog.ORIGIN_AIRPORT);
        String destinationAirport = flightData.getString(ProgressDialog.DESTINATION_AIRPORT);
        String departureDate = flightData.getString(ProgressDialog.DEPARTURE_DATE);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        final Call<JsonObject> request = this.api.flightSchedules(headers, originAirport, destinationAirport, departureDate, isDirectFlight);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                requestCallback.handleSchedulesResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                requestCallback.handleSchedulesError();
            }
        });
    }

    public interface SchedulesResponseCallback {
        void handleSchedulesResponse(Response<JsonObject> schedulesResponse);
        void handleSchedulesError();
    }
}
