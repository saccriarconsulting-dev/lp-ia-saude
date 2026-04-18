package com.axys.redeflexmobile.ui.register.customer.personal;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerPersonalPresenter extends BasePresenter<RegisterCustomerPersonalView> {

    void initializeData();

    void loadMccList(int personType);

    void clearAndChangeFields(ICustomSpinnerDialogModel selectedCustomerType,
                              ICustomSpinnerDialogModel selectedPersonType,
                              boolean clearValues);

    void doSave(CustomerRegister request);

    void verifyRegister(String text, ICustomSpinnerDialogModel selectedPersonType, ICustomSpinnerDialogModel customerType);

    void verifyRegisterCPF(String pCPF, String pDataNasc, ICustomSpinnerDialogModel selectedPersonType, ICustomSpinnerDialogModel customerType);

    void saveData(CustomerRegister request, boolean isBack);

    void onMccChanged(Integer mcc);

    boolean areProspectProcess();

    void queryCnpj(String cnpj, boolean dialogResponse);

    void queryCPF(String cpf, String pDataNasc, boolean dialogResponse);
}
