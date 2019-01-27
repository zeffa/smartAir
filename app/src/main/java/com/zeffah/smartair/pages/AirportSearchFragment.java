package com.zeffah.smartair.pages;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.zeffah.smartair.R;
import com.zeffah.smartair.adapters.AirportSearchAdapter;
import com.zeffah.smartair.callback.DialogDismissListener;
import com.zeffah.smartair.datamanager.pojo.Airport;
import com.zeffah.smartair.datamanager.viewmodel.AirportViewModel;
import com.zeffah.smartair.dialog.ProgressDialog;
import com.zeffah.smartair.helper.AppHelper;
import com.zeffah.smartair.helper.DrawablePosition;

import java.util.List;

public class AirportSearchFragment extends Fragment implements TextWatcher, View.OnTouchListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener, DialogDismissListener {
    private Context context;
    private AutoCompleteTextView autoOriginAirport, autoDestinationAirport;
    private TextView txtDepartureDate;
    private Button btnGetSchedules;
    private AirportViewModel airportViewModel;
    private Airport destinationAirport, originAirport;
    private String departureDate = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        airportViewModel = ViewModelProviders.of((AppCompatActivity) context).get(AirportViewModel.class);
        AppHelper.setActionBarTitle(context, "Search Airport", true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_search_airport, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAutoComplete();
    }

    private void init(View view) {
        autoDestinationAirport = view.findViewById(R.id.autocomplete_destination_airport);
        autoOriginAirport = view.findViewById(R.id.autocomplete_origin_airport);
        btnGetSchedules = view.findViewById(R.id.btn_get_flight_schedules);
        txtDepartureDate = view.findViewById(R.id.edt_departure_date);
        autoOriginAirport.addTextChangedListener(this);
        autoDestinationAirport.addTextChangedListener(this);
        autoOriginAirport.setThreshold(1);
        autoDestinationAirport.setThreshold(1);
        txtDepartureDate.setOnClickListener(this);
        btnGetSchedules.setOnClickListener(this);
        onItemClickListener();
    }

    private void initializeAutoComplete() {
        List<Airport> airportList = airportViewModel.getAirportList().getValue();
        AirportSearchAdapter airportSearchAdapter = new AirportSearchAdapter(context, R.layout.airport_item_view, airportList);
        autoOriginAirport.setAdapter(airportSearchAdapter);
        autoDestinationAirport.setAdapter(airportSearchAdapter);
    }

    private void onItemClickListener() {
        autoDestinationAirport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppHelper.hideSoftKeyboard(context);
                destinationAirport = (Airport) parent.getItemAtPosition(position);
            }
        });

        autoOriginAirport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppHelper.hideSoftKeyboard(context);
                originAirport = (Airport) parent.getItemAtPosition(position);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (autoDestinationAirport.getText().hashCode() == s.hashCode()) {
            if (s.toString().length() > 0) {
                AppHelper.setDrawable(autoDestinationAirport, DrawablePosition.RIGHT, R.drawable.ic_cancel_black_36dp);
            } else {
                AppHelper.setDrawable(autoDestinationAirport, DrawablePosition.NONE, R.drawable.ic_cancel_black_36dp);
            }
            return;
        }
        if (autoOriginAirport.getText().hashCode() == s.hashCode()) {
            if (s.toString().length() > 0) {
                AppHelper.setDrawable(autoOriginAirport, DrawablePosition.RIGHT, R.drawable.ic_cancel_black_36dp);
            } else {
                AppHelper.setDrawable(autoOriginAirport, DrawablePosition.NONE, R.drawable.ic_cancel_black_36dp);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.performClick()) {
        }
        if (v == autoOriginAirport) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (autoOriginAirport.getWidth() - autoOriginAirport.getCompoundDrawables()[2].getBounds().width() - autoOriginAirport.getPaddingRight())) {
                    Toast.makeText(context, "clicked!!!!!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String strDate = year + "-" + monthOfYear + 1 + "-" + dayOfMonth;
        departureDate = AppHelper.getDateTimeFromString(strDate);
        txtDepartureDate.setText(departureDate);
    }

    @Override
    public void onClick(View v) {
        if (v == txtDepartureDate) {
            AppHelper.showDatePickerDialog(context, this);
            return;
        }
        if (v == btnGetSchedules) {
            if (originAirport == null) {
                Toast.makeText(context, R.string.select_origin_airport, Toast.LENGTH_SHORT).show();
                return;
            }
            if (destinationAirport == null) {
                Toast.makeText(context, R.string.select_destination_airport, Toast.LENGTH_SHORT).show();
                return;
            }
            if (departureDate == null || departureDate.length() < 1) {
                Toast.makeText(context, R.string.select_departure_date, Toast.LENGTH_SHORT).show();
                return;
            }
            if (originAirport.getAirportCode().equals(destinationAirport.getAirportCode())) {
                Toast.makeText(context, "Origin and Destination Cannot be same", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(ProgressDialog.DESTINATION_AIRPORT, destinationAirport.getAirportCode());
            bundle.putString(ProgressDialog.ORIGIN_AIRPORT, originAirport.getAirportCode());
            bundle.putString(ProgressDialog.DEPARTURE_DATE, departureDate);
            AppHelper.showDialog(context, ProgressDialog.newInstance(bundle, this));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDismiss(List<?> dataList, String err) {
        if (dataList != null && !dataList.isEmpty()) {
            AppHelper.openPage(context, ScheduleListFragment.newInstance(originAirport.getPosition(), destinationAirport.getPosition()), true);
        }
    }
}
