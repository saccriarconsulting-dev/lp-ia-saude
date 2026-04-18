package com.axys.redeflexmobile.ui.register.customer.commercial.pos;

import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

/**
 * @author Rogério Massa on 18/02/19.
 */

public interface RegisterCustomerCommercialPosView extends RegisterCustomerCommon {

    void onBackPressed();

    void fillPosModel(List<ICustomSpinnerDialogModel> list);

    void showCarriersField(List<ICustomSpinnerDialogModel> carriers);

    void setPosFields(ModeloPOS machineType);

    void hideCarriersField();

    void showCableLengthField();

    void hideCableLengthField();
}
