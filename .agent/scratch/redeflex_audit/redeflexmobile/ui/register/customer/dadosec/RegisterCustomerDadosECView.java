package com.axys.redeflexmobile.ui.register.customer.dadosec;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

public interface RegisterCustomerDadosECView extends RegisterCustomerCommon {
    void carregaLista_Mcc(List<ICustomSpinnerDialogModel> list);

    void carregaLista_Segmento(List<ICustomSpinnerDialogModel> list);

    void initializeFieldValues(CustomerRegister customerRegister);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void fillHorarioFuncList(List<CustomerRegisterHorarioFunc> horarioFuncList);

    void setNumeroTitulo(String pNumero);
}
