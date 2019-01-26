package com.zeffah.smartair.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.zeffah.smartair.R;
import com.zeffah.smartair.datamanager.pojo.Airport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeffah on 8/30/16.
 **/

public class AirportSearchAdapter extends ArrayAdapter<Airport> {
    public List<Airport> airports, listSuggestions;
    public List<Airport> filteredAirports;
    private int resource;

    public AirportSearchAdapter(Context context, int resource, List<Airport> airports) {
        super(context, resource, airports);
        this.airports = airports;
        this.resource = resource;
        this.listSuggestions = airports;
        this.filteredAirports = new ArrayList<>(airports);
    }

    @Override
    public int getCount() {
        return listSuggestions.size();
    }

    @Nullable
    @Override
    public Airport getItem(int position) {
        return listSuggestions.get(position);
    }

    @Override
    public void clear() {
        super.clear();
        filteredAirports.clear();
        airports.clear();
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final Filter.FilterResults filterResults = new Filter.FilterResults();
                if (constraint != null) {
                    String charString = constraint.toString().toLowerCase();
                    List<Airport> _airportList = new ArrayList<>();
                    for (Airport airport : filteredAirports) {
                        if (airport.getNames().getNameInfo().getAirportName().toLowerCase().contains(charString)
                                || airport.getAirportCode().toLowerCase().contains(charString) || airport.getCountryCode().toLowerCase().contains(charString)) {
                            _airportList.add(airport);
                        }
                    }
                    filterResults.values = _airportList;
                    filterResults.count = _airportList.size();
                    return filterResults;
                } else {
                    filterResults.values = airports;
                    filterResults.count = airports.size();
                    return filterResults;
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null) {
                    listSuggestions = (ArrayList<Airport>) filterResults.values;
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(resource, parent, false);
        }

        final Airport airport = listSuggestions.get(position);

        if (airport != null) {
            TextView txtAirportCode, txtCountryCode, txtAirportName;

            txtAirportName = view.findViewById(R.id.txt_airport_name);
            txtAirportCode = view.findViewById(R.id.txt_airport_code);
            txtCountryCode = view.findViewById(R.id.txt_country_code);
            txtCountryCode.setText(airport.getCountryCode());
            txtAirportCode.setText(airport.getAirportCode());
            txtAirportName.setText(airport.getNames().getNameInfo().getAirportName());
        }
        return view;
    }

    public void updateList(List<Airport> newList) {
        if (newList != null && newList.size() > 0) {
            listSuggestions = newList;
            notifyDataSetChanged();
        }
    }
}
