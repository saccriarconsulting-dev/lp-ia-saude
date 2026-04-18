package com.axys.redeflexmobile.ui.register.customer.financial;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
interface RegisterCustomerFinancialView extends RegisterCustomerCommon {

    void fillAccountTypes(List<ICustomSpinnerDialogModel> accountTypes);

    void fillBanks(List<ICustomSpinnerDialogModel> banks);

    void initializeFieldValues(CustomerRegister customerRegister);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void setNumeroTitulo(String pNumero);
}
