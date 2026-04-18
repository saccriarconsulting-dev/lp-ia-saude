package com.axys.redeflexmobile.ui.clientemigracao.register;

import androidx.fragment.app.Fragment;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

/**
 * @author lucasmarciano on 02/04/20
 */
public interface RegisterMigrationView extends BaseActivityView {
    void openFragmentWithoutBottomBar(Fragment fragment);

    void closeFragmentWithoutBottomBarFromPersonal();

    void closeFragmentWithoutBottomBarFromProposal();

    void closeFragmentWithoutBottomBarFromHorarioFunc();

    /**
     * Initialize flow of the fragments
     */
    void initializeFlow();

    /**
     * Return the client id send in the bundle.
     * @return int
     */
    int getClientId();

    String getTipoMigracao();

    /**
     * Save temporally object of the migration
     *
     * @param registerMigrationSub [RegisterMigrationSub]
     */
    void saveObjectMigration(RegisterMigrationSub registerMigrationSub);

    /**
     * Recovery temporally object of the migration
     *
     * @return RegisterMigrationSub
     */
    RegisterMigrationSub recoverObjectMigration();

    /**
     * Implement the behavior of the end of process
     */
    void onSaveSuccess();

    void stepValidated();

    void stepValidatedBack();

    void cancelFlow();
}
