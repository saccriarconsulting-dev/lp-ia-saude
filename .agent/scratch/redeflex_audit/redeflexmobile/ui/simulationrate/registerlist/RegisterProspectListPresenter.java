package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author Lucas Marciano on 30/04/2020
 */
public interface RegisterProspectListPresenter extends BasePresenter<RegisterProspectListView> {

    void loadData();

    void loadDataByFilter(String filter);
}
