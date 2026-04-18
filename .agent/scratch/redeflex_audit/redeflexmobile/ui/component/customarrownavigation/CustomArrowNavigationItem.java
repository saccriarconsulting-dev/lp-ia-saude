package com.axys.redeflexmobile.ui.component.customarrownavigation;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.BEGIN;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.END;
import static com.axys.redeflexmobile.ui.component.customarrownavigation.CustomArrowNavigationItem.CustomArrowNavigationItemPosition.MIDDLE;

/**
 * @author Bruno Pimentel on 27/11/18.
 */
public class CustomArrowNavigationItem {

    private int index;
    private Fragment target;

    public CustomArrowNavigationItem(Fragment target) {
        this.target = target;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Fragment getTarget() {
        return target;
    }

    public void setTarget(Fragment target) {
        this.target = target;
    }

    static ImageView getImageView(Context context,
                                  boolean selected,
                                  CustomArrowNavigationItemPosition position) {

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(selected
                ? R.drawable.rounded_background_primary_dark
                : R.drawable.rounded_background_grey);

        int normal = (int) context.getResources().getDimension(R.dimen.spacing_small_extra);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(normal, normal);

        int tiny = (int) context.getResources().getDimension(R.dimen.spacing_tiny);
        layoutParams.setMargins(position == MIDDLE || position == END ? tiny : EMPTY_INT,
                EMPTY_INT,
                position == MIDDLE || position == BEGIN ? tiny : EMPTY_INT,
                EMPTY_INT);

        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    public enum CustomArrowNavigationItemPosition {
        BEGIN, MIDDLE, END
    }
}
