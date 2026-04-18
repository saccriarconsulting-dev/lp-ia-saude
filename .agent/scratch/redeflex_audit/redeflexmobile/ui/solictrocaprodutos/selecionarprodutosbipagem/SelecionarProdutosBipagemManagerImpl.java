package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBIccid;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public class SelecionarProdutosBipagemManagerImpl implements SelecionarProdutosBipagemManager {

    private DBEstoque dbEstoque;
    private DBIccid dbIccid;

    public SelecionarProdutosBipagemManagerImpl(DBEstoque dbEstoque, DBIccid dbIccid) {
        this.dbEstoque = dbEstoque;
        this.dbIccid = dbIccid;
    }

    @Override
    public Produto obterProdutoPorId(String id) {
        return dbEstoque.getProdutoById(id);
    }

    @Override
    public Iccid getIccIdById(String id) {
        return dbIccid.getByCodigo(id);
    }

    @Override
    public boolean verificaEstoque(String idProduto, int qtde) {
        return dbEstoque.verificaEstoque(idProduto, qtde);
    }
}
