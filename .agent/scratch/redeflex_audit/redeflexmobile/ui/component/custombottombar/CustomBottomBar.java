package com.axys.redeflexmobile.ui.component.custombottombar;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.component.custombottombar.CustomBottomBarModel.CustomBottomBarModelTypeEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.axys.redeflexmobile.ui.component.custombottombar.CustomBottomBarItem.ICustomBottomBarItemListener;

/**
 * Created by Rogério Massa on 19/07/2018.
 */

public class CustomBottomBar extends LinearLayout implements ICustomBottomBarItemListener {

    @BindView(R.id.cpt_bottom_bar_ll_container) LinearLayout llContainer;

    private Context context;
    private List<CustomBottomBarModel> objects;
    private ICustomBottomBarListener callback;
    private CustomBottomBarModel selectedItem;

    public CustomBottomBar(Context context) {
        super(context);
    }

    public CustomBottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return;
        }
        View view = inflater.inflate(R.layout.component_bottom_bar, this);
        ButterKnife.bind(this, view);
    }

    private void initializeItems() {
        llContainer.removeAllViews();
        for (CustomBottomBarModel listItem : this.objects) {
            CustomBottomBarItem componentChild = new CustomBottomBarItem(context, llContainer,
                    listItem, this);
            llContainer.addView(componentChild.getView());
        }
    }

    public void setButtons(List<CustomBottomBarModel> objects) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        this.objects = objects;
        this.initializeItems();
    }

    public void setCallback(ICustomBottomBarListener callback) {
        this.callback = callback;
    }

    public void initializeSelectItem(CustomBottomBarModelTypeEnum type) {
        if (type == null) {
            return;
        }

        for (CustomBottomBarModel object : this.objects) {
            if (type.equals(object.getType())) {
                this.doSelectItem(object);
            }
        }
    }

    @Override
    public boolean isItemSelected(CustomBottomBarModel object) {
        return this.selectedItem != null && this.selectedItem.equals(object);
    }

    @Override
    public void doSelectItem(CustomBottomBarModel object) {
        if (object == null
                || object.getTarget() == null
                || this.selectedItem != null
                && this.selectedItem.getTarget() != null
                && this.selectedItem.getTarget().getClass().getName()
                .equals(object.getTarget().getClass().getName())) {
            return;
        }

        if (object.getTarget() != null) {
            this.selectedItem = object;
            this.initializeItems();
        }

        if (this.callback != null) {
            this.callback.onBottomBarOptionSelect(object);
        }
    }

    public interface ICustomBottomBarListener {
        void onBottomBarOptionSelect(CustomBottomBarModel object);
    }
}
