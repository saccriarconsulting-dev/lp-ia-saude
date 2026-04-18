package com.axys.redeflexmobile.ui.register.customer.financial;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerFinancialPresenter extends
            BasePresenter<RegisterCustomerFinancialView> {

        void getBanks();

        void doSave(CustomerRegister customer, boolean isPosOwnerSelected);

        void saveData(CustomerRegister customer, boolean isBack);
}
