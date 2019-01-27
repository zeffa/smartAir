package com.zeffah.smartair;

import android.os.Bundle;

import com.zeffah.smartair.contracts.SchedulesViewContract;
import com.zeffah.smartair.presenters.ScheduleListPresenter;
import com.zeffah.smartair.repository.ScheduleRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import retrofit2.Response;

import static org.mockito.Mockito.spy;

public class SchedulePresenterTest {
    private ScheduleListPresenter presenter;
    @Mock
    private ScheduleRepository repository;
    @Mock
    private SchedulesViewContract viewContract;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = spy(new ScheduleListPresenter(viewContract, repository, null));
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

//    @SuppressWarnings("unchecked")
//    @Test
//    public void handleSchedulesSuccess() {
//        Response response = Mockito.mock(Response.class);
//        JsonObject searchResponse = Mockito.mock(JsonObject.class);
//        Mockito.doReturn(true).when(response).isSuccessful();
//        Mockito.doReturn(searchResponse).when(response).body();
//        List<Schedule> searchResults = new ArrayList<>();
//        searchResults.add(new Schedule());
//        searchResults.add(new Schedule());
//        searchResults.add(new Schedule());
//
//        AppHelper helper = spy(new AppHelper());
//        Mockito.when(helper.getScheduleList(searchResponse)).thenReturn(new ArrayList<Schedule>());
//        Mockito.doReturn(searchResults).when(searchResponse);
//        // trigger
//        presenter.handleSchedulesResponse(response);
//        // validation
//        Mockito.verify(viewContract, Mockito.times(1)).displayScheduleResults(searchResults);
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void handleSchedulesFailure() {
        Response response = Mockito.mock(Response.class);

        Mockito.doReturn(false).when(response).isSuccessful();
        presenter.handleSchedulesResponse(response);
        Mockito.verify(viewContract, Mockito.times(1)).displayError();
    }
}
