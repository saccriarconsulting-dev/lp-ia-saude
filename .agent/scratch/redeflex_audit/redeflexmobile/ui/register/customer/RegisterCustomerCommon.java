package com.axys.redeflexmobile.ui.register.customer;

import com.axys.redeflexmobile.shared.mvp.BaseView;

/**
 * @author Diego Fernando on 2019-01-04.
 */
public interface RegisterCustomerCommon extends BaseView {

    RegisterCustomerView getParentActivity();

    void persistData();

    void persistCloneData();
}
