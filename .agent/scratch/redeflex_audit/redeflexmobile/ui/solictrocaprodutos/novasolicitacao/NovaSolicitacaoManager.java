package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;

import java.util.ArrayList;

import io.reactivex.Completable;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public interface NovaSolicitacaoManager {

    ArrayList<Cliente> obterClientes();

    int obterVendedorId();

    Completable iniciarSolicitacao(SolicitacaoTroca solicitacaoTroca);
}
