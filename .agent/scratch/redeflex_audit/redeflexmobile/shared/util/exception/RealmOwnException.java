package com.axys.redeflexmobile.shared.util.exception;

import androidx.annotation.StringRes;

public class RealmOwnException extends NullPointerException {

    private final int stringId;

    public RealmOwnException(@StringRes int stringId) {
        super();
        this.stringId = stringId;
    }

    public int getStringId() {
        return stringId;
    }
}
