package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public interface NovaSolicitacaoView extends BaseView {

    void onBackPressed();

    void carregarListagem(List<SolicitacaoTrocaDetalhes> produtos);

    void exibirErro(String mensagem);

    void abrirProdutos();
}
