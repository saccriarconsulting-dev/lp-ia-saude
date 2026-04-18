package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Rogério Massa on 08/10/18.
 */

public class InformarCodigoBarraManagerImpl implements InformarCodigoBarraManager {

    private DBSolicitacaoTroca dbSolicitacaoTroca;

    public InformarCodigoBarraManagerImpl(DBSolicitacaoTroca dbSolicitacaoTroca) {
        this.dbSolicitacaoTroca = dbSolicitacaoTroca;
    }

    @Override
    public Single<SolicitacaoTroca> obterSolicitacao(int solicitacaoTrocaId) {
        return dbSolicitacaoTroca.obterSolicitacaoPorId(solicitacaoTrocaId);
    }

    @Override
    public Completable salvarTroca(int solicitacaoTrocaId, SolicitacaoTrocaDetalhes produto) {
        return dbSolicitacaoTroca.trocarProdutosComBipagem(solicitacaoTrocaId, produto);
    }


}
