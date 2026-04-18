package com.axys.redeflexmobile.ui.adquirencia.prospectinfo;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */

public interface ProspectInfoPresenter extends BasePresenter<ProspectInfoView> {

    void getProspectInfo(int prospectId);
}
