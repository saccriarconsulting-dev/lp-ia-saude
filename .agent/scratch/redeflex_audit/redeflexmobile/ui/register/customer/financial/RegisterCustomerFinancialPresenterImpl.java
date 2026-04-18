package com.axys.redeflexmobile.ui.register.customer.financial;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT_DIGIT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_AGENCY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SGV_ID;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ACCOUNT_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_BANK;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerFinancialPresenterImpl extends BasePresenterImpl<RegisterCustomerFinancialView>
        implements RegisterCustomerFinancialPresenter {

    private CustomerRegisterManager manager;
    private CustomerRegister customerRegister;

    public RegisterCustomerFinancialPresenterImpl(RegisterCustomerFinancialView view,
                                                  SchedulerProvider schedulerProvider,
                                                  ExceptionUtils exceptionUtils,
                                                  CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void getBanks() {
        List<Completable> request = Arrays.asList(
                manager.getAccountTypes().flatMapCompletable(accountTypes -> {
                    safeExecute(view -> view.fillAccountTypes(new ArrayList<>(accountTypes)));
                    return Completable.complete();
                }),
                manager.getBanks().flatMapCompletable(banks -> {
                    safeExecute(view -> view.fillBanks(new ArrayList<>(banks)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    private void initializeData() {
        safeExecute(view -> {
            RegisterCustomerView parentActivity = view.getParentActivity();
            if (parentActivity != null) customerRegister = parentActivity.getCustomerRegister();
            if (customerRegister != null) view.initializeFieldValues(customerRegister);

            // Ajusta o Numero da Sequencia do Titulo
            if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue())
            {
                if (customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                    view.setNumeroTitulo("6");
                else
                    view.setNumeroTitulo("5");
            }
        });
    }

    @Override
    public void doSave(CustomerRegister customer, boolean isPosOwnerSelected) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (isPosOwnerSelected && StringUtils.isEmpty(customer.getSgvCode())) {
            errors.add(ET_SGV_ID);
        }
        if (customer.getAccountType() == null) {
            errors.add(SPN_ACCOUNT_TYPE);
        }
        if (customer.getBank() == null) {
            errors.add(SPN_BANK);
        }
        if (StringUtils.isEmpty(customer.getBankAgency())) {
            errors.add(ET_BANK_AGENCY);
        }
        if (StringUtils.isEmpty(customer.getBankAccount())) {
            errors.add(ET_BANK_ACCOUNT);
        }
        if (StringUtils.isEmpty(customer.getBankAccountDigit())) {
            errors.add(ET_BANK_ACCOUNT_DIGIT);
        }

        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            saveData(customer, false);
        }
    }

    @Override
    public void saveData(CustomerRegister customer, boolean isBack) {
        CustomerRegister customerRegister;

        if (isBack) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            customerRegister = parentActivity.getCustomerRegisterClone();
        } else {
            customerRegister = this.customerRegister;
        }

        customerRegister.setSgvCode(customer.getSgvCode());
        customerRegister.setAccountType(customer.getAccountType());
        customerRegister.setBank(customer.getBank());
        customerRegister.setBankAgency(customer.getBankAgency());
        customerRegister.setBankAgencyDigit(customer.getBankAgencyDigit());
        customerRegister.setBankAccount(customer.getBankAccount());
        customerRegister.setBankAccountDigit(customer.getBankAccountDigit());

        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }
}
