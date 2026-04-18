package com.axys.redeflexmobile.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.DeviceUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private static final int CLICK_INTERVAL = 1000;
    private long lastClick = 0;
    private Dialog dialog;
    private Context context;
    private Window window;
    private Unbinder unbinder;
    private View view;

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void initialize(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.getDecorView().setBackgroundResource(R.drawable.rounded_corners_dialog_alert);
        }
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = getDialog();
        context = getContext();
        initialize(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (window != null && getContext() != null) {
            boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
            Point point = DeviceUtils.getScreenSizeInPixel(requireContext());
            int width = isTablet ? ((int) (point.x * 0.75f)) : ViewGroup.LayoutParams.MATCH_PARENT;
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View view) {
        long diferenca = calcularDiferenca();
        if (diferenca < CLICK_INTERVAL) {
            return;
        }
        lastClick = SystemClock.elapsedRealtime();
        onItemClick(view);
    }

    protected void onItemClick(View view) {
        dismiss();
    }

    private long calcularDiferenca() {
        return SystemClock.elapsedRealtime() - lastClick;
    }

    void fecharDialog() {
        if (dialog.isShowing()) {
            fecharTeclado();
            dialog.dismiss();
        }
    }

    @SuppressLint("LogNotTimber")
    private void fecharTeclado() {
        if (getActivity() == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
    }

    Context context() {
        return context;
    }

    protected Activity activity() {
        return getActivity();
    }

    public void showLoading() {
        throw new UnsupportedOperationException("must be implemented");
    }

    public void hideLoading() {
        throw new UnsupportedOperationException("must be implemented");
    }

    public String getStringByResId(int resourceId) {
        return getString(resourceId);
    }

    public String getStringByResId(int stringResource, Object... args) {
        return getString(stringResource, args);
    }

    public Resources getViewResources() {
        return getResources();
    }

    public View getMyView() {
        return view;
    }

    protected boolean mustCancelable() {
        return true;
    }

    public void showOneButtonDialog(@Nullable String title,
                             @Nullable String message,
                             @Nullable View.OnClickListener buttonCallback) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
