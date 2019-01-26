package com.zeffah.smartair.datamanager.pojo;

import com.google.gson.annotations.SerializedName;

public class AirportName {
    @SerializedName("Name")
    private NameInfo nameInfo;

    public NameInfo getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(NameInfo nameInfo) {
        this.nameInfo = nameInfo;
    }
}
