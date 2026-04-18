package com.axys.redeflexmobile.ui.solictrocaprodutos.informarcodigobarra;

import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rogério Massa on 08/10/18.
 */

public interface InformarCodigoBarraPresenter extends BasePresenter<InformarCodigoBarraView> {

    void setProdutoId(String produtoId);

    void obterSolicitacao(int solicitacaoTrocaId);

    SolicitacaoTrocaDetalhes obterProduto();

    HashMap<String, Integer> obterQuantidadeAntigosNovos();

    List<SolicitacaoTrocaCodBarras> obterProdutoCodBarras();

    void realizarLeitura(boolean salvar);

    void inserirCodigoBarra(Integer codBarraId, String codBarra);

    boolean houveAlteracao();

    void salvar();
}
