package com.axys.redeflexmobile.ui.register.customer.commercial.tax;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation.YES;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rogério Massa on 15/02/19.
 */

public class RegisterCustomerCommercialTaxPresenterImpl extends BasePresenterImpl<RegisterCustomerCommercialTaxView>
        implements RegisterCustomerCommercialTaxPresenter {

    private final CustomerRegisterManager manager;
    private final FlagsBankManager flagsBankManager;
    private CustomerRegister localCustomerRegister;
    private List<CustomerRegisterTax> customerRegisterTaxes;
    private List<TaxaMdr> taxMdrList;
    private List<TaxError> hasError;
    private List<FlagsBank> flagsBanks;

    public RegisterCustomerCommercialTaxPresenterImpl(RegisterCustomerCommercialTaxView view,
                                                      SchedulerProvider schedulerProvider,
                                                      ExceptionUtils exceptionUtils,
                                                      CustomerRegisterManager manager,
                                                      FlagsBankManager flagsBankManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
        this.flagsBankManager = flagsBankManager;
        this.customerRegisterTaxes = new ArrayList<>();
        this.taxMdrList = new ArrayList<>();
        this.flagsBanks = new ArrayList<>();
    }

    @Override
    public void initializeTaxValues() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        CustomerRegister customerRegisterCached = parentActivity.getCustomerRegisterCommercialCache();
        localCustomerRegister = new CustomerRegister(customerRegisterCached);

        compositeDisposable.add(manager.getTaxList(localCustomerRegister)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeTaxMdrSuccess,
                        throwable -> getView().onInitializeTaxError()
                ));
    }

    @Override
    public void loadFlagsBank() {
        compositeDisposable.add(flagsBankManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeFlagsBankSuccess,
                        throwable -> getView().onLoadFlagsBankError()
                ));
    }

    @Override
    public List<TaxError> hasError() {
        return hasError;
    }

    private void initializeFlagsBankSuccess(List<FlagsBank> list) {
        if(list.size() == 0){
            getView().onLoadFlagsBankEmpty();
        }else {
            this.flagsBanks = list;
            getView().onLoadFlagsBankSuccess(this.flagsBanks);
        }
    }

    private void initializeTaxMdrSuccess(List<TaxaMdr> taxList) {
        this.taxMdrList = taxList;

        if (Util_IO.isEmptyOrNullList(localCustomerRegister.getTaxList())) {
            this.customerRegisterTaxes = Stream.ofNullable(taxList)
                    .map(CustomerRegisterTax::new)
                    .toList();
        } else {
            this.customerRegisterTaxes = Stream.ofNullable(localCustomerRegister.getTaxList())
                    .map(CustomerRegisterTax::new)
                    .toList();
        }
        if(this.flagsBanks.size() > 0){
            selectTaxOption(this.flagsBanks.get(0).getId());
        }
    }

    @Override
    public void selectTaxOption(int flagType) {
        CustomerRegisterTax customerRegisterTax = Stream.ofNullable(customerRegisterTaxes)
                .filter(tax -> tax.getFlag() == flagType)
                .findFirst()
                .orElse(null);

        if (customerRegisterTax != null) {
            if (validateIfAllOfNull(customerRegisterTax)) {
                getView().fillTaxValue(customerRegisterTax, localCustomerRegister.getAnticipation());
            } else {
                getView().onInitializeTaxError();
            }
        } else {
            getView().onInitializeTaxError();
        }
    }

    @Override
    public void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value) {
        Stream.ofNullable(customerRegisterTaxes).forEach(customerRegisterTax -> {
            if (customerRegisterTax.getFlag() == flagType) {
                switch (taxType) {
                    case DEBIT:
                        customerRegisterTax.setDebit(value);
                        break;
                    case CREDIT_IN_CASH:
                        customerRegisterTax.setCredit(value);
                        break;
                    case CREDIT_UNTIL_SIX:
                        customerRegisterTax.setUntilSix(value);
                        break;
                    case CREDIT_BIGGER_SIX:
                        customerRegisterTax.setBiggerSix(value);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void saveTax() {

        int hasDivergence = 0;
        hasError = new ArrayList<>();

        for (FlagsBank flag: flagsBanks) {

            TaxError error = new TaxError(flag.getId(), new ArrayList<>());
            for (CustomerRegisterTax tax : customerRegisterTaxes) {

                if (flag.getId().equals(tax.getFlag())) {
                    // TODO: Cliente solicitou a remoção das validações, mas para implementações futuras prefiri apenas comentar.
//                    if (tax.getDebit() <= EMPTY_DOUBLE) error.fields.add(DEBIT);
//                    if (tax.getCredit() <= EMPTY_DOUBLE) error.fields.add(CREDIT_IN_CASH);
//                    if (tax.getUntilSix() <= EMPTY_DOUBLE) error.fields.add(CREDIT_UNTIL_SIX);
//                    if (tax.getBiggerSix() <= EMPTY_DOUBLE) error.fields.add(CREDIT_BIGGER_SIX);

                    for (TaxaMdr taxaMdr : taxMdrList) {

                        if (flag.getId().equals(taxaMdr.getFlagType())) {
                            if (tax.getDebit() != null && tax.getDebit() < taxaMdr.getTaxDebit()) hasDivergence++;
                            if (tax.getCredit() != null && tax.getCredit() < taxaMdr.getTaxCredit()) hasDivergence++;
                            if (tax.getUntilSix() != null && tax.getUntilSix() < taxaMdr.getTaxUntilSix()) hasDivergence++;
                            if (tax.getBiggerSix() != null && tax.getBiggerSix() < taxaMdr.getTaxBiggerSix()) hasDivergence++;
                        }
                    }
                }
            }
            if (!error.fields.isEmpty()) hasError.add(error);
        }

        // TODO: Cliente solicitou a remoção das validações, mas para implementações futuras prefiri apenas comentar.
//        if (!hasError.isEmpty()) {
//            selectTaxOption(hasError.get(EMPTY_INT).flagType);
//            return;
//        }

        RegisterCustomerView parentActivity = getView().getParentActivity();
        CustomerRegister customerRegisterCached = parentActivity.getCustomerRegisterCommercialCache();

        boolean isAnticipationSettingsEnabled = YES.equals(EnumRegisterAnticipation.getEnumByValue(
                Util_IO.booleanToNumber(localCustomerRegister.getAnticipation())));
        customerRegisterCached.setTaxList(Stream.ofNullable(customerRegisterTaxes)
                .map(customerRegisterTax -> {
                    if (!isAnticipationSettingsEnabled) {
                        customerRegisterTax.setAnticipation(null);
                    }else{
                        customerRegisterTax.setAnticipation(customerRegisterCached.getAnticipationValue());
                    }
                    return customerRegisterTax;
                })
                .toList());

        if (hasDivergence > 0) {
            getView().onSaveTaxWithDivergence(hasDivergence);
        } else {
            getView().onBackPressed();
        }
    }

    static class TaxError {

        final int flagType;
        final List<EnumRegisterTaxType> fields;

        TaxError(int flagType, List<EnumRegisterTaxType> fields) {
            this.flagType = flagType;
            this.fields = fields;
        }
    }

    private boolean validateIfAllOfNull(CustomerRegisterTax taxes) {
        if (taxes.getDebit() != null && taxes.getDebit() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getCredit() != null && taxes.getCredit() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getBiggerSix() != null && taxes.getBiggerSix() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getAnticipation() != null && taxes.getAnticipation() > EMPTY_DOUBLE)
            return true;
        else return taxes.getUntilSix() != null && taxes.getUntilSix() > EMPTY_DOUBLE;
    }
}
