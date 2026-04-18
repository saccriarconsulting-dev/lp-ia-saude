package com.axys.redeflexmobile.ui.component;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Rogério Massa on 20/06/2018.
 */

public class ComponentProgressLoading extends LinearLayout {

    @BindView(R.id.loading) RelativeLayout rlLoading;

    private Context context;

    public ComponentProgressLoading(Context context) {
        super(context);
        initialize(context);
    }

    public ComponentProgressLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View view = inflater.inflate(R.layout.component_progress_loading, this);
            ButterKnife.bind(this, view);
        }
    }

    public void showLoading() {
        rlLoading.setVisibility(VISIBLE);
        this.closeKeyboard();
        this.blockBackButton(true);
    }

    public void hideLoading() {
        rlLoading.setVisibility(GONE);
        this.closeKeyboard();
        this.blockBackButton(false);
    }

    private void closeKeyboard() {
        Activity activity = (Activity) context;
        if (activity != null) {
            DeviceUtils.closeKeyboard(activity);
        }
    }

    private void blockBackButton(boolean block) {
        BaseActivity activity = (BaseActivity) context;
        if (activity != null) {
            activity.setBackButtonActivated(block);
        }
    }
}
