package com.zeffah.smartair.presenters;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.contracts.AirportListPresenterContract;
import com.zeffah.smartair.contracts.AirportsViewContract;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.AirportData;
import com.zeffah.smartair.repository.AirportRepository;

import java.util.List;

import retrofit2.Response;

public class AirportsPresenter implements AirportListPresenterContract, AirportRepository.AirportResponseCallback {
    private final AirportsViewContract airportViewContract;
    private final AirportRepository repository;
    private final AVLoadingIndicatorView loadingIndicatorView;

    public AirportsPresenter(AirportsViewContract airportViewContract, AirportRepository repository, AVLoadingIndicatorView loadingIndicatorView) {
        this.airportViewContract = airportViewContract;
        this.loadingIndicatorView = loadingIndicatorView;
        this.repository = repository;
    }

    @Override
    public void getAirports(String token, boolean isLHOperated, String lang) {
        airportViewContract.showProcessing(loadingIndicatorView);
        repository.getAirports(token, isLHOperated, lang, this);
    }

    @Override
    public void handleAirportsResponse(Response<AirportData> airportResponse) {
        airportViewContract.hideProcessingDialog(loadingIndicatorView);
        if (airportResponse.isSuccessful()) {
            AirportData airportRs = airportResponse.body();
            if (airportRs != null) {
                List<Airport> airportList = airportRs.airportResource.airports.airportList;
                if (airportList != null && !airportList.isEmpty()) {
                    airportViewContract.displayAirportResults(airportList);
                } else {
                    airportViewContract.displayError("No Airports Found");
                }
            } else {
                airportViewContract.displayError("No Airports Found");
            }
        } else {
            airportViewContract.displayError();
        }
    }

    @Override
    public void handleAirportsError() {
        airportViewContract.displayError("Please check your Internet");
    }
}
