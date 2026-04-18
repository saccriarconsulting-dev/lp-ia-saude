package com.axys.redeflexmobile.ui.register.customer.financial;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT_DIGIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_AGENCY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SGV_ID;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ACCOUNT_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_BANK;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.dialog.register.CustomerRegisterFinancialDialog;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Bruno Pimentel on 22/11/18.
 */
public class RegisterCustomerFinancialFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerFinancialView {

    @Inject RegisterCustomerFinancialPresenter presenter;

    @BindView(R.id.third_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.third_register_switch_pos_owner) Switch swPOSOwner;
    @BindView(R.id.third_register_et_sgv_id) CustomEditText etSgvId;
    @BindView(R.id.third_register_spn_account_type) CustomSpinner spnAccountType;
    @BindView(R.id.third_register_spn_bank) CustomSpinner spnBank;
    @BindView(R.id.third_register_et_bank_agency) CustomEditText etBankAgency;
    @BindView(R.id.third_register_et_bank_agency_digit) CustomEditText etBankAgencyDigit;
    @BindView(R.id.third_register_et_bank_account) CustomEditText etBankAccount;
    @BindView(R.id.third_register_et_bank_account_digit) CustomEditText etBankAccountDigit;
    @BindView(R.id.third_register_tv_title_number) TextView txtNumeroTitulo;

    public static RegisterCustomerFinancialFragment newInstance() {
        return new RegisterCustomerFinancialFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_financial;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        swPOSOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                etSgvId.clearValue();
                if (swPOSOwner.isChecked()) {
                    etSgvId.setVisibility(VISIBLE);
                    etSgvId.requestFieldFocus();
                } else {
                    etSgvId.setVisibility(GONE);
                    clearFieldsFocus();
                }
            }
        });
        presenter.getBanks();
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
        showFinancialAlert();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void fillAccountTypes(List<ICustomSpinnerDialogModel> accountTypes) {
        spnAccountType.setList(accountTypes);
    }

    @Override
    public void fillBanks(List<ICustomSpinnerDialogModel> banks) {
        spnBank.setList(banks);
    }

    @Override
    public void initializeFieldValues(CustomerRegister customer) {
        String sgvCode = customer.getSgvCode();
        boolean haveSgvCode = StringUtils.isNotEmpty(sgvCode);
        swPOSOwner.setChecked(haveSgvCode);
        etSgvId.setText(haveSgvCode ? sgvCode : EMPTY_STRING);
        etSgvId.setVisibility(View.GONE);
        spnAccountType.doSelectWithCallback(customer.getAccountType());
        spnBank.doSelectWithCallback(customer.getBank());
        etBankAgency.setText(customer.getBankAgency());
        etBankAgencyDigit.setText(customer.getBankAgencyDigit());
        etBankAccount.setText(customer.getBankAccount());
        etBankAccountDigit.setText(customer.getBankAccountDigit());
    }

    @Override
    public void persistData() {
        presenter.doSave(getStepRequest(), swPOSOwner.isChecked());
    }

    @Override
    public void persistCloneData() {
        presenter.saveData(getStepRequest(), true);
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(SPN_ACCOUNT_TYPE)) spnAccountType.showError();
        //if (errors.contains(ET_SGV_ID)) etSgvId.showError();
        if (errors.contains(SPN_BANK)) spnBank.showError();
        if (errors.contains(ET_BANK_AGENCY)) etBankAgency.showError();
        if (errors.contains(ET_BANK_ACCOUNT)) etBankAccount.showError();
        if (errors.contains(ET_BANK_ACCOUNT_DIGIT)) etBankAccountDigit.showError();
        moveFocusToFirstError(errors.get(EMPTY_INT));
    }

    @Override
    public void onValidationSuccess() {
        parentActivity.stepValidated();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.stepValidatedBack();
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }

    private CustomerRegister getStepRequest() {
        CustomerRegister customer = new CustomerRegister();
        customer.setSgvCode(etSgvId.getText());
        customer.setAccountType(spnAccountType.getSelectedItemId());
        customer.setBank(spnBank.getSelectedItemId());
        customer.setBankAgency(StringUtils.returnNonSpecials(etBankAgency.getText()));
        customer.setBankAgencyDigit(StringUtils.returnNonSpecials(etBankAgencyDigit.getText()));
        customer.setBankAccount(StringUtils.returnNonSpecials(etBankAccount.getText()));
        customer.setBankAccountDigit(StringUtils.returnNonSpecials(etBankAccountDigit.getText()));
        return customer;
    }

    private void clearFieldsFocus() {
        etSgvId.removeFieldFocus();
        etBankAgency.removeFieldFocus();
        etBankAgencyDigit.removeFieldFocus();
        etBankAccount.removeFieldFocus();
        etBankAccountDigit.removeFieldFocus();
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        switch (error) {
            case ET_SGV_ID:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etSgvId.getY() - margin);
                break;
            case SPN_ACCOUNT_TYPE:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnAccountType.getY() - margin);
                break;
            case SPN_BANK:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnBank.getY() - margin);
                break;
            case ET_BANK_AGENCY:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etBankAgency.getY() - margin);
                break;
            case ET_BANK_AGENCY_DIGIT:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etBankAgencyDigit.getY() - margin);
                break;
            case ET_BANK_ACCOUNT:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etBankAccount.getY() - margin);
                break;
            case ET_BANK_ACCOUNT_DIGIT:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etBankAccountDigit.getY() - margin);
                break;
        }
    }

    private void showFinancialAlert() {
        if (!parentActivity.getFinancialAlert() && (parentActivity.getCustomerRegister() == null
                || StringUtils.isEmpty(parentActivity.getCustomerRegister().getId()))) {

            CustomerRegisterFinancialDialog dialog = CustomerRegisterFinancialDialog.newInstance(null);
            dialog.setCancelable(false);
            dialog.show(getChildFragmentManager(), dialog.getClass().getSimpleName());
            parentActivity.setFinancialAlert();
        }
    }
}
