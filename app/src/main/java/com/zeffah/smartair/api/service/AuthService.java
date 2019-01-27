package com.zeffah.smartair.api.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zeffah.smartair.api.ApiInterface;
import com.zeffah.smartair.callback.OnAuthCallback;
import com.zeffah.smartair.datamanager.pojo.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zeffah.smartair.helper.AppHelper.getApi;

public class AuthService {
    public static String getAuthToken(String clientId, String clientSecret, String grantType) {
        {
            ApiInterface api = getApi;
            String token = null;
            try {
                Token response = api.authenticate(clientId, clientSecret, grantType).execute().body();
                if (response != null) {
                    JSONObject object = new JSONObject(response.toString());
                    token = object.getString("access_token");
                }
            } catch (IOException | JSONException e) {
                token = null;
            }
            return token;
        }

    }

    public static void getAuthToken(String clientId, String clientSecret, String grantType, final OnAuthCallback authCallback) {
        ApiInterface getToken = getApi;
        final Call<Token> token = getToken.authenticate(clientId, clientSecret, grantType);
        token.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                ResponseBody responseBody = response.errorBody();
                Token object = response.body();
                try {
                    if (object != null) {
                        authCallback.authSuccess(object);
                    } else {
                        if (response.code() != 200) {
                            if (responseBody != null) {
                                authCallback.authFailed(responseBody.string());
                            }
                        }
                    }
                } catch (Exception e) {
                    authCallback.authFailed("Unknown Error. Please try again.");
                    Log.d("Auth_token_error_1", "Unknown Error. Please try again. => " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                authCallback.authFailed("Network Error.\n\nCheck your internet Connection");
                Log.d("Auth_token_error_2", t.getMessage());
            }
        });
    }
}
