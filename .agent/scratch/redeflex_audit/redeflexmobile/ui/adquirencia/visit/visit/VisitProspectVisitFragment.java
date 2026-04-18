package com.axys.redeflexmobile.ui.adquirencia.visit.visit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivity;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity.VisitProspectActivityListener;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectView;
import com.axys.redeflexmobile.ui.adquirencia.visit.cancel.VisitProspectCancelFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.catalog.VisitProspectCatalogFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.quality.VisitProspectQualityFragment;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivity.PROSPECT_ID;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.FLAG_FACADE_ATTACHMENT;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.FLAG_PROSPECT;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.REGISTER_FLOW_FLAG;

/**
 * @author Rogério Massa on 03/01/19.
 */

public class VisitProspectVisitFragment extends BaseFragment<VisitProspectView> implements
        VisitProspectVisitView,
        VisitProspectActivityListener,
        TextWatcher {

    @BindView(R.id.visit_prospect_card_tv_timer) TextView cardTvTimer;
    @BindView(R.id.visit_prospect_card_tv_name) TextView cardTvName;
    @BindView(R.id.visit_prospect_card_iv_info) ImageView ivInfo;
    @BindView(R.id.visit_prospect_bt_proposal) Button btProposal;
    @BindView(R.id.visit_prospect_bt_catalog) Button btCatalog;
    @BindView(R.id.visit_prospect_bt_quality) Button btQuality;
    @BindView(R.id.visit_prospect_et_obs) CustomEditText cetObs;
    @BindView(R.id.visit_prospect_bt_cancel) Button btCancel;

    private CompositeDisposable compositeDisposable;

    public static VisitProspectVisitFragment newInstance() {
        return new VisitProspectVisitFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_visit;
    }

    @Override
    public void initialize() {
        parentActivity.setToolbarTitle("Visita");
        initializeEvents();

        VisitProspect visit = parentActivity.getVisit();
        if (visit.getIdCustomer() != null && visit.getIdCustomer() > EMPTY_INT) {
            prepareQualityVisit();
        } else {
            prepareProspectVisit();
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        this.onCancelClick();
    }

    @Override
    public void setTimer(String time) {
        if (cardTvTimer == null) return;
        cardTvTimer.setText(time);
    }

    @Override
    public void afterTextChanged(Editable s) {
        parentActivity.setObservation(s.toString());
    }

    @Override
    public void lockScreen() {
        this.ivInfo.setClickable(false);
        this.btProposal.setClickable(false);
        this.btCatalog.setClickable(false);
        this.btQuality.setClickable(false);
        this.cetObs.setClickable(false);
        this.btCancel.setClickable(false);
    }

    private void initializeEvents() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxView.clicks(ivInfo)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> validateActions(ivInfo, v -> onInfoClick()), Timber::d));
        compositeDisposable.add(RxView.clicks(btProposal)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> validateActions(btProposal, v -> onProposalClick()), Timber::d));
        compositeDisposable.add(RxView.clicks(btCatalog)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> validateActions(btCatalog, v -> onCatalogClick()), Timber::d));
        compositeDisposable.add(RxView.clicks(btQuality)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> validateActions(btQuality, v -> onQualityClick()), Timber::d));
        compositeDisposable.add(RxView.clicks(btCancel)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onCancelClick(), Timber::d));
    }

    private void prepareProspectVisit() {
        btProposal.setVisibility(View.VISIBLE);
        btCatalog.setVisibility(View.VISIBLE);
        btQuality.setVisibility(View.GONE);
        cetObs.setVisibility(View.GONE);
    }

    private void prepareQualityVisit() {
        btProposal.setVisibility(View.GONE);
        btCatalog.setVisibility(View.GONE);
        btQuality.setVisibility(View.VISIBLE);
        cetObs.setVisibility(View.VISIBLE);
        cetObs.addTextChangedListener(this);

        String observation = parentActivity.getObservation();
        if (StringUtils.isNotEmpty(observation)) cetObs.setText(observation);
    }

    public void validateActions(View view, View.OnClickListener callback) {
        if (!Validacoes.validacaoDataAparelhoAdquirencia(getContext())) {
            return;
        }

        if (callback != null) {
            callback.onClick(view);
        }
    }

    private void onInfoClick() {
        RouteClientProspect prospect = parentActivity.getCustomer();

        if (prospect == null) {
            return;
        }

        if (prospect.getType() == RouteClientProspect.TYPE_CUSTOMER) {
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, String.valueOf(parentActivity.getCustomerId()));
            Utilidades.openNewActivity(requireContext(), ClienteInfoActivity.class, bundle, false);
            return;
        }

        if (prospect.getType() == RouteClientProspect.TYPE_PROSPECT) {
            Bundle bundle = new Bundle();
            bundle.putInt(PROSPECT_ID, parentActivity.getCustomerId());
            Utilidades.openNewActivity(requireContext(), ProspectInfoActivity.class, bundle, false);
        }
    }

    private void onProposalClick() {

        Bundle bundle = new Bundle();
        bundle.putInt(FLAG_PROSPECT, parentActivity.getCustomerId());

        VisitProspectAttachment visitProspectAttachment = parentActivity.getVisitProspectAttachment();
        if (visitProspectAttachment != null) {
            bundle.putString(FLAG_FACADE_ATTACHMENT,
                    Utilidades.getJsonFromClass(new CustomerRegisterAttachment(visitProspectAttachment)));
        }

        Utilidades.openNewActivityForResult(requireContext(),
                RegisterCustomerActivity.class, REGISTER_FLOW_FLAG, bundle);
    }

    private void onCatalogClick() {
        parentActivity.openFragment(VisitProspectCatalogFragment.newInstance());
    }

    private void onQualityClick() {
        parentActivity.openFragment(VisitProspectQualityFragment.newInstance());
    }

    private void onCancelClick() {
        parentActivity.openFragment(VisitProspectCancelFragment.newInstance());
    }
}
