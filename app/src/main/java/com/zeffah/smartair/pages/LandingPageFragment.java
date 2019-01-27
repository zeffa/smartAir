package com.zeffah.smartair.pages;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.R;
import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.contracts.AirportsViewContract;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.datamanager.pref.PreferenceData;
import com.zeffah.smartair.datamanager.viewmodel.AirportViewModel;
import com.zeffah.smartair.helper.AppHelper;
import com.zeffah.smartair.presenters.AirportsPresenter;
import com.zeffah.smartair.repository.AirportRepository;

import java.util.List;

public class LandingPageFragment extends Fragment implements View.OnClickListener, AirportsViewContract {
    private Context context;
    private Button btnStart;
    private AVLoadingIndicatorView indicatorView;
    private PreferenceData preferenceData;
    private AirportViewModel viewModel;
    private AirportsPresenter airportsPresenter;

    public LandingPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceData = new PreferenceData(context);
        viewModel = ViewModelProviders.of((AppCompatActivity)context).get(AirportViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing_page, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ApiInterface api = AppHelper.getApi;
        AirportRepository airportRepository = new AirportRepository(api);
        airportsPresenter = new AirportsPresenter(this, airportRepository, indicatorView);
    }

    private void init(View view) {
        btnStart = view.findViewById(R.id.btn_start);
        indicatorView = view.findViewById(R.id.avi);
        indicatorView.hide();
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onClick(View v) {
        if (v == btnStart){
            if (indicatorView.isShown()){
                return;
            }
            indicatorView.smoothToShow();
            Token token = preferenceData.getToken();
            if (token != null) {
                airportsPresenter.getAirports("Bearer " + token.accessToken, true, "en");
            }
        }
    }

    @Override
    public void displayAirportResults(@NonNull List<Airport> airports) {
        viewModel.setAirportList(airports);
        AppHelper.openPage(context, new AirportSearchFragment(), true);
    }

    @Override
    public void displayError() {
        Snackbar.make(btnStart, "No Airports Found", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayError(String s) {
        Snackbar.make(btnStart, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProcessing(AVLoadingIndicatorView loadingIndicatorView) {
        indicatorView.smoothToShow();
    }

    @Override
    public void hideProcessingDialog(AVLoadingIndicatorView loadingIndicatorView) {
        indicatorView.smoothToHide();
    }
}
