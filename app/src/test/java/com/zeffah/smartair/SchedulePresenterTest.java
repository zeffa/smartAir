package com.zeffah.smartair;

import android.os.Bundle;

import com.google.gson.JsonObject;
import com.zeffah.smartair.contracts.SchedulesViewContract;
import com.zeffah.smartair.datamanager.pojo.Schedule;
import com.zeffah.smartair.helper.AppHelper;
import com.zeffah.smartair.presenters.ScheduleListPresenter;
import com.zeffah.smartair.repository.ScheduleRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SchedulePresenterTest {
    private ScheduleListPresenter presenter;
    @Mock
    private ScheduleRepository repository;
    @Mock
    private SchedulesViewContract viewContract;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = spy(new ScheduleListPresenter(viewContract, repository));
    }

    @Test
    public void getSchedulesNoFlightInfo() {
        Bundle flightBundle = null;
        presenter.getFlightSchedule(flightBundle, false);
        Mockito.verify(repository, Mockito.never()).getFlightSchedules(flightBundle, false, presenter);
    }

    @Test
    public void getSchedules() {
        Bundle flightBundle = new Bundle();
        presenter.getFlightSchedule(flightBundle, false);
        Mockito.verify(repository, Mockito.times(1)).getFlightSchedules(flightBundle, false, presenter);
    }

    @Test
    public void getScheduleListFromResponse(){
        JsonObject scheduleResponse = Mockito.mock(JsonObject.class);
        AppHelper appHelperMock = Mockito.mock(AppHelper.class);
        List<Schedule> scheduleResults = new ArrayList<>();
        scheduleResults.add(new Schedule());
        scheduleResults.add(new Schedule());
        scheduleResults.add(new Schedule());
        Mockito.when(appHelperMock.getScheduleList(scheduleResponse)).thenReturn(scheduleResults);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void handleSchedulesFailure() {
        Response response = Mockito.mock(Response.class);

        Mockito.doReturn(false).when(response).isSuccessful();
        presenter.handleSchedulesResponse(response);
        Mockito.verify(viewContract, Mockito.times(1)).displayError();
    }
}
