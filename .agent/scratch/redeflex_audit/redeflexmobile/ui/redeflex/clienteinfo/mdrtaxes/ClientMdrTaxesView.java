package com.axys.redeflexmobile.ui.redeflex.clienteinfo.mdrtaxes;

import com.axys.redeflexmobile.shared.models.clientinfo.ClientTaxMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.mvp.BaseActivityView;

import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * @author lucasmarciano on 30/06/20
 */
public interface ClientMdrTaxesView extends BaseActivityView {

    void fillViewData(@Nullable ClientTaxMdr clientMdrTaxes, FlagsBank flagsBank);

    void fillAdapterFlags(List<FlagsBank> flagList);

    void showMainLoading();

    void hideMainLoading();
}
