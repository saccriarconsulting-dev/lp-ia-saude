package com.axys.redeflexmobile.ui.register.customer.commercial.pos;

import com.axys.redeflexmobile.shared.models.ModeloPOSConexao;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Rogério Massa on 18/02/19.
 */

public interface RegisterCustomerCommercialPosPresenter extends BasePresenter<RegisterCustomerCommercialPosView> {

    void setPositionInList(Integer positionInList);

    void getPosById(final int id);

    void getPosConnection(final ModeloPOSConexao modeloPOSConexao);

    void initializeData();

    void savePos(final MachineType machineType);
}
