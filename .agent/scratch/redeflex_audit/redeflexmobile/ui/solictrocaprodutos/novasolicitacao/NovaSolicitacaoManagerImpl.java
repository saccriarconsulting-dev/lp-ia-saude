package com.axys.redeflexmobile.ui.solictrocaprodutos.novasolicitacao;

import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;

import java.util.ArrayList;

import io.reactivex.Completable;

/**
 * Created by Rogério Massa on 03/10/2018.
 */

public class NovaSolicitacaoManagerImpl implements NovaSolicitacaoManager {

    private DBCliente dbCliente;
    private DBColaborador dbColaborador;
    private DBSolicitacaoTroca dbSolicitacaoTroca;

    public NovaSolicitacaoManagerImpl(DBCliente dbCliente,
                                      DBColaborador dbColaborador,
                                      DBSolicitacaoTroca dbSolicitacaoTroca) {
        this.dbCliente = dbCliente;
        this.dbColaborador = dbColaborador;
        this.dbSolicitacaoTroca = dbSolicitacaoTroca;
    }

    @Override
    public ArrayList<Cliente> obterClientes() {
        return dbCliente.getAll();
    }

    @Override
    public int obterVendedorId() {
        return dbColaborador.get().getId();
    }

    @Override
    public Completable iniciarSolicitacao(SolicitacaoTroca solicitacaoTroca) {
        return dbSolicitacaoTroca.iniciarSolicitacao(solicitacaoTroca);
    }
}
