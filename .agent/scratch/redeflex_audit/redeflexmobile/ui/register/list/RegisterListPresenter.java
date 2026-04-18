package com.axys.redeflexmobile.ui.register.list;

import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Rogério Massa on 21/11/18.
 */

public interface RegisterListPresenter extends BasePresenter<RegisterListView> {

    void getRegisters(final String filter);

    void validateToOpenRelease(final RegisterListItem register);

    void validateToken(final RegisterListItem item, final String token);
}
