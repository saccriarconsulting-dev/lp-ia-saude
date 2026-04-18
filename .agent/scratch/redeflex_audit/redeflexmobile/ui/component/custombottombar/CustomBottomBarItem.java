package com.axys.redeflexmobile.ui.component.custombottombar;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rogério Massa on 19/07/2018.
 */

class CustomBottomBarItem {

    @BindView(R.id.cpt_bottom_bar_item_ll_container) LinearLayout llContainer;
    @BindView(R.id.cpt_bottom_bar_item_iv_icon) ImageView ivIcon;
    @BindView(R.id.cpt_bottom_bar_item_tv_description) TextView tvDescription;

    private Context context;
    private ViewGroup viewGroup;
    private View view;

    private CustomBottomBarModel object;
    private ICustomBottomBarItemListener listener;

    CustomBottomBarItem(Context context,
                        ViewGroup viewGroup,
                        CustomBottomBarModel object,
                        ICustomBottomBarItemListener listener) {
        this.context = context;
        this.viewGroup = viewGroup;
        this.object = object;
        this.listener = listener;
        this.initialize();
    }

    private void initialize() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        if (inflater == null) {
            return;
        }

        this.view = inflater.inflate(R.layout.component_bottom_bar_item, this.viewGroup, false);
        ButterKnife.bind(this, view);

        this.llContainer.setOnClickListener(v ->
                CustomBottomBarItem.this.listener.doSelectItem(CustomBottomBarItem.this.object));
        this.initializeValues();
    }

    private void initializeValues() {
        if (this.object == null) {
            return;
        }

        if (StringUtils.isNotEmpty(this.object.getDescription())) {
            this.tvDescription.setText(this.object.getDescription());
            this.tvDescription.setTextColor(ContextCompat.getColor(this.context, R.color.white));
        }

        if (this.object.getIcon() != null) {
            this.ivIcon.setImageResource(this.object.getIcon());
        }

        if (!this.listener.isItemSelected(this.object)) {
            this.llContainer.setBackgroundColor(ContextCompat.getColor(this.context, R.color.cpt_bottom_bar_background));
        } else if (this.listener.isItemSelected(this.object)) {
            this.llContainer.setBackgroundColor(ContextCompat.getColor(this.context, R.color.cpt_bottom_bar_background_selected));
        }
    }

    public View getView() {
        return this.view;
    }

    interface ICustomBottomBarItemListener {
        boolean isItemSelected(CustomBottomBarModel object);

        void doSelectItem(CustomBottomBarModel object);
    }
}
