package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expires_in")
    public long expiryDuration;
}
