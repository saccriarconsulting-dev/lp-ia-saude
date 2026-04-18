package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.clientinfo.ClientHomeBankingManager;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import timber.log.Timber;

/**
 * @author lucasmarciano on 01/07/20
 */
public class ClientHomeBankingPresenterImpl extends BasePresenterImpl<ClientHomeBankingView>
        implements ClientHomeBankingPresenter {

    private final ClientHomeBankingManager clientHomeBankingManager;

    public ClientHomeBankingPresenterImpl(ClientHomeBankingView view,
                                          SchedulerProvider schedulerProvider,
                                          ExceptionUtils exceptionUtils,
                                          ClientHomeBankingManager clientHomeBankingManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.clientHomeBankingManager = clientHomeBankingManager;
    }

    @Override
    public void loadData(String clientId) {
        compositeDisposable.add(clientHomeBankingManager.getByClientId(Integer.parseInt(clientId))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showMainLoading())
                .doFinally(getView()::hideMainLoading)
                .subscribe(getView()::fillAdapterList, Timber::e));
    }
}
