package com.axys.redeflexmobile.ui.register.customer.contato;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

public interface RegisterCustomerDadosContatoView extends RegisterCustomerCommon {
    void initializeFieldValues(CustomerRegister customerRegister);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void fillDadosContatoList(List<CustomerRegisterContato> dadosContatoList);

    void setNumeroTitulo(String pNumero);

    void setTitulo(String pTitulo);
}
