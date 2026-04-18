package com.axys.redeflexmobile.ui.adquirencia.visit.catalog;

import android.annotation.SuppressLint;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity;
import com.axys.redeflexmobile.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Rogério Massa on 04/01/19.
 */
@SuppressLint("NonConstantResourceId")
public class VisitProspectCatalogFragment extends BaseFragment<VisitProspectActivity> implements
        VisitProspectCatalogView {

    @Inject VisitProspectCatalogPresenter presenter;

    @BindView(R.id.visit_prospect_empty_view) LinearLayout emptyView;
    @BindView(R.id.visit_prospect_catalog_vp_pager) ViewPager vpPager;
    @BindView(R.id.visit_prospect_catalog_ll_controls) LinearLayout llControls;

    public static VisitProspectCatalogFragment newInstance() {
        return new VisitProspectCatalogFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_catalog;
    }

    @Override
    public void initialize() {
        parentActivity.setToolbarTitle("Catálogo");
        initializeEmptyLayout();
        presenter.getImages();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void fillImages(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            showEmptyLayout();
        } else {
            initializeViewPager(urls);
        }
    }

    private void initializeEmptyLayout() {
        TextView emptyViewTitle = emptyView.findViewById(R.id.empty_view_title);
        emptyViewTitle.setText("Imagens indisponíveis");
        TextView emptyViewText = emptyView.findViewById(R.id.empty_view_text);
        emptyViewText.setText("Não possuem imagens do catálogo \n para serem exibidas.");
    }

    private void initializeViewPager(List<String> urls) {
        VisitProspectCatalogPagerAdapter pagerAdapter = new VisitProspectCatalogPagerAdapter(
                getChildFragmentManager(), llControls, urls);
        vpPager.addOnPageChangeListener(pagerAdapter);
        vpPager.setAdapter(pagerAdapter);
    }

    private void showEmptyLayout() {
        vpPager.setVisibility(View.GONE);
        llControls.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
}
