package com.axys.redeflexmobile.ui.clientemigracao.register;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import org.jetbrains.annotations.NotNull;

/**
 * @author lucasmarciano on 02/04/20
 */
interface RegisterMigrationPresenter extends BasePresenter<RegisterMigrationView> {

    void saveMigration(@NotNull RegisterMigrationSub registerMigrationSub);

}
