package com.axys.redeflexmobile.ui.register.customer.commercial.tax;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankEvent;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;
import com.axys.redeflexmobile.ui.register.customer.commercial.tax.RegisterCustomerCommercialTaxPresenterImpl.TaxError;

import java.util.List;

/**
 * @author Rogério Massa on 15/02/19.
 */

public interface RegisterCustomerCommercialTaxPresenter extends BasePresenter<RegisterCustomerCommercialTaxView>, LoadFlagsBankEvent {

    void initializeTaxValues();

    List<TaxError> hasError();

    void selectTaxOption(int taxType);

    void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value);

    void saveTax();
}
