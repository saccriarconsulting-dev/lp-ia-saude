package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public interface NovaSolicitacaoPresenter extends BasePresenter<NovaSolicitacaoView> {

    List<SolicitacaoTrocaDetalhes> getProdutos();

    void setProdutos(List<SolicitacaoTrocaDetalhes> produtos);

    void setCliente(int index);

    ArrayList<Cliente> obterClientes();

    void removerProduto(String produtoCodigo);

    boolean possoVoltar();

    void salvarSolicitacao();
}
