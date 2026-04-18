package com.axys.redeflexmobile.ui.clientevalidacao;

import com.axys.redeflexmobile.shared.models.migracao.MotiveMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

public interface ClienteView extends BaseActivityView {

    void callModalMotivesCancelMigration(List<MotiveMigrationSub> list);

    void showResponseMotivesCancelMigration();

    void registerAreCanceled(RegisterMigrationSub registerMigrationSub);
}
