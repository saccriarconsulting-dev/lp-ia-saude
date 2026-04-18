package com.axys.redeflexmobile.ui.adquirencia.routes;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity.FLAG_VISIT;

/**
 * @author Rogério Massa on 02/01/19.
 */

public class RoutesProspectActivity extends BaseActivity implements RoutesProspectView {

    @Inject RoutesProspectPresenter presenter;

    @BindView(R.id.routes_prospect_toolbar) Toolbar toolbar;
    @BindView(R.id.routes_prospect_cpl_loading) ComponentProgressLoading cplLoading;
    @BindView(R.id.routes_prospect_vp_options) ViewPager vpOptions;
    @BindView(R.id.routes_prospect_tl_options) TabLayout tlOptions;

    @Override
    protected int getContentView() {
        return R.layout.activity_routes_prospect;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    protected void initialize() {
        setSupportActionBar(toolbar);
        this.setTitle("Rotas");
        this.showBackButtonToolbar();
        presenter.getListItems();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void initializePager() {
        RoutesProspectPagerAdapter pagerAdapter = new RoutesProspectPagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.setTabSelectedListener(tab -> vpOptions.setCurrentItem(tab.getPosition()));
        vpOptions.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlOptions));
        vpOptions.setAdapter(pagerAdapter);
        tlOptions.setupWithViewPager(vpOptions);
        tlOptions.addOnTabSelectedListener(pagerAdapter);
        selectCurrentDay();
    }

    @Override
    public void showError(String message) {
        showOneButtonDialog(getString(R.string.app_name), message, v -> onBackPressed());
    }

    @Override
    public Colaborador getSalesman() {
        return presenter.getSalesman();
    }

    @Override
    public List<RoutesProspect> getRoutesProspect(EnumRotasDiasDaSemana dayOfWeek) {
        return presenter.getRoutesProspects(dayOfWeek);
    }

    @Override
    public List<RoutesProspect> getRoutesProspect(String filter) {
        return presenter.getRoutesProspects(filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FLAG_VISIT) presenter.getListItems();
    }

    private void selectCurrentDay() {
        int dow = DateUtils.getDayOfWeek();
        if (dow == 1) dow = 6;
        else dow = dow - 2;
        vpOptions.setCurrentItem(dow);
    }
}
