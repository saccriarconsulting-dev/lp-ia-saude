package com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc;

import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.List;

public interface RegisterMigrationHorarioFuncPresenter extends
        BasePresenter<RegisterMigrationHorarioFuncView> {

    void initializeData(List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios);

    void doSave(List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios);
}
