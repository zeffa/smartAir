package com.zeffah.smartair.pages;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.zeffah.smartair.MainActivity;
import com.zeffah.smartair.R;
import com.zeffah.smartair.api.service.AuthService;
import com.zeffah.smartair.callback.OnAuthCallback;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.datamanager.pref.PreferenceData;
import com.zeffah.smartair.helper.AppHelper;

import java.util.Calendar;
import java.util.Locale;

import static com.zeffah.smartair.api.ApiKeys.CLIENT_ID;
import static com.zeffah.smartair.api.ApiKeys.CLIENT_SECRET;
import static com.zeffah.smartair.api.ApiKeys.GRANT_TYPE;

public class SplashScreenActivity extends AppCompatActivity implements OnAuthCallback {
    private AVLoadingIndicatorView loadingIndicatorView;
    private PreferenceData preferenceData;
    private TextView txtCompanyName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        init();
        AuthService.getAuthToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, this);
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        TextView txtCompanyCopyright = findViewById(R.id.txt_company_copyright);
        txtCompanyCopyright.setText(getResources().getString(R.string.copyright, calendar.get(Calendar.YEAR)));
        preferenceData = new PreferenceData(this);
        txtCompanyName = findViewById(R.id.txt_company_name);
        loadingIndicatorView = findViewById(R.id.avi);
        loadingIndicatorView.smoothToShow();
    }

    @Override
    public void authSuccess(Token token) {
        preferenceData.setToken(token);
        loadingIndicatorView.smoothToHide();
        AppHelper.launchActivity(SplashScreenActivity.this, MainActivity.class);
    }

    @Override
    public void authFailed(String error) {
        loadingIndicatorView.smoothToHide();
        txtCompanyName.setText(error);
        txtCompanyName.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
    }
}
