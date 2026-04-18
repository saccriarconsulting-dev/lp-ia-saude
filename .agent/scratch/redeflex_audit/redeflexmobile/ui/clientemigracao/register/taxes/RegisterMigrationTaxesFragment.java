package com.axys.redeflexmobile.ui.clientemigracao.register.taxes;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.NoTaxesFoundedDialog;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.NoTaxesSelectedDialog;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.RegisterMotiveMigrationCancelDialog;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommonImpl;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBank;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBankEventListener;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadEmptyDialog;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadErrorDialog;
import com.axys.redeflexmobile.ui.simulationrate.dialogs.ProspectProposalDialog;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_DEBIT_AUTOMATIC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_DEBIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.ANTICIPATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_BIGGER_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_IN_CASH;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_UNTIL_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.DEBIT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Lucas Marciano on 07/04/2020
 */
@SuppressLint("NonConstantResourceId")
public class RegisterMigrationTaxesFragment extends RegisterMigrationCommonImpl implements
        RegisterMigrationTaxesView, FlagsBankEventListener {

    private static final int FROZEN_DURATION = 2000;
    private static final int EMPTY_CLIENT_ID = -1;

    @Inject RegisterMigrationTaxesPresenter presenter;

    @BindView(R.id.bt_current_taxes) Button btCurrentTaxes;
    @BindView(R.id.bt_include_taxes_sub) Button btIncludeTaxesSub;
    @BindView(R.id.migration_cet_debit_tax) CustomEditText etDebitTax;
    @BindView(R.id.migration_cet_tax_credit) CustomEditText etCreditTax;
    @BindView(R.id.migration_cet_tax_credit_six_times) CustomEditText etCreditSixTax;
    @BindView(R.id.migration_cet_tax_credit_twelve_times) CustomEditText etCreditTwelveTax;
    @BindView(R.id.migration_cet_tax_credit_anticipation) CustomEditText etCreditAnticipationTax;
    @BindView(R.id.migration_register_taxes_flags_bank) FlagsBank flagsBank;

    private boolean selectedButtonSubTax = false;
    private Integer selectedOption;
    private RegisterMigrationSub registerMigrationSub;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static RegisterMigrationTaxesFragment newInstance() {
        return new RegisterMigrationTaxesFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_migration_register_taxes;
    }

    @Override
    public void initialize() {
        flagsBank.setSelectItemEnabled(false);
        flagsBank.setCardFlagsEventListener(this);
        this.presenter.loadFlagsBank();
    }

    public void continueInitialize () {
        this.registerMigrationSub = parentActivity.recoverObjectMigration();
        /*if (parentActivity.getClientId() != EMPTY_CLIENT_ID) {
            presenter.initialize(registerMigrationSub);
        }*/
        setupActions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        presenter.detach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register_migration_taxes, menu);
        MenuItem refuseItem = menu.findItem(R.id.action_refuse);
        compositeDisposable.add(
                RxMenuItem.clicks(refuseItem)
                        .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(unused -> presenter.loadMotivesCancelMigration(), Timber::e)
        );
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.contains(EnumRegisterFields.TAX)) {
            showDialogNotSelectedTaxes();
        }
    }

    public void showDialogNotSelectedTaxes() {
        NoTaxesSelectedDialog dialog = NoTaxesSelectedDialog.newInstance();
        dialog.show(getFragmentManager(), NoTaxesSelectedDialog.class.getCanonicalName());
    }

    @Override
    public void initializeInterface(RegisterMigrationSubTax tax) {
        selectedOption = tax.getBandeiraTipoId();
        flagsBank.updateSelected(selectedOption);
        if (tax.getDebito() > EMPTY_DOUBLE) {
            etDebitTax.setText(StringUtils.maskCurrencyDouble(tax.getDebito()));
            etDebitTax.setVisibility(View.VISIBLE);
        } else {
            etDebitTax.setText(EMPTY_STRING);
            etDebitTax.setVisibility(View.GONE);
        }

        if (tax.getCreditoAVista() > EMPTY_DOUBLE) {
            etCreditTax.setText(StringUtils.maskCurrencyDouble(tax.getCreditoAVista()));
            etCreditTax.setVisibility(View.VISIBLE);
        } else {
            etCreditTax.setText(EMPTY_STRING);
            etCreditTax.setVisibility(View.GONE);
        }

        if (tax.getCreditoAte6() > EMPTY_DOUBLE) {
            etCreditSixTax.setText(StringUtils.maskCurrencyDouble(tax.getCreditoAte6()));
            etCreditSixTax.setVisibility(View.VISIBLE);
        } else {
            etCreditSixTax.setText(EMPTY_STRING);
            etCreditSixTax.setVisibility(View.GONE);
        }

        if (tax.getCreditoMaior6() > EMPTY_DOUBLE) {
            etCreditTwelveTax.setText(StringUtils.maskCurrencyDouble(tax.getCreditoMaior6()));
            etCreditTwelveTax.setVisibility(View.VISIBLE);
        } else {
            etCreditTwelveTax.setText(EMPTY_STRING);
            etCreditTwelveTax.setVisibility(View.GONE);
        }

        if (selectedButtonSubTax) {
            if (registerMigrationSub.isAntecipacao()) {
                etCreditAnticipationTax.setText(StringUtils.maskCurrencyDouble(tax.getAntecipacaoAutomatica()));
                etCreditAnticipationTax.setVisibility(View.VISIBLE);
            } else {
                etCreditAnticipationTax.setText(EMPTY_STRING);
                etCreditAnticipationTax.setVisibility(View.GONE);
            }
        } else {
            if (registerMigrationSub.isAntecipacao()) {
                if (tax.getAntecipacaoAutomatica() > EMPTY_DOUBLE) {
                    etCreditAnticipationTax.setText(StringUtils.maskCurrencyDouble(tax.getAntecipacaoAutomatica()));
                    etCreditAnticipationTax.setVisibility(View.VISIBLE);
                } else {
                    etCreditAnticipationTax.setVisibility(View.GONE);
                    etCreditAnticipationTax.setText(EMPTY_STRING);
                }
            } else {
                etCreditAnticipationTax.setVisibility(View.GONE);
                etCreditAnticipationTax.setText(EMPTY_STRING);
            }
        }
    }

    @Override
    public void initializeInterfaceNoTaxes(int flag) {
        selectedOption = flag;
        flagsBank.updateSelected(flag);
        clearFields();
    }

    @Override
    public void persistData() {
        parentActivity.closeKeyboard();
        presenter.doSave(registerMigrationSub);
    }

    @Override
    public void persistCloneData() {
        clearErrors();
        parentActivity.closeKeyboard();
        presenter.finalizeFlow(true);
    }

    @Override
    public void onValidationSuccess(List<RegisterMigrationSubTax> list) {
        registerMigrationSub.setTaxesList(list);
        parentActivity.saveObjectMigration(registerMigrationSub);
        parentActivity.doComplete();
    }

    @Override
    public void onValidationSuccessBack(List<RegisterMigrationSubTax> list) {
        registerMigrationSub.setTaxesList(list);
        parentActivity.saveObjectMigration(registerMigrationSub);
        parentActivity.stepValidatedBack();
    }

    @Override
    public void showResponseMotivesCancelMigration() {
        if (getActivity() == null) {
            return;
        }

        Toast.makeText(
                getActivity(),
                R.string.message_success_cancel_migration,
                Toast.LENGTH_LONG
        ).show();

        getActivity().finish();
    }

    @Override
    public void callModalMotivesCancelMigration(List<MotiveMigrationSub> motiveMigrationSubs) {
        final RegisterMotiveMigrationCancelDialog dialog = RegisterMotiveMigrationCancelDialog
                .newInstance(presenter::saveMigrationCancelMotive, motiveMigrationSubs);
        dialog.show(getChildFragmentManager(), RegisterMotiveMigrationCancelDialog.class.getName());
    }

    @Override
    public void showDialogNotFoundTaxes() {
        NoTaxesFoundedDialog dialog = NoTaxesFoundedDialog.newInstance(parentActivity::stepValidatedBack);
        dialog.show(getFragmentManager(), ProspectProposalDialog.class.getCanonicalName());
    }

    @Override
    public void saveCurrentState() {
        saveState();
    }

    private void saveState() {
        presenter.saveTaxChanged(selectedOption, DEBIT, etDebitTax.getCurrencyDouble());
        presenter.saveTaxChanged(selectedOption, CREDIT_IN_CASH, etCreditTax.getCurrencyDouble());
        presenter.saveTaxChanged(selectedOption, CREDIT_UNTIL_SIX, etCreditSixTax.getCurrencyDouble());
        presenter.saveTaxChanged(selectedOption, CREDIT_BIGGER_SIX, etCreditTwelveTax.getCurrencyDouble());
        presenter.saveTaxChanged(selectedOption, ANTICIPATION, etCreditAnticipationTax.getCurrencyDouble());
    }

    private void setupActions() {
        compositeDisposable.add(RxView.clicks(btCurrentTaxes)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    selectedButtonSubTax = false;
                    parentActivity.closeKeyboard();
                    removeFocus();
                    clearErrors();
                    clearFields();
                    toggleEnableFields(false);
                    presenter.loadCurrentTaxes(registerMigrationSub);
                }, Timber::d));

        compositeDisposable.add(RxView.clicks(btIncludeTaxesSub)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    selectedButtonSubTax = true;
                    parentActivity.closeKeyboard();
                    removeFocus();
                    clearErrors();
                    clearFields();
                    toggleEnableFields(true);
                    presenter.loadNewTaxes(registerMigrationSub);
                }, Timber::d));
    }

    private void removeFocus() {
        etDebitTax.removeFieldFocus();
        etCreditTax.removeFieldFocus();
        etCreditSixTax.removeFieldFocus();
        etCreditTwelveTax.removeFieldFocus();
        etCreditAnticipationTax.removeFieldFocus();
    }

    private void clearErrors() {
        etDebitTax.hideError();
        etCreditTax.hideError();
        etCreditSixTax.hideError();
        etCreditTwelveTax.hideError();
        etCreditAnticipationTax.hideError();
    }

    private void toggleEnableFields(boolean isEnable) {
        etDebitTax.setEnabled(isEnable);
        etCreditTax.setEnabled(isEnable);
        etCreditSixTax.setEnabled(isEnable);
        etCreditTwelveTax.setEnabled(isEnable);
        etCreditAnticipationTax.setEnabled(isEnable);
        flagsBank.setSelectItemEnabled(true);
    }

    private void clearFields() {
        etDebitTax.setText(EMPTY_STRING);
        etCreditTax.setText(EMPTY_STRING);
        etCreditSixTax.setText(EMPTY_STRING);
        etCreditTwelveTax.setText(EMPTY_STRING);
        etCreditAnticipationTax.setText(EMPTY_STRING);
    }

    private void onTaxOptionSelected(int optionId) {
        if (selectedOption != null && selectedOption == optionId) {
            return;
        }
        if(selectedButtonSubTax){
            saveState();
        }
        selectedOption = optionId;
        presenter.selectTaxOption(optionId);
    }

    @Override
    public void onFlagBankSelected(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flag) {
        onTaxOptionSelected(flag.getId());
    }

    @Override
    public void onLoadFlagsBankError() {
        FlagBanksLoadErrorDialog dialog = FlagBanksLoadErrorDialog.newInstance(this::loadFlagsBankErrorHandler);
        dialog.show(getFragmentManager(), FlagBanksLoadErrorDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankEmpty() {
        FlagBanksLoadEmptyDialog dialog = FlagBanksLoadEmptyDialog.newInstance(this::loadFlagsBankErrorHandler);
        dialog.show(getFragmentManager(), FlagBanksLoadEmptyDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankSuccess(List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> list) {
        flagsBank.updateData(list);
        continueInitialize();
    }

    public void loadFlagsBankErrorHandler() {
        continueInitialize();
    }
}
