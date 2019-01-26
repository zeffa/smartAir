package com.zeffah.smartair.pages;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zeffah.smartair.R;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.Flight;
import com.zeffah.smartair.datamanager.pojo.Point;
import com.zeffah.smartair.datamanager.pojo.Position;
import com.zeffah.smartair.datamanager.viewmodel.AirportViewModel;
import com.zeffah.smartair.dialog.ProgressDialog;
import com.zeffah.smartair.helper.AppHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private AirportViewModel airportViewModel;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private Context context;

    public MapsFragment() {
    }

    public static MapsFragment newInstance(Position origin, Position destination, List<Flight> flightList) {
        Bundle args = new Bundle();
        MapsFragment fragment = new MapsFragment();
        args.putSerializable(ProgressDialog.DESTINATION_AIRPORT, destination);
        args.putSerializable(ProgressDialog.ORIGIN_AIRPORT, origin);
        args.putSerializable(ProgressDialog.FLIGHT_LIST, (Serializable) flightList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        airportViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AirportViewModel.class);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppHelper.setActionBarTitle(context, "Flight Route", true);
        mapFragment.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        initMarkers();
    }

    @SuppressWarnings("unchecked")
    private void initMarkers() {
        if (getArguments() != null) {
            Position origin = (Position) getArguments().getSerializable(ProgressDialog.ORIGIN_AIRPORT);
            Position destination = (Position) getArguments().getSerializable(ProgressDialog.DESTINATION_AIRPORT);
            if (origin != null && destination != null) {
                LatLng originLatLng = new LatLng(origin.getCordinate().getLatitude(), origin.getCordinate().getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 0f));
            }

            /*Display icon on map and draw route through all connections in case flight not direct*/
            List<Flight> flightList = (List<Flight>) getArguments().getSerializable(ProgressDialog.FLIGHT_LIST);
            List<Airport> airportList = airportViewModel.getAirportList().getValue();
            if (flightList != null && !flightList.isEmpty() && airportList != null) {
                List<Point> pointList = AppHelper.flightPoints(flightList, airportList);
                List<LatLng> polyPoints = new ArrayList<>();
                for (Point point : pointList) {
                    LatLng latLng = new LatLng(point.coordinate.getLatitude(), point.coordinate.getLongitude());
                    polyPoints.add(latLng);
                    map.addMarker(new MarkerOptions().position(new LatLng(point.coordinate.getLatitude(), point.coordinate.getLongitude())).title(point.name));
                }
                map.addPolyline(new PolylineOptions().addAll(polyPoints).width(2).color(Color.BLUE).geodesic(true));
            }
        }
    }
}
