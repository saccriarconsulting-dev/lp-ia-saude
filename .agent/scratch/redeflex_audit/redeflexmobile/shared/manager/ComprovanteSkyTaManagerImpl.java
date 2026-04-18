package com.axys.redeflexmobile.shared.manager;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBComprovanteSkyTa;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComprovanteSkyTa;
import com.axys.redeflexmobile.shared.services.bus.ComprovanteSkyTaBus;

import java.util.List;

import io.reactivex.Single;

public class ComprovanteSkyTaManagerImpl implements ComprovanteSkyTaManager {

    private DBComprovanteSkyTa db;
    private DBCliente dbCliente;
    private DBColaborador dbColaborador;
    private ComprovanteSkyTaBus servico;

    public ComprovanteSkyTaManagerImpl(DBComprovanteSkyTa db,
                                       DBCliente dbCliente,
                                       DBColaborador dbColaborador,
                                       ComprovanteSkyTaBus servico) {
        this.db = db;
        this.dbCliente = dbCliente;
        this.dbColaborador = dbColaborador;
        this.servico = servico;
    }

    @Override
    public void salvar(ComprovanteSkyTa comprovanteSkyTa) {
        new Thread(() -> db.salvar(comprovanteSkyTa)).start();
    }

    @Override
    public void sincronizar(ComprovanteSkyTa comprovanteSkyTa) {
        new Thread(() -> servico.sincronizar()).start();
    }

    @Override
    public Single<List<Cliente>> obterClientes() {
        return Single.create(emitter -> {
            List<Cliente> clientes = Stream.ofNullable(dbCliente.getAll())
                    .sortBy(Cliente::getNomeFantasia)
                    .toList();
            if (clientes.isEmpty()) {
                return;
            }

            emitter.onSuccess(clientes);
        });
    }

    @Override
    public Colaborador obterColaborador() {
        return dbColaborador.get();
    }
}
