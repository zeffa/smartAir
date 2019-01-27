package com.zeffah.smartair.contracts;

import android.support.annotation.NonNull;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.datamanager.pojo.Schedule;

import java.util.List;

public interface SchedulesViewContract {

    void displayScheduleResults(@NonNull List<Schedule> schedules);

    void displayError();

    void displayError(String s);

    void showProcessing(AVLoadingIndicatorView loadingIndicatorView);

    void hideProcessingDialog(AVLoadingIndicatorView loadingIndicatorView);

}
