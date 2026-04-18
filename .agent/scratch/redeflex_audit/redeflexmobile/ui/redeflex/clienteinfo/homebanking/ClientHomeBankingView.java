package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import java.util.List;

/**
 * @author lucasmarciano on 01/07/20
 */
public interface ClientHomeBankingView extends BaseActivityView {
    void fillAdapterList(List<ClientHomeBanking> list);

    void showMainLoading();

    void hideMainLoading();
}
