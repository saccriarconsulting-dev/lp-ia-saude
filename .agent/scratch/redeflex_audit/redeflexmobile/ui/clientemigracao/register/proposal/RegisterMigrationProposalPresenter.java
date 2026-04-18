package com.axys.redeflexmobile.ui.clientemigracao.register.proposal;

import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author lucasmarciano on 06/04/20
 */
public interface RegisterMigrationProposalPresenter extends BasePresenter<RegisterMigrationProposalView> {

    void initializeData(RegisterMigrationSub registerMigrationSub);

    void loadNegotiationPeriod();

    void loadDataAutomaticAnticipation();

    void doSave(RegisterMigrationSub registerMigrationSub);

    void finalizeFlow(RegisterMigrationSub request, boolean isBack);
}
