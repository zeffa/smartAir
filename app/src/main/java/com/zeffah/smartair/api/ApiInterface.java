package com.zeffah.smartair.api;

import com.zeffah.smartair.datamanager.pojo.AirportData;
import com.zeffah.smartair.datamanager.pojo.FlightScheduleData;
import com.zeffah.smartair.datamanager.pojo.Token;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("oauth/token")
    Call<Token> authenticate(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType);

    @GET("references/airports")
    Call<AirportData> fetchAirports(@HeaderMap Map<String, String> headers, @Query("LHoperated") boolean isLHOperated, @Query("lang") String languageCode);

    @GET("operations/schedules/{origin}/{destination}/{departureDate}")
    Call<FlightScheduleData> flightSchedules(@HeaderMap Map<String, String> headers, @Path("origin") String originAirport, @Path("destination") String destinationAirport, @Path("departureDate") String departureDate, @Query("directFlights") boolean directFlights);
}
