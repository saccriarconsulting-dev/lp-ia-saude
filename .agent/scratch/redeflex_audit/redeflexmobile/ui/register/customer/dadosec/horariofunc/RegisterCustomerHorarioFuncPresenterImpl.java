package com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

public class RegisterCustomerHorarioFuncPresenterImpl extends BasePresenterImpl<RegisterCustomerHorarioFuncView>
        implements RegisterCustomerHorarioFuncPresenter {

    private final CustomerRegisterManager manager;
    private CustomerRegister customerRegister;
    private List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc;

    public RegisterCustomerHorarioFuncPresenterImpl(RegisterCustomerHorarioFuncView view,
                                                    SchedulerProvider schedulerProvider,
                                                    ExceptionUtils exceptionUtils,
                                                    CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void initializeData(List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc) {
        getView().carregaLista_Horario(new ArrayList<>(EnumRegisterHorarioFunc.getEnumList()));

        this.customerRegisterHorarioFunc = customerRegisterHorarioFunc;

        RegisterCustomerView parentActivity = getView().getParentActivity();
        customerRegister = parentActivity.getCustomerRegister();
    }

    @Override
    public void doSave(List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc) {
        
        saveData(customerRegisterHorarioFunc);
    }

    private void saveData(List<CustomerRegisterHorarioFunc> customerRegisterHorarioFunc) {
        customerRegister.setHorarioFunc(new ArrayList<>());

        for (int aa = 0; aa < customerRegisterHorarioFunc.size(); aa++) {
            customerRegister.getHorarioFunc().add(customerRegisterHorarioFunc.get(aa));
        }
        getView().onBackPressed();
    }
}
