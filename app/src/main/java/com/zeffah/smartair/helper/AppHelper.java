package com.zeffah.smartair.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zeffah.smartair.R;
import com.zeffah.smartair.api.ApiClient;
import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.Flight;
import com.zeffah.smartair.datamanager.pojo.Point;
import com.zeffah.smartair.datamanager.pojo.Schedule;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppHelper {
    public static ApiInterface getApi = ApiClient.getClient().create(ApiInterface.class);

    public static String getDateTimeFromString(String dateTime) {
        String date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date formattedDate = formatter.parse(dateTime);
            date = formatter.format(formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void openPage(Context context, Fragment pageFragment, boolean addToBackStack) {
        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(R.id.container, pageFragment, pageFragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(pageFragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    public static void showDialog(Context context, DialogFragment dialogFragment) {
        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
        dialogFragment.show(manager, dialogFragment.getClass().getSimpleName());
    }

    public static Airport getAirport(String airportCode, List<Airport> airports) {
        for (Airport _airport : airports) {
            if (_airport.getAirportCode().equals(airportCode)) {
                return _airport;
            }
        }
        return null;
    }

    public List<Schedule> getScheduleList(JsonObject scheduleData) {
        JsonObject resToJSON = scheduleData.get("ScheduleResource").getAsJsonObject();
        JsonArray schedulesArray = resToJSON.get("Schedule").getAsJsonArray();
        for (JsonElement element : schedulesArray) {
            JsonObject schedule = element.getAsJsonObject();
            JsonElement flight = schedule.get("Flight");
            if (flight.isJsonObject()) {
                JsonArray array = new JsonArray();
                array.add(flight);
                schedule.add("Flight", array);
            }
        }
        Type listType = new TypeToken<ArrayList<Schedule>>() {
        }.getType();
        return new Gson().fromJson(schedulesArray, listType);
    }

    public static List<Point> flightPoints(List<Flight> flightList, List<Airport> airportList) {
        List<Point> pointList = new ArrayList<>();
        if (flightList != null && !flightList.isEmpty() && airportList != null) {
            if (flightList.size() > 1) {
                for (int i = 0; i < flightList.size() - 1; i++) {
                    Point point = new Point();
                    Flight flight = flightList.get(i);
                    Airport departureAirport = getAirport(flight.getDeparture().getAirportCode(), airportList);
                    if (departureAirport != null) {
                        point.departure = flight.getDeparture();
                        point.coordinate = departureAirport.getPosition().getCordinate();
                        point.name = departureAirport.getNames().getNameInfo().getAirportName();
                        pointList.add(point);
                    }
                }
                Point last = new Point();
                Flight lastFlight = flightList.get(flightList.size() - 1);
                Airport arrivalAirport = getAirport(lastFlight.getArrival().getAirportCode(), airportList);
                if (arrivalAirport != null) {
                    last.departure = lastFlight.getDeparture();
                    last.coordinate = arrivalAirport.getPosition().getCordinate();
                    last.name = arrivalAirport.getNames().getNameInfo().getAirportName();
                    pointList.add(last);
                }
            } else {
                Flight flight = flightList.get(0);
                Airport departureAirport = getAirport(flight.getDeparture().getAirportCode(), airportList);
                Airport arrivalAirport = getAirport(flight.getArrival().getAirportCode(), airportList);
                if (arrivalAirport != null && departureAirport != null) {
                    Point pointOrigin = new Point();
                    pointOrigin.departure = flight.getDeparture();
                    pointOrigin.coordinate = departureAirport.getPosition().getCordinate();
                    pointOrigin.name = departureAirport.getNames().getNameInfo().getAirportName();
                    pointList.add(pointOrigin);

                    Point pointDest = new Point();
                    pointDest.departure = flight.getDeparture();
                    pointDest.coordinate = arrivalAirport.getPosition().getCordinate();
                    pointDest.name = arrivalAirport.getNames().getNameInfo().getAirportName();
                    pointList.add(pointDest);
                }

            }
            return pointList;
        }
        return null;
    }

    public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void launchActivity(Context context, Class mClass) {
        Intent intent = new Intent(context, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void setActionBarTitle(Context context, String title, boolean setDisplayHomeAsUpEnabled) {
        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            if (!setDisplayHomeAsUpEnabled) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    public static void setDrawable(View view, DrawablePosition position, int drawable) {
        if (view instanceof EditText) {
            switch (position) {
                case TOP:
                    ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
                    break;
                case LEFT:
                    ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
                    break;
                case RIGHT:
                    ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
                    break;
                case BOTTOM:
                    ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable);
                    break;
                default:
                    ((EditText) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    break;
            }
            ((EditText) view).setCompoundDrawablePadding(2);
        }
    }

    public static void hideSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View view = ((AppCompatActivity) context).getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
