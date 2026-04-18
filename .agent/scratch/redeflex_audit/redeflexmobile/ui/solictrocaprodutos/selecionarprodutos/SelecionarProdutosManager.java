package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 04/10/2018.
 */

public interface SelecionarProdutosManager {

    ArrayList<Produto> obterProdutosSelecaoTroca();

    List<SolicitacaoTrocaMotivo> obterMotivos();
}
