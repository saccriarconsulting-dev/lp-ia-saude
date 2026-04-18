package com.axys.redeflexmobile.ui.register.customer;

import com.axys.redeflexmobile.ui.base.BaseFragment;

/**
 * @author Rogério Massa on 14/01/19.
 */

public abstract class RegisterCustomerCommonImpl extends BaseFragment<RegisterCustomerActivity>
        implements RegisterCustomerCommon {

    @Override
    public RegisterCustomerView getParentActivity() {
        return parentActivity;
    }

    @Override
    public void persistData() {
        //unused
    }

    @Override
    public void persistCloneData() {
        //unused
    }
}
