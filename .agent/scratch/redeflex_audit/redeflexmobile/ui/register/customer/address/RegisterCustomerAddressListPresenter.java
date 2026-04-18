package com.axys.redeflexmobile.ui.register.customer.address;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Rogério Massa on 07/02/19.
 */

interface RegisterCustomerAddressListPresenter extends BasePresenter<RegisterCustomerAddressListView> {

    List<CustomerRegisterAddress> getAddressList();

    void initializeData();

    void loadAddressTypeList();

    CustomerRegisterAddress getAddress(CustomerRegisterAddressType addressType);

    void removeAddress(CustomerRegisterAddress address);

    void saveAddress(boolean isBack);
}
