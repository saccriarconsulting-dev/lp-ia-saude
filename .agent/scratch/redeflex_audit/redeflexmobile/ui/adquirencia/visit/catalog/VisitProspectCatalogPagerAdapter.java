package com.axys.redeflexmobile.ui.adquirencia.visit.catalog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

import java.util.ArrayList;
import java.util.List;

import static com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogItemFragment.newInstance;

/**
 * @author Rogério Massa on 29/10/18.
 */

public class VisitProspectCatalogPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int CONTROL_SIZE = 40;
    private static final int CONTROL_MARGIN = 8;
    private static final int NO_MARGIN = 0;

    private List<Fragment> items;
    private LinearLayout llControls;

    VisitProspectCatalogPagerAdapter(FragmentManager fm, LinearLayout llControls, List<String> urls) {
        super(fm);
        this.llControls = llControls;
        this.items = new ArrayList<>();
        Stream.ofNullable(urls).forEach(url -> items.add(newInstance(url)));
        prepareControls(0);
    }

    @Override
    public Fragment getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public void onPageSelected(int i) {
        prepareControls(i);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        // unused
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        // unused
    }

    private void prepareControls(int selectedIndex) {
        llControls.removeAllViews();
        if (items == null || items.isEmpty()) return;
        for (int index = 0; index < items.size(); index++) {
            PagePosition position = index == 0
                    ? PagePosition.BEGIN : index == items.size() - 1
                    ? PagePosition.END : PagePosition.MIDDLE;
            llControls.addView(getControl(index == selectedIndex, position));
        }
    }

    private ImageView getControl(boolean activated, PagePosition position) {
        ImageView imageView = new ImageView(llControls.getContext());
        imageView.setImageResource(activated
                ? R.drawable.ic_catalog_control_selected
                : R.drawable.ic_catalog_control_unselected);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(CONTROL_SIZE, CONTROL_SIZE);
        if (position.equals(PagePosition.BEGIN)) {
            lParams.setMargins(NO_MARGIN, NO_MARGIN, CONTROL_MARGIN, NO_MARGIN);
        } else if (position.equals(PagePosition.MIDDLE)) {
            lParams.setMargins(CONTROL_MARGIN, NO_MARGIN, CONTROL_MARGIN, NO_MARGIN);
        } else {
            lParams.setMargins(CONTROL_MARGIN, NO_MARGIN, NO_MARGIN, NO_MARGIN);
        }
        imageView.setLayoutParams(lParams);
        return imageView;
    }

    private enum PagePosition {
        BEGIN, MIDDLE, END
    }
}
