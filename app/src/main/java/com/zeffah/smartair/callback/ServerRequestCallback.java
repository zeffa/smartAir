package com.zeffah.smartair.callback;

import java.util.List;

public interface ServerRequestCallback {
    void requestSuccess(List<?> airports);

    void requestFailed(String error);
}
