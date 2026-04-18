package com.axys.redeflexmobile.ui.register.customer.partners;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

public interface RegisterCustomerPartnersView extends RegisterCustomerCommon {
    void fillProfissoes(List<ICustomSpinnerDialogModel> profissoes);

    void fillRenda(List<ICustomSpinnerDialogModel> renda);

    void fillPatrimonio(List<ICustomSpinnerDialogModel> patrimonio);

    void initializeFieldValues(CustomerRegisterPartners customerRegister, String pNomeSocio);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void setNumeroTitulo(String pNumero);
}
