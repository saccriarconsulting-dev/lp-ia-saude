package com.axys.redeflexmobile.ui.clientemigracao.register.proposal;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommon;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.List;

/**
 * @author lucasmarciano on 06/04/20
 */
public interface RegisterMigrationProposalView extends RegisterMigrationCommon {

    void fillSpinnerNegotiationPeriod(List<ICustomSpinnerDialogModel> list);

    void fillSpinnerAutomaticAnticipation(List<ICustomSpinnerDialogModel> list);

    void initializeInterface(RegisterMigrationSub registerMigrationSub,
                             @Nullable ICustomSpinnerDialogModel negotiationPeriod,
                             @Nullable ICustomSpinnerDialogModel automaticAnticipation);

    void onValidationSuccess(RegisterMigrationSub request);

    void onValidationSuccessBack();

    void setErrors(List<EnumRegisterFields> errors);
}
