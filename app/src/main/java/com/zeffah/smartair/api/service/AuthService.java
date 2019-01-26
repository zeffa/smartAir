package com.zeffah.smartair.api.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.callback.ServerRequestCallback;
import com.zeffah.smartair.callback.OnAuthCallback;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.AirportData;
import com.zeffah.smartair.datamanager.pojo.FlightScheduleData;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.dialog.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zeffah.smartair.helper.AppHelper.getApi;

public class AuthService {
    public static String getAuthToken(String clientId, String clientSecret, String grantType) {
        {
            ApiInterface api = getApi;
            String token = null;
            try {
                Token response = api.authenticate(clientId, clientSecret, grantType).execute().body();
                if (response != null) {
                    JSONObject object = new JSONObject(response.toString());
                    token = object.getString("access_token");
                }
            } catch (IOException | JSONException e) {
                token = null;
            }
            return token;
        }

    }

    public static void getAuthToken(String clientId, String clientSecret, String grantType, final OnAuthCallback authCallback) {
        ApiInterface getToken = getApi;
        final Call<Token> token = getToken.authenticate(clientId, clientSecret, grantType);

        token.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                ResponseBody responseBody = response.errorBody();
                Token object = response.body();
                try {
                    if (object != null) {
                        authCallback.authSuccess(object);
                    } else {
                        if (response.code() != 200) {
                            if (responseBody != null) {
                                authCallback.authFailed(responseBody.string());
                            }
                        }
                    }
                } catch (Exception e) {
                    authCallback.authFailed("Unknown Error. Please try again.");
                    Log.d("Auth_token_error_1", "Unknown Error. Please try again. => "+e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                authCallback.authFailed("Network Error.\n\nCheck your internet Connection");
                Log.d("Auth_token_error_2", t.getMessage());
            }
        });
    }

    public static void getAirports(String token, boolean isLHOperated, String lang, final ServerRequestCallback requestCallback) {
        ApiInterface api = getApi;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        final Call<AirportData> request = api.fetchAirports(headers, isLHOperated, lang);
        request.enqueue(new Callback<AirportData>() {
            @Override
            public void onResponse(@NonNull Call<AirportData> call, @NonNull Response<AirportData> response) {
                try {
                    AirportData airportRs = response.body();
                    if (airportRs != null) {
                        List<Airport> airportList = airportRs.airportResource.airports.airportList;
                        Log.d("myAirportList", new Gson().toJson(airportList));
                        if (airportList != null && !airportList.isEmpty()) {
                            requestCallback.requestSuccess(airportList);
                        }else {
                            requestCallback.requestFailed("No airports Found for your Search");
                        }
                    }
                } catch (Exception e) {
                    requestCallback.requestFailed("Failed");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<AirportData> call, @NonNull Throwable t) {
                Log.d("AirportsList_error", t.getLocalizedMessage() + "");
                requestCallback.requestFailed("Please Check your internet connection");
            }
        });
    }

    public static void getFlightSchedules(final Bundle flightData, boolean isDirectFlight, final ServerRequestCallback requestCallback) {
        String token = flightData.getString(ProgressDialog.AUTH_TOKEN);
        String originAirport = flightData.getString(ProgressDialog.ORIGIN_AIRPORT);
        String destinationAirport = flightData.getString(ProgressDialog.DESTINATION_AIRPORT);
        String departureDate = flightData.getString(ProgressDialog.DEPARTURE_DATE);
        ApiInterface api = getApi;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        final Call<FlightScheduleData> request = api.flightSchedules(headers, originAirport, destinationAirport, departureDate, isDirectFlight);
        Log.d("FlightList_response", request.request().url().toString());
        request.enqueue(new Callback<FlightScheduleData>() {
            @Override
            public void onResponse(@NonNull Call<FlightScheduleData> call, @NonNull Response<FlightScheduleData> response) {
                Log.d("FlightList_response", response.toString());
                try {
                    FlightScheduleData scheduleData = response.body();
                    if (scheduleData != null) {
                        List<Schedule> flightSchedule = scheduleData.scheduleResource.scheduleList;
                        Log.d("flightSize", flightData.size()+"");
                        Log.d("myScheduleList2", new Gson().toJson(flightSchedule));
                        requestCallback.requestSuccess(flightSchedule);
                    }else {
                        requestCallback.requestFailed(response.errorBody() != null? response.errorBody().string(): "Could not fetch schedules. Try again");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<FlightScheduleData> call, @NonNull Throwable t) {
                Log.d("FlightList_error", t.getLocalizedMessage() + "");
                requestCallback.requestFailed("Please Check Your Internet Connection");
            }
        });
    }
}
