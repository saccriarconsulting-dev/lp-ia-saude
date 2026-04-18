package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import android.content.Context;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.mvp.BaseView;

import java.util.List;

/**
 * Created by Rogério Massa on 15/10/18.
 */

interface SelecionarProdutosBipagemView extends BaseView {

    Context getContext();

    void exibirErro(String mensagem);

    void carregarListagem(List<SolicitacaoTrocaCodBarras> lista, boolean camera);

    void abrirCamera();

    void retornarInformarCodigoBarraActivity(Integer codBarraId, String codBarra);

    void exibirMotivos(List<SolicitacaoTrocaMotivo> motivos, SolicitacaoTrocaDetalhes produto);

    void sairSemAlteracao();

}
