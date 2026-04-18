package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.ui.solictrocaprodutos.dialog.SolicitacaoTrocaDialog.SolicitarTrocaListener;

/**
 * Created by Rogério Massa on 08/10/18.
 */

interface InformarCodigoBarraView extends BaseView {

    void popularTela(SolicitacaoTroca solicitacao);

    void popularListagem();

    void exibirErro(String mensagem, SolicitarTrocaListener callback);

    void abrirSelecionarProdutosBipagem(int codBarraId);

    void onBackPressed();
}
