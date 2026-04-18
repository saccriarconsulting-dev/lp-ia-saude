package com.axys.redeflexmobile.shared.mvp;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.view.View;

public interface BaseView {

    void showLoading();

    void hideLoading();

    String getStringByResId(int resourceId);

    String getStringByResId(@StringRes int stringResource, Object... args);

    default void showOneButtonDialog(@Nullable String title,
                                     @Nullable String message,
                                     @Nullable View.OnClickListener buttonCallback) {
    }

}
