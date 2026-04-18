package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import timber.log.Timber;

/**
 * @author Lucas Marciano on 30/04/2020
 */
public class RegisterProspectListPresenterImpl extends BasePresenterImpl<RegisterProspectListView> implements RegisterProspectListPresenter {

    private final ProspectingClientAcquisitionManager manager;

    public RegisterProspectListPresenterImpl(RegisterProspectListView view,
                                             SchedulerProvider schedulerProvider,
                                             ExceptionUtils exceptionUtils,
                                             ProspectingClientAcquisitionManager manager
    ) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void loadData() {
        compositeDisposable.add(manager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(getView()::fillAdapter, Timber::e));
    }

    @Override
    public void loadDataByFilter(String filter) {
        compositeDisposable.add(manager.searchBy(filter)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(getView()::fillAdapter, Timber::e));
    }
}
