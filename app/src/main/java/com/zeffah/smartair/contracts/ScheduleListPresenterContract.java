package com.zeffah.smartair.contracts;

import android.os.Bundle;

public interface ScheduleListPresenterContract {
    void getFlightSchedule(Bundle flightData, boolean isDirectFlight);
}
