package com.axys.redeflexmobile.shared.manager;

import android.content.Context;
import androidx.annotation.StringRes;

public class ProvideStringImpl implements ProvideString {

    private final Context context;

    public ProvideStringImpl(final Context context) {
        this.context = context;
    }

    public String getString(@StringRes final int string) {
        return context.getString(string);
    }
}
