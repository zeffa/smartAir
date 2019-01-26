package com.zeffah.smartair.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.R;
import com.zeffah.smartair.api.service.AuthService;
import com.zeffah.smartair.callback.DialogDismissListener;
import com.zeffah.smartair.callback.ServerRequestCallback;
import com.zeffah.smartair.datamanager.pojo.Flight;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.datamanager.pref.PreferenceData;
import com.zeffah.smartair.datamanager.viewmodel.FlightViewModel;

import java.util.List;

public class ProgressDialog extends DialogFragment implements ServerRequestCallback {
    public static final String DESTINATION_AIRPORT = "DESTINATION_AIRPORT";
    public static final String ORIGIN_AIRPORT = "ORIGIN_AIRPORT";
    public static final String FLIGHT_LIST = "FLIGHT_LIST";
    public static final String DEPARTURE_DATE = "DEPARTURE_DATE";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    private static String FLIGHT_INFO_KEY = "FLIGHT_INFO_KEY";
    private TextView txtProgressMessage;
    private AVLoadingIndicatorView loadingIndicatorView;
    private PreferenceData prefData;
    private Context context;
    private Bundle flightData;
    private FlightViewModel flightViewModel;

    private DialogDismissListener dismissListener;

    public static ProgressDialog newInstance(Bundle flightInfo, DialogDismissListener dismissListener) {
        Bundle args = new Bundle();
        ProgressDialog fragment = new ProgressDialog();
        args.putBundle(FLIGHT_INFO_KEY, flightInfo);
        fragment.setDismissListener(dismissListener);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDismissListener(DialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
        flightViewModel = ViewModelProviders.of((AppCompatActivity)context).get(FlightViewModel.class);
        prefData = new PreferenceData(context);
        if (getArguments() != null) {
            flightData = getArguments().getBundle(FLIGHT_INFO_KEY);
        }
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_indicator_dialog, container, false);
        txtProgressMessage = view.findViewById(R.id.txt_indicator_text);
        loadingIndicatorView = view.findViewById(R.id.avi);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingIndicatorView.smoothToShow();
        txtProgressMessage.setText(view.getContext().getResources().getString(R.string.flights_progress_message));
        Token token = prefData.getToken();
        if (token != null) {
            flightData.putString(AUTH_TOKEN, "Bearer " + token.accessToken);
            AuthService.getFlightSchedules(flightData, false, this);
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void requestSuccess(List<?> schedules) {
        loadingIndicatorView.smoothToHide();
        this.dismiss();
        flightViewModel.setFLightList((List<Schedule>) schedules);
        dismissListener.onDismiss(schedules, null);
    }

    @Override
    public void requestFailed(String error) {
        loadingIndicatorView.smoothToHide();
        txtProgressMessage.setText(error);
        txtProgressMessage.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
    }
}
