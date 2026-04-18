package com.axys.redeflexmobile.ui.clientemigracao.register.taxes;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankEvent;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Lucas Marciano on 07/04/2020
 */
public interface RegisterMigrationTaxesPresenter extends BasePresenter<RegisterMigrationTaxesView>, LoadFlagsBankEvent {

    //void initialize(RegisterMigrationSub registerMigrationSub);

    void loadCurrentTaxes(RegisterMigrationSub registerMigrationSub);

    void loadNewTaxes(RegisterMigrationSub registerMigrationSub);

    void doSave(RegisterMigrationSub registerMigrationSub);

    void selectTaxOption(int taxType);

    void finalizeFlow(boolean isBack);

    void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value);

    void loadMotivesCancelMigration();

    void saveMigrationCancelMotive(int motiveId, String observation);
}
