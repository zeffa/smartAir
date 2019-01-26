package com.zeffah.smartair.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeffah.smartair.R;
import com.zeffah.smartair.callback.FlightItemClickCallback;
import com.zeffah.smartair.datamanager.pojo.Schedule;

import java.util.List;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {
    private List<Schedule> flightList;
    private FlightItemClickCallback airportItemClickCallback;

    public FlightListAdapter(List<Schedule> flightList, FlightItemClickCallback airportItemClickCallback) {
        this.flightList = flightList;
        this.airportItemClickCallback = airportItemClickCallback;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flight_item_view, viewGroup, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FlightViewHolder flightViewHolder, int position) {
        final Schedule schedule = flightList.get(position);
        if (schedule != null) {
            flightViewHolder.txtFlightNumber.setText(String.valueOf("Flight No: "+schedule.getFlight().get(0).getMarketingCarrier().getFlightNumber()));
            flightViewHolder.txtDepartureDate.setText(schedule.getFlight().get((0)).getDeparture().getScheduledTimeLocal().getArrivalDateTime());
            flightViewHolder.txtEffectiveFrom.setText(schedule.getFlight().get(0).getDetail().getDatePeriod().getEffectiveDate());
            flightViewHolder.txtExpiryDate.setText(schedule.getFlight().get(0).getDetail().getDatePeriod().getExpirationDate());
            flightViewHolder.txtFlightDuration.setText(schedule.getTotalJourney().getDuration());

            flightViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    airportItemClickCallback.onItemClick(schedule, flightViewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFlightNumber, txtDepartureDate, txtEffectiveFrom, txtExpiryDate, txtFlightDuration;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFlightNumber = itemView.findViewById(R.id.txt_flight_number);
            txtDepartureDate = itemView.findViewById(R.id.txt_departure_Date);
            txtEffectiveFrom = itemView.findViewById(R.id.txt_effective_from);
            txtExpiryDate = itemView.findViewById(R.id.txt_expiry_date);
            txtFlightDuration = itemView.findViewById(R.id.txt_flight_duration);


        }
    }
}
