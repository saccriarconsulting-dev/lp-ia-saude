package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 04/10/2018.
 */

public class SelecionarProdutosManagerImpl implements SelecionarProdutosManager {

    private DBEstoque dbEstoque;
    private DBSolicitacaoTroca dbSolicitacaoTroca;

    public SelecionarProdutosManagerImpl(DBEstoque dbEstoque, DBSolicitacaoTroca dbSolicitacaoTroca) {
        this.dbEstoque = dbEstoque;
        this.dbSolicitacaoTroca = dbSolicitacaoTroca;
    }

    @Override
    public ArrayList<Produto> obterProdutosSelecaoTroca() {
        return dbEstoque.obterProdutosSolicitacaoTroca();
    }

    @Override
    public List<SolicitacaoTrocaMotivo> obterMotivos() {
        return dbSolicitacaoTroca.obterMotivos();
    }
}
