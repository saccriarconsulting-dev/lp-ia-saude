package com.axys.redeflexmobile.ui.adquirencia.routes;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.PagerAdapterItem;
import com.axys.redeflexmobile.ui.adquirencia.routes.list.RoutesProspectListFragment;

import java.util.Arrays;
import java.util.List;

import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.FRIDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.MONDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.SATURDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.SUNDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.THURSDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.TUESDAY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana.WEDNESDAY;

/**
 * @author Rogério Massa on 29/10/18.
 */

public class RoutesProspectPagerAdapter extends FragmentPagerAdapter implements TabLayout.BaseOnTabSelectedListener {

    private IMainTabSelectedListener tabSelectedListener;
    private List<PagerAdapterItem> items;

    RoutesProspectPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.items = Arrays.asList(
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_monday),
                        RoutesProspectListFragment.newInstance(MONDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_tuesday),
                        RoutesProspectListFragment.newInstance(TUESDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_wednesday),
                        RoutesProspectListFragment.newInstance(WEDNESDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_thursday),
                        RoutesProspectListFragment.newInstance(THURSDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_friday),
                        RoutesProspectListFragment.newInstance(FRIDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_saturday),
                        RoutesProspectListFragment.newInstance(SATURDAY)),
                new PagerAdapterItem(context.getString(R.string.frg_main_routes_tab_sunday),
                        RoutesProspectListFragment.newInstance(SUNDAY)));
    }

    void setTabSelectedListener(IMainTabSelectedListener tabAdapterListener) {
        this.tabSelectedListener = tabAdapterListener;
    }

    @Override
    public Fragment getItem(int position) {
        return this.items.get(position).fragment;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.items.get(position).title;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // unused
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // unused
    }

    public interface IMainTabSelectedListener {
        void onTabSelected(TabLayout.Tab tab);
    }
}
