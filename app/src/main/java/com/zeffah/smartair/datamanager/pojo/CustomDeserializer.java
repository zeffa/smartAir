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

public class CustomDeserializer implements JsonDeserializer<List<Schedule>> {

    @Override
    public List<Schedule> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject resToJSON = json.getAsJsonObject().get("ScheduleResource").getAsJsonObject();
        JsonArray scheduleList = resToJSON.get("Schedule").getAsJsonArray();
        for (int i = 0; i < scheduleList.size(); i++) {
            JsonObject schedule = scheduleList.get(i).getAsJsonObject();
            JsonElement flight = schedule.get("Flight");
            if (flight.isJsonObject()) {
                JsonArray array = new JsonArray();
                array.add(flight);
                if (scheduleList.remove(flight)) {
                    scheduleList.set(i, array);
                }
            }
        }
        Type scheduleListType = new TypeToken<ArrayList<Schedule>>() {}.getType();
        return new Gson().fromJson(scheduleList, scheduleListType);
    }
}
