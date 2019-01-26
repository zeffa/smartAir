package com.zeffah.smartair;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zeffah.smartair.api.ApiKeys;
import com.zeffah.smartair.api.service.AuthService;
import com.zeffah.smartair.callback.OnAuthCallback;
import com.zeffah.smartair.datamanager.pojo.Token;
import com.zeffah.smartair.datamanager.pref.PreferenceData;
import com.zeffah.smartair.helper.AppHelper;
import com.zeffah.smartair.pages.LandingPageFragment;

public class MainActivity extends AppCompatActivity implements OnAuthCallback {
    private PreferenceData prefData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefData = new PreferenceData(this);
        AuthService.getAuthToken(ApiKeys.CLIENT_ID, ApiKeys.CLIENT_SECRET, ApiKeys.GRANT_TYPE, MainActivity.this);
        AppHelper.openPage(this, new LandingPageFragment(), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void authSuccess(Token token) {
        Log.d("Auth_token_success_home", token.accessToken);
        prefData.setToken(token);
    }

    @Override
    public void authFailed(String error) {
        Log.d("Auth_token_error_home", error);
    }
}
