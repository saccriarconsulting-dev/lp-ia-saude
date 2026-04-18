package com.axys.redeflexmobile.ui.adquirencia.release;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Rogério Massa on 04/12/18.
 */

public interface ReleasePresenter extends BasePresenter<ReleaseView> {

    void loadReleases(int clientId);

    void reloadReleases(int clientId);
}
