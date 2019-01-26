package com.zeffah.smartair.callback;

import com.zeffah.smartair.datamanager.pojo.Token;

public interface OnAuthCallback {
    void authSuccess(Token token);
    void authFailed(String error);
}
