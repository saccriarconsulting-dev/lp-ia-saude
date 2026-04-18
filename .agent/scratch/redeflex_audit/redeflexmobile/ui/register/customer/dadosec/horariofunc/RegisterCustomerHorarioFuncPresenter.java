package com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

public interface RegisterCustomerHorarioFuncPresenter extends
        BasePresenter<RegisterCustomerHorarioFuncView> {

    void initializeData(List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc);

    void doSave(List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc);
}
