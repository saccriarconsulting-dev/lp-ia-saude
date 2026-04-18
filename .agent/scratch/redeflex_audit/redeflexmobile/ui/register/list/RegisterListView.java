package com.axys.redeflexmobile.ui.register.list;

import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.shared.util.IGPSTracker;

import java.util.List;

/**
 * @author Rogério Massa on 21/11/18.
 */

public interface RegisterListView extends BaseView {

    void hideLoadingDelayed();

    void fillRegisterList(final List<RegisterListItem> list);

    IGPSTracker getGpsTracker();

    void validacaoNaoEstaNasImediacoes();

    void openRelease(final RegisterListItem registerListItem);

    void showTokenValidationSuccess();

    void showTokenValidationFailure(RegisterListItem item);
}
