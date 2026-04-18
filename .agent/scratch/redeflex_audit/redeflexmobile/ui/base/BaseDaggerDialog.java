package com.axys.redeflexmobile.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.axys.redeflexmobile.shared.mvp.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerDialogFragment;

public abstract class BaseDaggerDialog extends DaggerDialogFragment implements BaseView {

    private Unbinder unbinder;
    private View view;

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void viewCreated(View view, @Nullable Bundle savedInstanceState);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(mustCancelable());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentView(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        throw new UnsupportedOperationException("must be implemented");
    }

    @Override
    public void hideLoading() {
        throw new UnsupportedOperationException("must be implemented");
    }

    @Override
    public String getStringByResId(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public String getStringByResId(int stringResource, Object... args) {
        return getString(stringResource, args);
    }

    protected boolean mustCancelable() {
        return true;
    }
}
