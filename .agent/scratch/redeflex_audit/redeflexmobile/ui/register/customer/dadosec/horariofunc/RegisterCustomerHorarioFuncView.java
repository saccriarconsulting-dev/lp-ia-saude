package com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc;

import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommon;

import java.util.List;

public interface RegisterCustomerHorarioFuncView extends RegisterCustomerCommon {
    void carregaLista_Horario(List<ICustomSpinnerDialogModel> list);

    void onBackPressed();
}
