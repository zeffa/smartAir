package com.zeffah.smartair.repository;

import android.support.annotation.NonNull;

import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.datamanager.pojo.AirportData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirportRepository {
    private final ApiInterface api;

    public AirportRepository(ApiInterface api) {
        this.api = api;
    }

    public void getAirports(String token, boolean isLHOperated, String lang, final AirportResponseCallback requestCallback) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        final Call<AirportData> request = api.fetchAirports(headers, isLHOperated, lang);
        request.enqueue(new Callback<AirportData>() {
            @Override
            public void onResponse(@NonNull Call<AirportData> call, @NonNull Response<AirportData> response) {
                requestCallback.handleAirportsResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<AirportData> call, @NonNull Throwable t) {
                requestCallback.handleAirportsError();
            }
        });
    }

    public interface AirportResponseCallback {
        void handleAirportsResponse(Response<AirportData> schedulesResponse);

        void handleAirportsError();
    }
}
