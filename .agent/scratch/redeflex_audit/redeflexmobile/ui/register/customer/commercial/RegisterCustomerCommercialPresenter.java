package com.axys.redeflexmobile.ui.register.customer.commercial;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerCommercialPresenter extends BasePresenter<RegisterCustomerCommercialView> {

    CustomerRegister getLocalCustomerRegister();

    void setErrorList(List<EnumRegisterFields> errorList);

    void initializeData();

    void removePos(int position);

    void doSave(CustomerRegister request);

    void saveData(CustomerRegister customer, boolean isBack);

    void cacheValues(CustomerRegister customerRegister);
}
