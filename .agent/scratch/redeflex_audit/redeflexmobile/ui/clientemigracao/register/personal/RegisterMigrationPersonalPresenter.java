package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Lucas Marciano on 02/04/2020.
 */
public interface RegisterMigrationPersonalPresenter extends BasePresenter<RegisterMigrationPersonalView> {

    void initializeData(int clientId);

    void loadTypeCount();

    void loadDataBank();

    void doSave(RegisterMigrationSub registerMigrationSub);

    void finalizeFlow(RegisterMigrationSub request, boolean isBack);

    void loadMotivesCancelMigration();

    void saveMigrationCancelMotive(int motiveId, String observation);

    void fillMigration(final RegisterMigrationSub recoverMigration);

    void loadProfissoes();

    void loadSexo();

    void loadFaixaSalarial();

    void loadPatrimonio();

}
