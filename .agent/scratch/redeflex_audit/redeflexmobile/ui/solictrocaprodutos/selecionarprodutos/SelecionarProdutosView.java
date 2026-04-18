package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos;

import android.content.Context;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * Created by Rogério Massa on 01/10/2018.
 */

public interface SelecionarProdutosView extends BaseView {

    Context getContext();

    void carregarListagem(List<SolicitacaoTrocaDetalhes> produtos);

    void exibirErro(String mensagem);

    void exibirMotivos(List<SolicitacaoTrocaMotivo> motivos, SolicitacaoTrocaDetalhes produto);
}
