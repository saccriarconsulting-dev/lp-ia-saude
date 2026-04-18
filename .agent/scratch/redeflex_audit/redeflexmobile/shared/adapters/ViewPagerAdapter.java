package com.axys.redeflexmobile.shared.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 25/08/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titulos = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String titulo) {
        fragments.add(fragment);
        titulos.add(titulo);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulos.get(position);
    }

    public void clearFragments() {
        fragments.clear();
        titulos.clear();
    }
}