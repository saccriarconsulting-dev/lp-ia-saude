package com.axys.redeflexmobile.ui.clientemigracao.register;

import com.axys.redeflexmobile.ui.base.BaseFragment;

/**
 * @author lucasmarciano on 02/04/20
 */
public abstract class RegisterMigrationCommonImpl extends BaseFragment<RegisterMigrationActivity> implements RegisterMigrationCommon {

    @Override
    public RegisterMigrationView getParentActivity() {
        return parentActivity;
    }

    @Override
    public void persistData() {
        // Not used
    }

    @Override
    public void persistCloneData() {
        // Not used
    }
}
