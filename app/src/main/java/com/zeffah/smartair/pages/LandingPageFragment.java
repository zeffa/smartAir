package com.zeffah.smartair.pages;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.R;
import com.zeffah.smartair.api.service.AuthService;
import com.zeffah.smartair.callback.ServerRequestCallback;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.datamanager.pref.PreferenceData;
import com.zeffah.smartair.datamanager.viewmodel.AirportViewModel;
import com.zeffah.smartair.helper.AppHelper;

import java.util.List;

public class LandingPageFragment extends Fragment implements View.OnClickListener, ServerRequestCallback {
    private Context context;
    private Button btnStart;
    private AVLoadingIndicatorView indicatorView;
    private PreferenceData preferenceData;
    private AirportViewModel viewModel;

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
                AuthService.getAirports("Bearer " + token.accessToken, true, "en", LandingPageFragment.this);
            }
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void requestSuccess(List<?> airports) {
        if (indicatorView.isShown()){
            indicatorView.smoothToHide();
        }
        viewModel.setAirportList((List<Airport>) airports);
        AppHelper.openPage(context, new AirportSearchFragment(), true);
    }

    @Override
    public void requestFailed(String error) {
        if (indicatorView.isShown()){
            indicatorView.smoothToHide();
        }
        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
    }
}
