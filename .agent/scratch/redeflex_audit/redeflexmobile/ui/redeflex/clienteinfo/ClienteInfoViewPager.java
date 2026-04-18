package com.axys.redeflexmobile.ui.redeflex.clienteinfo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Rogério Massa on 07/12/18.
 */

public class ClienteInfoViewPager extends ViewPager {

    public ClienteInfoViewPager(@NonNull Context context) {
        super(context);
    }

    public ClienteInfoViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View view = getChildAt(this.getCurrentItem());
        if (view != null) view.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) return specSize;
        if (view != null) result = view.getMeasuredHeight();
        if (specMode == MeasureSpec.AT_MOST) result = Math.min(result, specSize);
        return result;
    }
}
