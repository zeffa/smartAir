package com.zeffah.smartair.contracts;

import android.support.annotation.NonNull;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.datamanager.pojo.Airport;

import java.util.List;

public interface AirportsViewContract {
    void displayAirportResults(@NonNull List<Airport> airports);

    void displayError();

    void displayError(String s);

    void showProcessing(AVLoadingIndicatorView loadingIndicatorView);

    void hideProcessingDialog(AVLoadingIndicatorView loadingIndicatorView);
}
