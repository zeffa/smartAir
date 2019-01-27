package com.zeffah.smartair.presenters;

import android.os.Bundle;

import com.google.gson.JsonObject;
import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.contracts.ScheduleListPresenterContract;
import com.zeffah.smartair.contracts.SchedulesViewContract;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.helper.AppHelper;
import com.zeffah.smartair.repository.ScheduleRepository;

import java.util.List;

import retrofit2.Response;

public class ScheduleListPresenter implements ScheduleListPresenterContract, ScheduleRepository.SchedulesResponseCallback {

    private final SchedulesViewContract schedulesViewContract;
    private final ScheduleRepository repository;
    private final AVLoadingIndicatorView loadingIndicatorView;

    public ScheduleListPresenter(SchedulesViewContract schedulesViewContract, ScheduleRepository repository, AVLoadingIndicatorView loadingIndicatorView) {
        this.schedulesViewContract = schedulesViewContract;
        this.loadingIndicatorView = loadingIndicatorView;
        this.repository = repository;
    }

    @Override
    public void getFlightSchedule(Bundle flightData, boolean isDirectFlight) {
        if (flightData != null) {
            schedulesViewContract.showProcessing(loadingIndicatorView);
            repository.getFlightSchedules(flightData, isDirectFlight, this);
        }
    }

    @Override
    public void handleSchedulesResponse(Response<JsonObject> schedulesResponse) {
        schedulesViewContract.hideProcessingDialog(loadingIndicatorView);
        if (schedulesResponse.isSuccessful()) {
            JsonObject scheduleData = schedulesResponse.body();
            if (scheduleData != null) {
                List<Schedule> scheduleList = AppHelper.getScheduleList(scheduleData);
                if (scheduleList != null && !scheduleList.isEmpty()) {
                    schedulesViewContract.displayScheduleResults(scheduleList);
                } else {
                    schedulesViewContract.displayError("No Schedules were Found");
                }
            } else {
                schedulesViewContract.displayError("No Schedules were Found");
            }
        } else {
            schedulesViewContract.displayError();
        }
    }

    @Override
    public void handleSchedulesError() {
        schedulesViewContract.hideProcessingDialog(loadingIndicatorView);
        schedulesViewContract.displayError("Please check your connection");
    }

}
