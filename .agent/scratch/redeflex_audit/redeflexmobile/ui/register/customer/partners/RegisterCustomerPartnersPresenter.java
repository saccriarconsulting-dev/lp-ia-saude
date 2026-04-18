package com.axys.redeflexmobile.ui.register.customer.partners;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface RegisterCustomerPartnersPresenter extends
        BasePresenter<RegisterCustomerPartnersView> {

    void getProfissoes();

    void getRenda();

    void getPatrimonio();

    void doSave(CustomerRegisterPartners customer);

    void saveData(CustomerRegisterPartners customer, boolean isBack);
}
