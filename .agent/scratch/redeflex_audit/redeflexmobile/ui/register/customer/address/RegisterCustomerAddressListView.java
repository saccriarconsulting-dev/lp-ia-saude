package com.axys.redeflexmobile.ui.register.customer.address;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Rogério Massa on 07/02/19.
 */

public interface RegisterCustomerAddressListView extends RegisterCustomerCommon {

    void showError(String error);

    void stepValidated();

    void stepValidatedBack();

    void fillAddressType(List<CustomerRegisterAddressType> addressType);

    void setNumeroTitulo(String pNumero);
}
