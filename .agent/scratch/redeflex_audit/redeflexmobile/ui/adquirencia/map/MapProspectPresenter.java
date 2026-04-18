package com.axys.redeflexmobile.ui.adquirencia.map;

import com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Rogério Massa on 23/11/18.
 */

public interface MapProspectPresenter extends BasePresenter<MapProspectView> {

    void getMapItems(EnumMapPeriod periodEnum);
}
