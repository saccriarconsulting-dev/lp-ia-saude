package com.axys.redeflexmobile.shared.util;

import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.util.PrecoDiferenciadoValidadorImpl.PrecoVendido;

import java.util.List;

public interface PrecoDiferenciadoValidador {

    boolean validarPrecoDiferenciado(List<ItemVendaCombo> listaFinal, Venda venda);

    PrecoVendido getPrecoVendido();
}
