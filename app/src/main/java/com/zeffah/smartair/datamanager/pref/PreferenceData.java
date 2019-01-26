package com.zeffah.smartair.datamanager.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.zeffah.smartair.datamanager.pojo.Token;

public class PreferenceData {
    private Context context;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor prefEditor;
    private final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private final String KEY_EXPIRY_DURATION = "KEY_EXPIRY_DURATION";

    public PreferenceData(Context context) {
        this.context = context;
        String AUTHORIZATION = "AUTHORIZATION";
        sharedPreference = context.getSharedPreferences(AUTHORIZATION, Context.MODE_PRIVATE);
    }

    public void setToken(Token token){
        prefEditor = sharedPreference.edit();
        prefEditor.putString(KEY_ACCESS_TOKEN, token.accessToken);
        prefEditor.putLong(KEY_EXPIRY_DURATION, token.expiryDuration);
        if (prefEditor.commit()){
            prefEditor.apply();
        }
    }

    public Token getToken() {
        Token token = new Token();
        token.accessToken = sharedPreference.getString(KEY_ACCESS_TOKEN, null);
        token.expiryDuration = sharedPreference.getLong(KEY_EXPIRY_DURATION, 0);
        return token;
    }
}
