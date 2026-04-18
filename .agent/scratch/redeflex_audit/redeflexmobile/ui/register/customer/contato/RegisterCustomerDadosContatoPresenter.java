package com.axys.redeflexmobile.ui.register.customer.contato;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface RegisterCustomerDadosContatoPresenter extends BasePresenter<RegisterCustomerDadosContatoView> {

    void doSave(CustomerRegister customerRegister);

    void saveData(CustomerRegister customerRegister, boolean isBack);

    void initializeData();
}
