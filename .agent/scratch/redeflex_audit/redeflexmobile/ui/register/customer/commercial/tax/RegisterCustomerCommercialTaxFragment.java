package com.axys.redeflexmobile.ui.register.customer.commercial.tax;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_BIGGER_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_IN_CASH;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.CREDIT_UNTIL_SIX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType.DEBIT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBank;
import com.axys.redeflexmobile.ui.component.flagsbank.FlagsBankEventListener;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.RegisterCustomerActivityListener;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadEmptyDialog;
import com.axys.redeflexmobile.ui.register.dialog.FlagBanksLoadErrorDialog;
import com.axys.redeflexmobile.ui.register.dialog.NoTaxesFoundedDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Rogério Massa on 15/02/19.
 */
@SuppressLint("NonConstantResourceId")
public class RegisterCustomerCommercialTaxFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerCommercialTaxView,
        RegisterCustomerActivityListener, FlagsBankEventListener
{

    @Inject RegisterCustomerCommercialTaxPresenter presenter;

    @BindView(R.id.customer_register_commercial_tax_card_flags) FlagsBank flagsBank;
    @BindView(R.id.customer_register_commercial_tax_cet_tax_debit) CustomEditText cetTaxDebit;
    @BindView(R.id.customer_register_commercial_tax_cet_tax_credit) CustomEditText cetTaxCredit;
    @BindView(R.id.customer_register_commercial_tax_cet_tax_credit_until_six) CustomEditText cetTaxCreditUntilSix;
    @BindView(R.id.customer_register_commercial_tax_cet_tax_credit_bigger_than_six) CustomEditText cetTaxCreditBiggerSix;
    //@BindView(R.id.customer_register_commercial_tax_cet_anticipation) CustomEditText cetAnticipation;
    @BindView(R.id.customer_register_commercial_tax_bt_cancel) Button btCancel;
    @BindView(R.id.customer_register_commercial_tax_bt_confirm) Button btConfirm;

    private Integer selectedOption;

    public static RegisterCustomerCommercialTaxFragment newInstance() {
        return new RegisterCustomerCommercialTaxFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_commercial_tax;
    }

    @Override
    public void initialize() {
        parentActivity.setKeyboardListenerActivated(false);
        flagsBank.setCardFlagsEventListener(this);
        this.presenter.loadFlagsBank();
        initializeActionEvents();
        enableFieldEvents();
        presenter.initializeTaxValues();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onInitializeTaxError() {
        NoTaxesFoundedDialog dialog = NoTaxesFoundedDialog.newInstance(this::onBackPressed);
        dialog.show(getFragmentManager(), NoTaxesFoundedDialog.class.getCanonicalName());
    }

    @Override
    public void onBackPressed() {
        parentActivity.closeKeyboard();
        parentActivity.setKeyboardListenerActivated(true);
        parentActivity.closeFragmentWithoutBottomBarFromCommercial();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (cetTaxDebit.hasFocus()) {
            presenter.saveTaxChanged(selectedOption, DEBIT, cetTaxDebit.getCurrencyDouble());
        } else if (cetTaxCredit.hasFocus()) {
            presenter.saveTaxChanged(selectedOption, CREDIT_IN_CASH, cetTaxCredit.getCurrencyDouble());
        } else if (cetTaxCreditUntilSix.hasFocus()) {
            presenter.saveTaxChanged(selectedOption, CREDIT_UNTIL_SIX, cetTaxCreditUntilSix.getCurrencyDouble());
        } else if (cetTaxCreditBiggerSix.hasFocus()) {
            presenter.saveTaxChanged(selectedOption, CREDIT_BIGGER_SIX, cetTaxCreditBiggerSix.getCurrencyDouble());
        } /*else if (cetAnticipation.hasFocus()) {
            presenter.saveTaxChanged(selectedOption, ANTICIPATION, cetAnticipation.getCurrencyDouble());
        }*/
    }

    @Override
    public void fillTaxValue(CustomerRegisterTax tax, boolean checkAnticipation) {
        flagsBank.updateSelected(tax.getFlag());
        onTaxOptionSelected(tax.getFlag());
        pauseFieldEvents();
        if (tax.getDebit() != null && tax.getDebit() != 0.01) {
            cetTaxDebit.setText(StringUtils.maskCurrencyDouble(tax.getDebit()));
            cetTaxDebit.setVisibility(View.VISIBLE);
        } else {
            cetTaxDebit.setVisibility(View.GONE);
        }

        if (tax.getCredit() != null && tax.getCredit() != 0.01) {
            cetTaxCredit.setText(StringUtils.maskCurrencyDouble(tax.getCredit()));
            cetTaxCredit.setVisibility(View.VISIBLE);
        } else {
            cetTaxCredit.setVisibility(View.GONE);
        }

        if (tax.getUntilSix() != null && tax.getUntilSix() != 0.01) {
            cetTaxCreditUntilSix.setText(StringUtils.maskCurrencyDouble(tax.getUntilSix()));
            cetTaxCreditUntilSix.setVisibility(View.VISIBLE);
        } else {
            cetTaxCreditUntilSix.setVisibility(View.GONE);
        }

        if (tax.getBiggerSix() != null && tax.getBiggerSix() != 0.01) {
            cetTaxCreditBiggerSix.setText(StringUtils.maskCurrencyDouble(tax.getBiggerSix()));
            cetTaxCreditBiggerSix.setVisibility(View.VISIBLE);
        } else {
            cetTaxCreditBiggerSix.setVisibility(View.GONE);
        }

        /*if (checkAnticipation) {
            if (tax.getAnticipation() != null && tax.getAnticipation() > EMPTY_DOUBLE) {
                cetAnticipation.setText(StringUtils.maskCurrencyDouble(tax.getAnticipation()));
                cetAnticipation.setVisibility(View.VISIBLE);
            } else {
                cetAnticipation.setVisibility(View.GONE);
            }
        } else {
            cetAnticipation.setVisibility(View.GONE);
        }*/

        // TODO: Cliente solicitou a remoção das validações, mas para implementações futuras prefiri apenas comentar.
//        displayErrors(tax.getFlag());
        enableFieldEvents();
    }

    @Override
    public void onSaveTaxWithDivergence(int divergenceCount) {
        showOneButtonDialog(
                getString(R.string.customer_register_commercial_tax_error_title),
                getResources().getQuantityString(R.plurals.customer_register_commercial_tax_error_save_tax, divergenceCount),
                view -> onBackPressed()
        );
    }

    private void initializeActionEvents() {
        btCancel.setOnClickListener(v -> onBackPressed());
        btConfirm.setOnClickListener(v -> {
            parentActivity.closeKeyboard();
            presenter.saveTax();
        });
    }

    private void onTaxOptionSelected(int optionId) {
        if (selectedOption != null && selectedOption == optionId) {
            return;
        }
        selectedOption = optionId;
        presenter.selectTaxOption(optionId);
    }

    private void enableFieldEvents() {
        cetTaxDebit.addTextChangedListener(this);
        cetTaxCredit.addTextChangedListener(this);
        cetTaxCreditUntilSix.addTextChangedListener(this);
        cetTaxCreditBiggerSix.addTextChangedListener(this);
        //cetAnticipation.addTextChangedListener(this);
    }

    private void pauseFieldEvents() {
        cetTaxDebit.removeTextChangedListener(this);
        cetTaxCredit.removeTextChangedListener(this);
        cetTaxCreditUntilSix.removeTextChangedListener(this);
        cetTaxCreditBiggerSix.removeTextChangedListener(this);
    }

    @Override
    public void onFlagBankSelected(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flag) {
        onTaxOptionSelected(flag.getId());
    }

    @Override
    public void onLoadFlagsBankError() {
        FlagBanksLoadErrorDialog dialog = FlagBanksLoadErrorDialog.newInstance(this::onBackPressed);
        dialog.show(getFragmentManager(), FlagBanksLoadErrorDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankEmpty() {
        FlagBanksLoadEmptyDialog dialog = FlagBanksLoadEmptyDialog.newInstance(this::onBackPressed);
        dialog.show(getFragmentManager(), FlagBanksLoadEmptyDialog.class.getCanonicalName());
    }

    @Override
    public void onLoadFlagsBankSuccess(List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> list) {
        flagsBank.updateData(list);
        presenter.initializeTaxValues();
    }
    // TODO: Cliente solicitou a remoção das validações, mas para implementações futuras prefiri apenas comentar.
//    private void displayErrors(int optionId) {
//        List<TaxError> errors = presenter.hasError();
//        cetTaxDebit.hideError();
//        cetTaxCredit.hideError();
//        cetTaxCreditUntilSix.hideError();
//        cetTaxCreditBiggerSix.hideError();
//
//        if (Util_IO.isEmptyOrNullList(errors)) {
//            return;
//        }
//
//        Stream.ofNullable(errors).forEach(taxError -> {
//            if (taxError.flagType == optionId) {
//                Stream.ofNullable(taxError.fields).forEach(field -> {
//                    switch (field) {
//                        case DEBIT:
//                            cetTaxDebit.showError(getString(R.string.customer_register_commercial_tax_error_empty));
//                            break;
//                        case CREDIT_IN_CASH:
//                            cetTaxCredit.showError(getString(R.string.customer_register_commercial_tax_error_empty));
//                            break;
//                        case CREDIT_UNTIL_SIX:
//                            cetTaxCreditUntilSix.showError(getString(R.string.customer_register_commercial_tax_error_empty));
//                            break;
//                        case CREDIT_BIGGER_SIX:
//                            cetTaxCreditBiggerSix.showError(getString(R.string.customer_register_commercial_tax_error_empty));
//                            break;
//                    }
//                });
//            }
//        });
//    }
}
