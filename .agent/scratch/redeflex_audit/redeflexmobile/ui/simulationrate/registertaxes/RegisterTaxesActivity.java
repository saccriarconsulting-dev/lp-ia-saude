package com.axys.redeflexmobile.ui.simulationrate.registertaxes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBank;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBankEventListener;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadEmptyDialog;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadErrorDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.CancelRegisterRateDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.ChooseImportDataRegisterDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.ConfirmProspectDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.NoTaxesFoundedDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.ProspectProposalDialog;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListActivity;
import com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.FLAG_PROSPECT_MDR;
import static com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity.KEY_PROSPECT_COMPLETE_REGISTER;
import static com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity.KEY_PROSPECT_ID;
import static com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity.KEY_PROSPECT_IS_NEW;
import static com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity.KEY_PROSPECT_NO_TAXES;

/**
 * @author Lucas Marciano on 28/04/2020
 */
@SuppressLint("NonConstantResourceId")
public class RegisterTaxesActivity extends BaseActivity implements RegisterTaxesView, FlagsBankEventListener {

    private static final int BUTTON_WAIT_DURATION = 1200;
    private static final int DEFAULT_NON_ID = -1;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.register_taxes_card_flags) FlagsBank flagsBank;
    @BindView(R.id.cet_debit_tax) CustomEditText cetDebit;
    @BindView(R.id.cet_tax_credit) CustomEditText cetCredit;
    @BindView(R.id.cet_tax_credit_six_times) CustomEditText cetCreditSixTimes;
    @BindView(R.id.cet_tax_credit_twelve_times) CustomEditText cetCreditTwelveTimes;
    @BindView(R.id.cet_tax_credit_anticipation) CustomEditText cetCreditAnticipation;
    @BindView(R.id.bt_prospect) Button btProspect;
    @BindView(R.id.bt_generate_proposal) Button btGenerateProposal;
    @BindView(R.id.loading_view) View viewLoading;

    @Inject RegisterTaxesPresenter presenter;

    private int prospectId = DEFAULT_NON_ID;
    private boolean isNewProspect = true;
    private Integer selectedOption;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_register_taxes;
    }

    @Override
    protected void initialize() {
        getBundleVariable();
        flagsBank.setCardFlagsEventListener(this);
        presenter.initializer(prospectId);
        setupToolbar();
        presenter.loadFlagsBank();
        setupActions();
        disableFields();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
        CancelRegisterRateDialog dialog;
        if (isNewProspect) {
            dialog = CancelRegisterRateDialog.newInstance(
                    presenter::rollbackProspect
            );
        } else {
            dialog = CancelRegisterRateDialog.newInstance(this::openListActivity);
        }
        dialog.show(getSupportFragmentManager(), CancelRegisterRateDialog.class.getName());
    }

    @Override
    public void endProspectProcess(boolean isCancel) {
        if (!isCancel)
            Toast.makeText(this, R.string.message_success_create_prospection, Toast.LENGTH_LONG).show();

        openListActivity();
    }

    @Override
    public void openDialogToCheckPersonalInfo() {
        ProspectProposalDialog dialog = ProspectProposalDialog.newInstance(this::goToBackScreen);
        dialog.show(getSupportFragmentManager(), ProspectProposalDialog.class.getCanonicalName());
    }

    @Override
    public void showLoading() {
        viewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        viewLoading.setVisibility(View.GONE);
    }

    @Override
    public void fillInterface(RegistrationSimulationFee tax) {
        flagsBank.updateSelected(tax.getIdFlag());
        onTaxOptionSelected(tax.getIdFlag());
        if (tax.getDebitValue() > EMPTY_DOUBLE) {
            cetDebit.setText(StringUtils.maskCurrencyDouble(tax.getDebitValue()));
            cetDebit.setVisibility(View.VISIBLE);
        } else {
            cetDebit.setVisibility(View.GONE);
        }

        if (tax.getCreditValue() > EMPTY_DOUBLE) {
            cetCredit.setText(StringUtils.maskCurrencyDouble(tax.getCreditValue()));
            cetCredit.setVisibility(View.VISIBLE);
        } else {
            cetCredit.setVisibility(View.GONE);
        }

        if (tax.getCreditSixValue() > EMPTY_DOUBLE) {
            cetCreditSixTimes.setText(StringUtils.maskCurrencyDouble(tax.getCreditSixValue()));
            cetCreditSixTimes.setVisibility(View.VISIBLE);
        } else {
            cetCreditSixTimes.setVisibility(View.GONE);
        }

        if (tax.getCreditMoreThanSixValue() > EMPTY_DOUBLE) {
            cetCreditTwelveTimes.setText(StringUtils.maskCurrencyDouble(tax.getCreditMoreThanSixValue()));
            cetCreditTwelveTimes.setVisibility(View.VISIBLE);
        } else {
            cetCreditTwelveTimes.setVisibility(View.GONE);
        }

        if (presenter.checkAnticipation()) {
            if (tax.getAutomaticAnticipation() > 0) {
                cetCreditAnticipation.setText(StringUtils.maskCurrencyDouble(tax.getAutomaticAnticipation()));
                cetCreditAnticipation.setVisibility(View.VISIBLE);
            } else {
                cetCreditAnticipation.setVisibility(View.GONE);
            }
        } else {
            cetCreditAnticipation.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDialogNotFoundTaxes() {
        NoTaxesFoundedDialog dialog = NoTaxesFoundedDialog.newInstance(this::noTaxesFound);
        dialog.show(getSupportFragmentManager(), ProspectProposalDialog.class.getCanonicalName());
    }

    @Override
    public void goToGenerateNewProspect() {
        ChooseImportDataRegisterDialog dialog = ChooseImportDataRegisterDialog.newInstance(
                this::openNewActivity,
                isNewProspect);
        dialog.show(getSupportFragmentManager(), ProspectProposalDialog.class.getCanonicalName());
    }

    @Override
    public void updateInterfaceVisibility(ProspectingClientAcquisition prospect) {
        cetCreditAnticipation.setVisibility(
                    prospect.isAnticipation() ? View.VISIBLE : View.GONE
            );
    }
    @Override
    public void validateProspect() {
        presenter.validateProspect();
    }

    @Override
    public void onLoadFlagsBankError() {
        FlagBanksLoadErrorDialog dialog = FlagBanksLoadErrorDialog.newInstance(this::onBackPressed);
        dialog.show(getSupportFragmentManager(), FlagBanksLoadErrorDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankEmpty() {
        FlagBanksLoadEmptyDialog dialog = FlagBanksLoadEmptyDialog.newInstance(this::onBackPressed);
        dialog.show(getSupportFragmentManager(), FlagBanksLoadEmptyDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankSuccess(List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> list) {
        flagsBank.updateData(list);
    }

    private void openNewActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(FLAG_PROSPECT_MDR, prospectId);

        Utilidades.openNewActivity(
                this,
                RegisterCustomerActivity.class,
                bundle,
                true
        );
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.register_taxes_title));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    private void goToBackScreen() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_PROSPECT_IS_NEW, false);
        bundle.putBoolean(KEY_PROSPECT_COMPLETE_REGISTER, true);
        bundle.putInt(KEY_PROSPECT_ID, prospectId);
        Utilidades.openNewActivity(
                this,
                RegisterRateActivity.class,
                bundle,
                true
        );
    }

    private void noTaxesFound() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_PROSPECT_IS_NEW, false);
        bundle.putBoolean(KEY_PROSPECT_NO_TAXES, true);
        bundle.putInt(KEY_PROSPECT_ID, prospectId);
        Utilidades.openNewActivity(
                this,
                RegisterRateActivity.class,
                bundle,
                true
        );
    }

    private void setupActions() {
        compositeDisposable.add(RxView.clicks(btProspect)
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> openDialogToConfirmProcess(), Timber::d));

        compositeDisposable.add(RxView.clicks(btGenerateProposal)
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter.loadGenerateProposalFunction(), Timber::d));
    }

    private void openDialogToConfirmProcess() {
        ConfirmProspectDialog dialog = ConfirmProspectDialog.newInstance(presenter::loadProspectFunction);
        dialog.show(getSupportFragmentManager(), ProspectProposalDialog.class.getCanonicalName());
    }

    private void onTaxOptionSelected(int optionId) {
        if (selectedOption != null && selectedOption == optionId) {
            return;
        }
        selectedOption = optionId;
        presenter.selectTaxOption(optionId);
    }

    private void getBundleVariable() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            prospectId = bundle.getInt(KEY_PROSPECT_ID, DEFAULT_NON_ID);
            isNewProspect = bundle.getBoolean(KEY_PROSPECT_IS_NEW, true);
        }
    }

    private void disableFields() {
        hideKeyboardFrom(cetDebit);
        hideKeyboardFrom(cetCredit);
        hideKeyboardFrom(cetCreditSixTimes);
        hideKeyboardFrom(cetCreditTwelveTimes);
        hideKeyboardFrom(cetCreditAnticipation);

        cetDebit.removeFieldFocus();
        cetCredit.removeFieldFocus();
        cetCreditSixTimes.removeFieldFocus();
        cetCreditTwelveTimes.removeFieldFocus();
        cetCreditAnticipation.removeFieldFocus();

        if (!isNewProspect) {
            btProspect.setVisibility(View.GONE);
        }
    }

    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openListActivity() {
        Utilidades.openNewActivity(
                this,
                RegisterProspectListActivity.class,
                null,
                true
        );
    }

    @Override
    public void onFlagBankSelected(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flag) {
        onTaxOptionSelected(flag.getId());
    }
}
