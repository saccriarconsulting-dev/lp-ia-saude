package com.axys.redeflexmobile.ui.register.customer.commercial.tax;

import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankListener;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

/**
 * @author Rogério Massa on 15/02/19.
 */

public interface RegisterCustomerCommercialTaxView extends RegisterCustomerCommon, LoadFlagsBankListener {

    void onInitializeTaxError();

    void onBackPressed();

    void fillTaxValue(CustomerRegisterTax tax, boolean checkAnticipation);

    void onSaveTaxWithDivergence(int divergenceCount);
}
