package com.zeffah.smartair.pages;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeffah.smartair.R;
import com.zeffah.smartair.adapters.FlightListAdapter;
import com.zeffah.smartair.callback.FlightItemClickCallback;
import com.zeffah.smartair.datamanager.pojo.Flight;
import com.zeffah.smartair.datamanager.pojo.Position;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.datamanager.viewmodel.FlightViewModel;
import com.zeffah.smartair.helper.AppHelper;

import java.util.List;

public class ScheduleListFragment extends Fragment implements FlightItemClickCallback {
    private static final String ORIGIN_AIRPORT_POSITION = "ORIGIN_AIRPORT_POSITION";
    private static final String DESTINATION_AIRPORT_POSITION = "DESTINATION_AIRPORT_POSITION";
    private RecyclerView airportsListView;
    private Context context;
    private TextView txtEmptyListError;
    private FlightViewModel flightViewModel;


    public ScheduleListFragment() {
    }

    public static ScheduleListFragment newInstance(Position originPosition, Position destinationPosition) {
        Bundle args = new Bundle();
        args.putSerializable(ORIGIN_AIRPORT_POSITION, originPosition);
        args.putSerializable(DESTINATION_AIRPORT_POSITION, destinationPosition);
        ScheduleListFragment fragment = new ScheduleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightViewModel = ViewModelProviders.of((AppCompatActivity) context).get(FlightViewModel.class);
        AppHelper.setActionBarTitle(context, getString(R.string.schedule_title), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppHelper.setActionBarTitle(context, getString(R.string.schedule_title), false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flight_schedule_list, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Schedule> flightList = flightViewModel.getFlightList().getValue();
        initFlights(flightList);
    }

    private void init(View view) {
        txtEmptyListError = view.findViewById(R.id.txt_error_empty_list);
        txtEmptyListError.setVisibility(View.GONE);
        airportsListView = view.findViewById(R.id.flights_list);
        airportsListView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initFlights(List<Schedule> flightSchedules) {
        FlightListAdapter airportListAdapter = new FlightListAdapter(flightSchedules, this);
        airportsListView.setAdapter(airportListAdapter);
    }

    @Override
    public void onItemClick(Schedule schedule, int position) {
        if (getArguments() != null) {
            List<Flight> flightList = schedule.getFlight();
            Position origin = (Position) getArguments().getSerializable(ORIGIN_AIRPORT_POSITION);
            Position destination = (Position) getArguments().getSerializable(DESTINATION_AIRPORT_POSITION);
            AppHelper.openPage(context, MapsFragment.newInstance(origin, destination, flightList), true);
        }
    }
}
