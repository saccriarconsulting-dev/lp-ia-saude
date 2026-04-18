package com.axys.redeflexmobile.ui.register.customer.dadosec;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface RegisterCustomerDadosECPresenter extends BasePresenter<RegisterCustomerDadosECView> {

    void doSave(CustomerRegister customer);

    void saveData(CustomerRegister customer, boolean isBack);

    void initializeData();

    void getMcc();

    void getSegmento();
}
