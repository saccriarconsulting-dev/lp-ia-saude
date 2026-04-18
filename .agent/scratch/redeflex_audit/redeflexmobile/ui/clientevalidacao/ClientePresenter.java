package com.axys.redeflexmobile.ui.clientevalidacao;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

public interface ClientePresenter extends BasePresenter<ClienteView> {

    void loadMotivesCancelMigration();

    void saveMigrationCancel(RegisterMigrationSub registerMigrationSub);

    void checkRegisterMigrationAreCanceled(String clientId, String tipoMigracao);
}
