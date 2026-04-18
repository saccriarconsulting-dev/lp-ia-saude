package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public interface SelecionarProdutosBipagemPresenter {

    boolean isInformarCodBarrasActivity();

    void setInformarCodBarrasActivity(boolean ativa);

    void setInformarCodigoBarrasCodBarraId(Integer informarCodigoBarrasCodBarraId);

    void setInformarCodigoBarrasLista(String lista);

    void inicializarListagem(String produtos);

    Produto getProduto();

    Produto getProdutoById(String id);

    void deletarCodBarra(String codBarra);

    void inserirCodigoBarra(final String codBarra);

    SolicitacaoTrocaDetalhes obterProdutoSalvar();

    boolean deveRemoverProduto();

    void exibirMotivo();
}
