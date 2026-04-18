package com.axys.redeflexmobile.ui.register.customer.commercial;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerCommercialView extends RegisterCustomerCommon {

    void fillInterfaceWithProspectData(ProspectingClientAcquisition prospecting);

    void fillNegotiation(List<ICustomSpinnerDialogModel> negotiationTermList, ProspectingClientAcquisition prospect);

    void fillAnticipation(List<ICustomSpinnerDialogModel> anticipationList, ProspectingClientAcquisition prospect);

    void fillAnticipationValue(List<CustomerRegisterTax> anticipationListValue);

    void fillPosList(List<MachineType> posList);

    void fillDueList(List<ICustomSpinnerDialogModel> dueList);

    void fillExemption(List<ICustomSpinnerDialogModel> exemptionList);

    void fillTotalValue(String value);

    void initializeFieldValues(CustomerRegister customerRegister);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess();

    void onValidationSuccessBack();

    void setNumeroTitulo(String pNumero);
}
