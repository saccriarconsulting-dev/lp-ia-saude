package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_LIST;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;
import static com.axys.redeflexmobile.ui.redeflex.Config.KEY_INFO_CLIENT_MDR_TAXES;
import static com.axys.redeflexmobile.ui.redeflex.Config.KEY_INFO_CLIENT_MDR_TAXES_AUTOMATIC;

/**
 * @author lucasmarciano on 30/06/20
 */
@SuppressLint("NonConstantResourceId")
public class ClientMdrTaxesActivity extends BaseActivity implements ClientMdrTaxesView, ClientFlagsTaxesViewHolder.ClickListener {

    @BindView(R.id.pb_load_data) ProgressBar pbLoad;
    @BindView(R.id.cet_debit_tax) CustomEditText etDebitTax;
    @BindView(R.id.cet_tax_credit) CustomEditText etCreditTax;
    @BindView(R.id.cet_tax_credit_six_times) CustomEditText etCreditSixTax;
    @BindView(R.id.cet_tax_credit_twelve_times) CustomEditText etCreditTwelveTax;
    @BindView(R.id.cet_tax_credit_anticipation) CustomEditText etCreditAnticipationTax;
    @BindView(R.id.tv_flag_name) TextView tvFlagName;
    @BindView(R.id.sv_taxes) ScrollView svTaxes;
    @BindView(R.id.rv_flags) RecyclerView rvFlags;
    @BindView(R.id.client_mdr_taxes_tv_empty) AppCompatTextView tvEmpty;

    @Inject ClientMdrTaxesPresenter presenter;
    private ClientFlagsTaxesAdapter adapter;

    private boolean isAutomaticAnticipation = false;
    private String clientId;

    @Override
    protected int getContentView() {
        return R.layout.activity_client_mdr_taxes;
    }

    @Override
    protected void initialize() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clientId = bundle.getString(KEY_INFO_CLIENT_MDR_TAXES);
            isAutomaticAnticipation = bundle.getBoolean(KEY_INFO_CLIENT_MDR_TAXES_AUTOMATIC);

            presenter.loadFlags();
            configureToolbar();
        } else {
            Toast.makeText(this, R.string.message_not_found_client_taxes, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void fillAdapterFlags(List<FlagsBank> flagList) {
        if (flagList.size() > EMPTY_LIST) {
            adapter = new ClientFlagsTaxesAdapter(flagList, this);
            configureRecyclerView();
            presenter.loadData(clientId);
        } else {
            Toast.makeText(this, getString(R.string.message_do_not_find_flags), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void fillViewData(@Nullable ClientTaxMdr clientMdrTaxes, FlagsBank flagsBank) {
        clearFields();
        tvFlagName.setText(flagsBank != null ? flagsBank.getName() : EMPTY_STRING);
        if (clientMdrTaxes != null) {
            if (clientMdrTaxes.getDebitTax() != null) {
                etDebitTax.setVisibility(View.VISIBLE);
                etDebitTax.setText(StringUtils.maskCurrencyDouble(clientMdrTaxes.getDebitTax()));
            } else {
                etDebitTax.setVisibility(View.GONE);
            }

            if (clientMdrTaxes.getCreditTax() != null) {
                etCreditTax.setVisibility(View.VISIBLE);
                etCreditTax.setText(StringUtils.maskCurrencyDouble(clientMdrTaxes.getCreditTax()));
            } else {
                etCreditTax.setVisibility(View.GONE);
            }

            if (clientMdrTaxes.getCreditTaxSixTimes() != null) {
                etCreditSixTax.setVisibility(View.VISIBLE);
                etCreditSixTax.setText(StringUtils.maskCurrencyDouble(clientMdrTaxes.getCreditTaxSixTimes()));
            } else {
                etCreditSixTax.setVisibility(View.GONE);
            }

            if (clientMdrTaxes.getCreditTaxTwelveTimes() != null) {
                etCreditTwelveTax.setVisibility(View.VISIBLE);
                etCreditTwelveTax.setText(StringUtils.maskCurrencyDouble(clientMdrTaxes.getCreditTaxTwelveTimes()));
            } else {
                etCreditTwelveTax.setVisibility(View.GONE);
            }

            if (clientMdrTaxes.getTaxAnticipation() != null) {
                etCreditAnticipationTax.setVisibility(View.VISIBLE);
                etCreditAnticipationTax.setText(StringUtils.maskCurrencyDouble(clientMdrTaxes.getTaxAnticipation()));
            } else {
                etCreditAnticipationTax.setVisibility(View.GONE);
            }
        } else {
            clearFields();

            etCreditTwelveTax.setVisibility(View.GONE);
            etCreditSixTax.setVisibility(View.GONE);
            etCreditTax.setVisibility(View.GONE);
            etDebitTax.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
        etCreditAnticipationTax.setVisibility(isAutomaticAnticipation ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void showMainLoading() {
        pbLoad.setVisibility(View.VISIBLE);
        tvFlagName.setVisibility(View.GONE);
        svTaxes.setVisibility(View.GONE);
        rvFlags.setVisibility(View.GONE);
    }

    @Override
    public void hideMainLoading() {
        pbLoad.setVisibility(View.GONE);
        tvFlagName.setVisibility(View.VISIBLE);
        svTaxes.setVisibility(View.VISIBLE);
        rvFlags.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickEvent(FlagsBank flag) {
        presenter.loadTaxesByFlagId(flag.getId());
    }

    private void configureToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getStringByResId(R.string.title_mdr_taxes));
        }
    }

    private void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        rvFlags.setLayoutManager(layoutManager);
        rvFlags.setAdapter(adapter);
    }

    private void clearFields() {
        tvEmpty.setVisibility(View.GONE);
        etDebitTax.setText(EMPTY_STRING);
        etDebitTax.setText(EMPTY_STRING);
        etCreditTax.setText(EMPTY_STRING);
        etCreditSixTax.setText(EMPTY_STRING);
        etCreditTwelveTax.setText(EMPTY_STRING);
        etCreditAnticipationTax.setText(EMPTY_STRING);
    }
}
