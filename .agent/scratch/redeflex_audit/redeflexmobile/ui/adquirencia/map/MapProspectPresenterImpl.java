package com.axys.redeflexmobile.ui.adquirencia.map;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod;
import com.axys.redeflexmobile.shared.manager.MapItemManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

/**
 * @author Rogério Massa on 23/11/18.
 */

public class MapProspectPresenterImpl extends BasePresenterImpl<MapProspectView> implements MapProspectPresenter {

    private MapItemManager mapItemManager;

    public MapProspectPresenterImpl(MapProspectView view,
                             SchedulerProvider schedulerProvider,
                             ExceptionUtils exceptionUtils,
                             MapItemManager mapItemManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.mapItemManager = mapItemManager;
    }

    @Override
    public void getMapItems(EnumMapPeriod periodEnum) {
        compositeDisposable.add(mapItemManager.getMapItems(periodEnum)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(mapItems -> getView().fillMarkers(mapItems), this::showError));
    }
}
