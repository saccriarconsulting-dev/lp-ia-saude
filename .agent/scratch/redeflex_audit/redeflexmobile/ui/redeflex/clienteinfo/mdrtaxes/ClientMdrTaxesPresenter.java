package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import com.axys.redeflexmobile.shared.mvp.BasePresenter;

/**
 * @author lucasmarciano on 30/06/20
 */
public interface ClientMdrTaxesPresenter extends BasePresenter<ClientMdrTaxesView> {
    void loadData(String clientId);

    void loadTaxesByFlagId(int optionId);

    void loadFlags();
}
