package com.axys.redeflexmobile.ui.clientemigracao.register.taxes;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankListener;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommon;

import java.util.List;

/**
 * @author Lucas Marciano on 07/04/2020
 */
public interface RegisterMigrationTaxesView extends RegisterMigrationCommon, LoadFlagsBankListener {

    void initializeInterface(RegisterMigrationSubTax item);

    void initializeInterfaceNoTaxes(int flag);

    void onValidationSuccess(List<RegisterMigrationSubTax> list);

    void onValidationSuccessBack(List<RegisterMigrationSubTax> list);

    void showResponseMotivesCancelMigration();

    void callModalMotivesCancelMigration(List<MotiveMigrationSub> motiveMigrationSubs);

    void showDialogNotFoundTaxes();

    void saveCurrentState();

    void setErrors(List<EnumRegisterFields> errors);
}
