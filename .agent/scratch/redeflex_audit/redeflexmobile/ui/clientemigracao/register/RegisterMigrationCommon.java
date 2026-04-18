package com.axys.redeflexmobile.ui.clientemigracao.register;

import com.axys.redeflexmobile.shared.mvp.BaseView;

/**
 * @author lucasmarciano on 02/04/20
 */
public interface RegisterMigrationCommon extends BaseView {
    RegisterMigrationView getParentActivity();

    void persistData();

    void persistCloneData();
}
