package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomDeserializer implements JsonDeserializer<List<Airport>> {

    @Override
    public List<Airport> deserialize(JsonElement json, Type typeOfT_, JsonDeserializationContext context) throws JsonParseException {
        JsonObject resToJSON = json.getAsJsonObject().get("AirportResource").getAsJsonObject();
        JsonArray airports = resToJSON.get("Airports").getAsJsonObject().get("Airport").getAsJsonArray();
        Type airportListType = new TypeToken<ArrayList<Airport>>(){}.getType();
        return new Gson().fromJson(airports, airportListType);
    }
}
