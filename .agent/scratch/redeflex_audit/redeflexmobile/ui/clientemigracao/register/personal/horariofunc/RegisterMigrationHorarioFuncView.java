package com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc;

import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommon;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.List;

public interface RegisterMigrationHorarioFuncView extends RegisterMigrationCommon {
    void carregaLista_Horario(List<ICustomSpinnerDialogModel> list);

    void onBackPressed();
}
