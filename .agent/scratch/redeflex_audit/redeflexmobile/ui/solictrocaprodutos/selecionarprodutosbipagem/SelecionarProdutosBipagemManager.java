package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public interface SelecionarProdutosBipagemManager {

    Produto obterProdutoPorId(String id);

    Iccid getIccIdById(String id);

    boolean verificaEstoque(String idProduto, int qtde);
}
