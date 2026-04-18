package com.axys.redeflexmobile.ui.register.customer.address.addressregister;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public interface RegisterCustomerAddressView extends RegisterCustomerCommon {

    void fillStates(List<ICustomSpinnerDialogModel> list);

    void fillTipoLogradouro(List<ICustomSpinnerDialogModel> list);

    void prepareNonMainAddressLayout();

    void initializeFieldValues(CustomerRegisterAddress address);

    void blockFieldFromSearchData(List<EnumRegisterFields> fields);

    void fillAddress(Cep postalCode);

    void onBackPressed();

    void setErrors(List<EnumRegisterFields> errors);

    void showAddressError();

    void initializeFieldByProspectInfo(ProspectingClientAcquisition prospect);
}
