package com.axys.redeflexmobile.ui.adquirencia.ranking;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener;
import androidx.viewpager.widget.ViewPager;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class RankingActivity extends BaseActivity implements RankingView {

    @Inject RankingPresenter presenter;

    @BindView(R.id.ranking_vp_options) ViewPager vpOptions;
    @BindView(R.id.ranking_tl_options) TabLayout tlOptions;
    @BindView(R.id.ranking_cpl_loading) ComponentProgressLoading cplLoading;

    private RankingPagerAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_ranking;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    public void initialize() {
        setTitle("Ranking");
        showBackButtonToolbar();
        adapter = new RankingPagerAdapter(getSupportFragmentManager(), this);
        presenter.getRankings();
    }

    @Override
    public void onGetRankingSuccess(List<Ranking> rankings) {
        adapter.setRankingList(rankings);
        adapter.initializeFragments();
        initializeTabs();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    private void initializeTabs() {
        adapter.setTabSelectedListener(tab -> vpOptions.setCurrentItem(tab.getPosition()));
        adapter.setTabLayout(tlOptions);
        vpOptions.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tlOptions));
        vpOptions.setAdapter(adapter);
        tlOptions.setupWithViewPager(vpOptions);
        tlOptions.addOnTabSelectedListener(adapter);
        adapter.initializeColorScheme();
    }
}
