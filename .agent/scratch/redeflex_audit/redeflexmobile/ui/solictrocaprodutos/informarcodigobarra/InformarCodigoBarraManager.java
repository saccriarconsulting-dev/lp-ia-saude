package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Rogério Massa on 08/10/18.
 */

public interface InformarCodigoBarraManager {

    Single<SolicitacaoTroca> obterSolicitacao(int solicitacaoTrocaId);

    Completable salvarTroca(int solicitacaoTrocaId, SolicitacaoTrocaDetalhes produto);
}
