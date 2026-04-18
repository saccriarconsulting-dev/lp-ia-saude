package com.axys.redeflexmobile.ui.simulationrate.registertaxes;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankEvent;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Lucas Marciano on 28/04/2020
 */
public interface RegisterTaxesPresenter extends BasePresenter<RegisterTaxesView>, LoadFlagsBankEvent {

    void initializer(int prospectId);

    void loadPreSavedTaxes();

    void loadTaxes();

    void rollbackProspect();

    void selectTaxOption(int flagId);

    void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value);

    void loadGenerateProposalFunction();

    void loadProspectFunction();

    void saveTaxes();

    void saveTaxesToProspect();

    boolean checkAnticipation();

    void validateProspect();
}
