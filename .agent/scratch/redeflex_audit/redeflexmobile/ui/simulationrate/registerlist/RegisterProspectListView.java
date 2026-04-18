package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * @author Lucas Marciano on 30/04/2020
 */
public interface RegisterProspectListView extends BaseView {

    void fillAdapter(List<ProspectingClientAcquisition> prospectList);
}
