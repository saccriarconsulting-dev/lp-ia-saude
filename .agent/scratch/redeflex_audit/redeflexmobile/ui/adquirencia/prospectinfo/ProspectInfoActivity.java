package com.axys.redeflexmobile.ui.adquirencia.prospectinfo;

import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.axys.redeflexmobile.shared.util.NumberUtils.NEGATIVE_SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.maskCellPhone;
import static com.axys.redeflexmobile.shared.util.StringUtils.maskPhone;
import static com.axys.redeflexmobile.shared.util.StringUtils.maskPostalCode;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */

public class ProspectInfoActivity extends BaseActivity implements ProspectInfoView {

    public static final String PROSPECT_ID = "PROSPECT_ID";

    @Inject ProspectInfoPresenter presenter;

    @BindView(R.id.prospect_info_tv_prospect) TextView tvProspect;

    @BindView(R.id.prospect_info_nsv_container) NestedScrollView nsvContainer;
    @BindView(R.id.prospect_info_ll_fantasy_name) LinearLayout llFantasyName;
    @BindView(R.id.prospect_info_tv_fantasy_name) TextView tvFantasyName;
    @BindView(R.id.prospect_info_ll_contact) LinearLayout llContact;
    @BindView(R.id.prospect_info_tv_contact) TextView tvContact;
    @BindView(R.id.prospect_info_ll_phone) LinearLayout llPhone;
    @BindView(R.id.prospect_info_tv_phone) TextView tvPhone;
    @BindView(R.id.prospect_info_ll_cellphone) LinearLayout llCellPhone;
    @BindView(R.id.prospect_info_tv_cellphone) TextView tvCellPhone;
    @BindView(R.id.prospect_info_ll_email) LinearLayout llEmail;
    @BindView(R.id.prospect_info_tv_email) TextView tvEmail;
    @BindView(R.id.prospect_info_ll_type) LinearLayout llType;
    @BindView(R.id.prospect_info_tv_type) TextView tvType;
    @BindView(R.id.prospect_info_ll_address_name) LinearLayout llAddressName;
    @BindView(R.id.prospect_info_tv_address_name) TextView tvAddressName;
    @BindView(R.id.prospect_info_ll_address_number) LinearLayout llAddressNumber;
    @BindView(R.id.prospect_info_tv_address_number) TextView tvAddressNumber;
    @BindView(R.id.prospect_info_ll_neighborhood) LinearLayout llNeighborhood;
    @BindView(R.id.prospect_info_tv_neighborhood) TextView tvNeighborhood;
    @BindView(R.id.prospect_info_ll_city) LinearLayout llCity;
    @BindView(R.id.prospect_info_tv_city) TextView tvCity;
    @BindView(R.id.prospect_info_ll_federal_state) LinearLayout llFederalState;
    @BindView(R.id.prospect_info_tv_federal_state) TextView tvFederalState;
    @BindView(R.id.prospect_info_ll_postal_code) LinearLayout llPostalCode;
    @BindView(R.id.prospect_info_tv_postal_code) TextView tvPostalCode;
    @BindView(R.id.prospect_info_ll_address_complement) LinearLayout llAddressComplement;
    @BindView(R.id.prospect_info_tv_address_complement) TextView tvAddressComplement;

    private RouteClientProspect prospect;

    @Override
    protected int getContentView() {
        return R.layout.activity_prospect_info;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.prospect_info_activity_name);
        }

        int prospectId = NEGATIVE_SINGLE_INT;

        if (bundle != null) {
            prospectId = bundle.getInt(PROSPECT_ID, NEGATIVE_SINGLE_INT);
        }

        if (prospectId == NEGATIVE_SINGLE_INT) {
            return;
        }

        presenter.getProspectInfo(prospectId);
    }

    @Override
    public void fillProspectInfo(RouteClientProspect prospect) {
        this.prospect = prospect;

        if (prospect == null) {
            return;
        }

        fillValues();
        nsvContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void getProspectInfoError(String error) {
        showOneButtonDialog(getString(R.string.app_name), error, v -> onBackPressed());
    }

    private void fillValues() {
        tvProspect.setText(getStringByResId(R.string.prospect_info_tv_fantasy_name_value, prospect.getId(), prospect.getNameFull()));
        tvFantasyName.setText(prospect.getNameFantasy());
        tvContact.setText(prospect.getContact());
        tvPhone.setText(maskPhone(prospect.getDddTelephone() + prospect.getTelephone()));
        tvCellPhone.setText(maskCellPhone(prospect.getDddCellphone() + prospect.getCellphone()));
        tvEmail.setText(prospect.getEmail());
        tvType.setText(prospect.getAddressType());
        tvAddressName.setText(prospect.getAddressName());
        tvAddressNumber.setText(prospect.getAddressNumber());
        tvNeighborhood.setText(prospect.getNeighborhood());
        tvCity.setText(prospect.getCity());
        tvFederalState.setText(prospect.getFederalState());

        if (prospect.getPostalCode() != null) {
            tvPostalCode.setText(maskPostalCode(prospect.getPostalCode()));
        }

        tvAddressComplement.setText(prospect.getAddressComplement());

        hideEmptyFields();
    }

    private void hideEmptyFields() {
        if (tvProspect.getText().toString().isEmpty()) {
            tvProspect.setVisibility(View.GONE);
        }

        if (tvFantasyName.getText().toString().isEmpty()) {
            llFantasyName.setVisibility(View.GONE);
        }

        if (tvContact.getText().toString().isEmpty()) {
            llContact.setVisibility(View.GONE);
        }

        if (tvPhone.getText().toString().isEmpty()) {
            llPhone.setVisibility(View.GONE);
        }

        if (tvCellPhone.getText().toString().isEmpty()) {
            llCellPhone.setVisibility(View.GONE);
        }

        if (tvEmail.getText().toString().isEmpty()) {
            llEmail.setVisibility(View.GONE);
        }

        if (tvType.getText().toString().isEmpty()) {
            llType.setVisibility(View.GONE);
        }

        if (tvAddressName.getText().toString().isEmpty()) {
            llAddressName.setVisibility(View.GONE);
        }

        if (tvAddressNumber.getText().toString().isEmpty()) {
            llAddressNumber.setVisibility(View.GONE);
        }

        if (tvNeighborhood.getText().toString().isEmpty()) {
            llNeighborhood.setVisibility(View.GONE);
        }

        if (tvCity.getText().toString().isEmpty()) {
            llCity.setVisibility(View.GONE);
        }

        if (tvFederalState.getText().toString().isEmpty()) {
            llFederalState.setVisibility(View.GONE);
        }

        if (tvPostalCode.getText().toString().isEmpty()) {
            llPostalCode.setVisibility(View.GONE);
        }

        if (tvAddressComplement.getText().toString().isEmpty()) {
            llAddressComplement.setVisibility(View.GONE);
        }
    }
}
