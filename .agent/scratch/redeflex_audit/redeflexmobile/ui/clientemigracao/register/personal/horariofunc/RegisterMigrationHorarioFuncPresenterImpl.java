package com.axys.redeflexmobile.ui.clientemigracao.register.personal.horariofunc;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationView;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

public class RegisterMigrationHorarioFuncPresenterImpl extends BasePresenterImpl<RegisterMigrationHorarioFuncView>
        implements RegisterMigrationHorarioFuncPresenter {

    private final RegisterMigrationSubManager manager;
    private RegisterMigrationSub registerMigrationSub;
    private List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios;

    public RegisterMigrationHorarioFuncPresenterImpl(RegisterMigrationHorarioFuncView view,
                                                    SchedulerProvider schedulerProvider,
                                                    ExceptionUtils exceptionUtils,
                                                    RegisterMigrationSubManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void initializeData(List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios) {
        getView().carregaLista_Horario(new ArrayList<>(EnumRegisterHorarioFunc.getEnumList()));

        this.cadastroMigracaoSubHorarios = cadastroMigracaoSubHorarios;

        RegisterMigrationView parentActivity = getView().getParentActivity();
        registerMigrationSub = parentActivity.recoverObjectMigration();
    }

    @Override
    public void doSave(List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios) {
        saveData(cadastroMigracaoSubHorarios);
    }

    private void saveData(List<CadastroMigracaoSubHorario> cadastroMigracaoSubHorarios) {
        registerMigrationSub.setHorarioFuncionamento(new ArrayList<>());

        for (int aa = 0; aa < cadastroMigracaoSubHorarios.size(); aa++) {
            registerMigrationSub.getHorarioFuncionamento().add(cadastroMigracaoSubHorarios.get(aa));
        }
        getView().onBackPressed();
    }
}
