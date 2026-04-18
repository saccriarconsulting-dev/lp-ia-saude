package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommon;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.List;

/**
 * @author Lucas Marciano on 02/04/2020.
 */
interface RegisterMigrationPersonalView extends RegisterMigrationCommon {

    void fillSpinnerTypeCount(List<ICustomSpinnerDialogModel> list);

    void fillSpinnerBank(List<ICustomSpinnerDialogModel> list);

    void initializeInterface(Cliente client,
                             @Nullable ICustomSpinnerDialogModel countType,
                             @Nullable ICustomSpinnerDialogModel bank);

    void setErrors(List<EnumRegisterFields> errors);

    void onValidationSuccess(RegisterMigrationSub request);

    void callModalMotivesCancelMigration(List<MotiveMigrationSub> motiveMigrationSubs);

    void showResponseMotivesCancelMigration();

    void onValidationSuccessBack();

    void fillMigrationFields(final RegisterMigrationSub migrationSub);

    void fillProfissoes(List<ICustomSpinnerDialogModel> profissoes);

    void fillRenda(List<ICustomSpinnerDialogModel> renda);

    void fillPatrimonio(List<ICustomSpinnerDialogModel> patrimonio);

    void fillSexo(List<ICustomSpinnerDialogModel> list);

    void fillHorarioFuncList(List<CadastroMigracaoSubHorario> horarioFuncList);
}
