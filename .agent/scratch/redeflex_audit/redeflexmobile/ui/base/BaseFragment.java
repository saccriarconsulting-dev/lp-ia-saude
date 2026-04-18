package com.axys.redeflexmobile.ui.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;
import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.shared.util.Alerta;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public abstract class BaseFragment<T extends BaseActivityView> extends DaggerFragment implements
        BaseView, TextWatcher {

    protected T parentActivity;
    private Unbinder unbinder;

    @LayoutRes
    public abstract int getContentRes();

    public abstract void initialize();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = (T) getActivity();
    }

    @Override
    public void showLoading() {
        this.parentActivity.showLoading();
    }

    @Override
    public void hideLoading() {
        this.parentActivity.hideLoading();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentRes(), container, false);
//        DeviceUtils.closeKeyboardOnTouch(requireContext(), view);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initialize();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("%s HAS BEING STOPPED", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("%s HAS BEING DESTROYED", this.getClass().getSimpleName());
    }

    @Override
    public String getStringByResId(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public String getStringByResId(int stringResource, Object... args) {
        return getString(stringResource, args);
    }

    @Override
    public void showOneButtonDialog(@Nullable String title,
                                    @Nullable String message,
                                    @Nullable View.OnClickListener buttonCallback) {
        Alerta alerta = new Alerta(requireContext(), getResources().getString(R.string.app_name), message);
        alerta.show((dialog, which) -> {
            if (buttonCallback != null) buttonCallback.onClick(null);
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // unused
    }

    @Override
    public void afterTextChanged(Editable s) {
        // unused
    }
}
