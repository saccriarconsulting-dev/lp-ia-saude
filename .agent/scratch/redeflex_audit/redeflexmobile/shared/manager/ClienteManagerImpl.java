package com.axys.redeflexmobile.shared.manager;

import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ColaboradorDao;
import com.axys.redeflexmobile.shared.models.Cep;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.GenericResponse;
import com.axys.redeflexmobile.shared.models.ReenviaSenhaCliente;
import com.axys.redeflexmobile.shared.services.ClienteService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ClienteManagerImpl implements ClienteManager {

    private final ClienteService service;
    private final ClienteDao clienteDao;
    private final ColaboradorDao colaboradorDao;

    public ClienteManagerImpl(ClienteService service, ClienteDao clienteDao, ColaboradorDao colaboradorDao) {
        this.service = service;
        this.clienteDao = clienteDao;
        this.colaboradorDao = colaboradorDao;
    }

    @Override
    public Single<Cep[]> obterInformacoesCep(String cep) {
        return service.obterInformacoesCep(cep);
    }

    @Override
    public Observable<List<Cliente>> obterClientes() {
        return clienteDao.obterTodos();
    }

    @Override
    public Observable<List<Cliente>> obterClientesComPendencia() {
        return clienteDao.obterClientesComPendencia();
    }

    @Override
    public Single<Cliente> obterClientePorId(int id) {
        return clienteDao.obterClientePorId(id);
    }

    @Override
    public Observable<List<Cliente>> obterClientesComPendenciaNaoRespondido() {
        return clienteDao.obterClientesComPendenciaNaoRespondido();
    }

    @Override
    public Observable<List<Cliente>> obterClienteComPendenciaNaoRespondidoPorClienteId(String idCliente) {
        return clienteDao.obterClienteComPendenciaNaoRespondidoPorClienteId(idCliente);
    }

    @Override
    public Single<Colaborador> obterColaborador() {
        return Single.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            Colaborador colaborador = colaboradorDao.get();
            emitter.onSuccess(colaborador);
        });
    }

    @Override
    public Observable<List<Cliente>> obterClientesMigracao() {
        return clienteDao.obterClientesMigracao();
    }

    @Override
    public Observable<List<Cliente>> obterCadastroMigracaoSub() {
        return clienteDao.obterCadastroMigracaoSub();
    }

    @Override
    public Observable<List<Cliente>> obterClientesPendentesMigracao() {
        return clienteDao.obterClientesPendentesMigracao();
    }

    @Override
    public Single<GenericResponse> obterSenhaSgv(ReenviaSenhaCliente reenviaSenhaCliente) {
        return service.obterSenhaSgv(reenviaSenhaCliente);
    }

    @Override
    public Single<Boolean> updateNegotiationMigrateSub(int clintId) {
        return clienteDao.updateNegotiationMigrateSub(clintId);
    }
}
