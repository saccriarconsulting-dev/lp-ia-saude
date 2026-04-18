package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author lucasmarciano on 01/07/20
 */
public interface ClientHomeBankingPresenter extends BasePresenter<ClientHomeBankingView> {
    void loadData(String clientId);
}
