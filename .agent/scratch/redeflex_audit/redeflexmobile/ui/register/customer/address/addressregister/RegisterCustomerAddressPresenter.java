package com.axys.redeflexmobile.ui.register.customer.address.addressregister;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerAddressPresenter extends
        BasePresenter<RegisterCustomerAddressView> {

    void initializeData(CustomerRegisterAddress address, int operationType);

    void getAddress(String cep);

    void doSave(CustomerRegisterAddress address);

    void doRemove();
}
