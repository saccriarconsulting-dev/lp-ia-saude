package com.axys.redeflexmobile.shared.repository;


import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.models.AtualizaCliente;

public class AtualizarClienteRepositoryImpl implements AtualizarClienteRepository {

    private DBAtualizarCliente dbAtualizarCliente;
    private ClienteDao clienteDao;

    public AtualizarClienteRepositoryImpl(DBAtualizarCliente dbAtualizarCliente,
                                          ClienteDao clienteDao) {
        this.dbAtualizarCliente = dbAtualizarCliente;
        this.clienteDao = clienteDao;
    }

    @Override
    public void salvar(AtualizaCliente atualizaCliente) {
        new Thread(() -> {
            dbAtualizarCliente.salvar(atualizaCliente);
            clienteDao.confirmaAtualizacaoCadastral(atualizaCliente.getId());
        }).start();
    }

    @Override
    public void obterPorId(String id, AsyncRepository<AtualizaCliente> async) {
        new Thread(() -> {
            AtualizaCliente cliente = dbAtualizarCliente.obterPorId(id);
            async.getResult(cliente);
        }).start();
    }
}
