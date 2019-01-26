package com.zeffah.smartair.callback;

import java.util.List;

public interface DialogDismissListener {
    void onDismiss(List<?> dataList, String err);
}
