package com.axys.redeflexmobile.ui.adquirencia.prospectinfo;

import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

/**
 * @author Denis Gasparoto on 30/04/2019.
 */

public interface ProspectInfoView extends BaseActivityView {

    void fillProspectInfo(RouteClientProspect prospect);

    void getProspectInfoError(String error);
}
