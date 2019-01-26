package com.zeffah.smartair.pages;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, LocationSource,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {
    private AirportViewModel airportViewModel;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
//    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final int LOCATION_PERMISSION_REQUEST = 100;
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG = MapsFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static String PROVIDERS_LIST_KEY = "PROVIDERS_LIST_KEY";
//    private static final float MINIMUM_ACCURACY = 50.0f;
//    private static final float RADIUS = 1000;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap map;
    private Location location;
    private OnLocationChangedListener mapLocationListener = null;
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        airportViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AirportViewModel.class);
        buildGoogleApiClient();
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS / 2);
//
//        mLocationSettingsRequest = buildLocationSettingsRequest();
//        checkLocationSettings(mLocationSettingsRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppHelper.setActionBarTitle(context, "Flight Route", true);
        mGoogleApiClient.connect();
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
        mapFragment.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
        mGoogleApiClient.disconnect();
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

    private void requestLocationPermission() {
        requestPermissions(new String[]{ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
    }

    protected LocationSettingsRequest buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        return builder.build();
    }

    protected void checkLocationSettings(LocationSettingsRequest locationSettingsRequest) {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);
        result.setResultCallback(this);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
//    protected void startLocationUpdates() {
//        if (Utils.checkLocationPermission(context)) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this)
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                            if (!status.isSuccess()) {
//                                Toast.makeText(context, status.getStatusMessage(), Toast.LENGTH_LONG).show();
//                                ((AppCompatActivity) context).finish();
//                            }
//                        }
//                    });
//        } else {
//            requestLocationPermission();
//        }
//
//    }
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
    public void onLocationChanged(Location location) {
        if (this.mapLocationListener != null) {
            this.mapLocationListener.onLocationChanged(location);
        }
        this.location = location;
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(this.location.getLatitude(), this.location.getLongitude())));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (AppHelper.checkLocationPermission(context)) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                this.location = lastLocation;
            } else {
//                startLocationUpdates();
            }
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(((AppCompatActivity) context), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.mapLocationListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.mapLocationListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setLocationSource(this);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        if (AppHelper.checkLocationPermission(context)) {
            map.setMyLocationEnabled(true);
        } else {
            requestLocationPermission();
        }
        map.moveCamera(CameraUpdateFactory.zoomTo(15f));
        initMarkers();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
//                startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                try {
                    status.startResolutionForResult(((AppCompatActivity) context), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (AppHelper.checkLocationPermission(context)) {
//                        startLocationUpdates();
                        map.setMyLocationEnabled(true);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)
                                    && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                AppHelper.showMessageDialog(context, "You need to allow access to both the permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestLocationPermission();
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
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
            if (flightList != null && !flightList.isEmpty() && airportList != null){
                List<Point> pointList = AppHelper.flightPoints(flightList, airportList);
                List<LatLng> polyPoints = new ArrayList<>();
                for (Point point: pointList){
                    LatLng latLng = new LatLng(point.coordinate.getLatitude(), point.coordinate.getLongitude());
                    polyPoints.add(latLng);
                    map.addMarker(new MarkerOptions().position(new LatLng(point.coordinate.getLatitude(), point.coordinate.getLongitude())).title(point.name));
                }
                map.addPolyline(new PolylineOptions().addAll(polyPoints).width(2).color(Color.BLUE).geodesic(true));
            }
        }
    }
}
