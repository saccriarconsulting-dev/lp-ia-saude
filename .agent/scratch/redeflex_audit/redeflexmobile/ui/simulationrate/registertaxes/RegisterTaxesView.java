package com.axys.redeflexmobile.ui.simulationrate.registertaxes;

import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankListener;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.mvp.BaseView;

/**
 * @author Lucas Marciano on 28/04/2020
 */
public interface RegisterTaxesView extends BaseView, LoadFlagsBankListener {

    void endProspectProcess(boolean isCancel);

    void openDialogToCheckPersonalInfo();

    void fillInterface(RegistrationSimulationFee tax);

    void showDialogNotFoundTaxes();

    void goToGenerateNewProspect();

    void updateInterfaceVisibility(ProspectingClientAcquisition prospect);

    void validateProspect();
}
