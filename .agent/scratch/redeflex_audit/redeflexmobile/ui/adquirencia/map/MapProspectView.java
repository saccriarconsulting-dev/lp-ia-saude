package com.axys.redeflexmobile.ui.adquirencia.map;

import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author Rogério Massa on 23/11/18.
 */

public interface MapProspectView extends BaseActivityView {

    void fillMarkers(List<Ranking> list);
}
