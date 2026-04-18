package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.dao.PendenciaMotivoDao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.services.PendenciaMotivoService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaMotivoManagerImpl implements PendenciaMotivoManager {

    private PendenciaMotivoService service;
    private PendenciaMotivoDao pendenciaMotivoDao;

    public PendenciaMotivoManagerImpl(PendenciaMotivoService service, PendenciaMotivoDao pendenciaMotivoDao) {
        this.service = service;
        this.pendenciaMotivoDao = pendenciaMotivoDao;
    }

    @Nullable
    @Override
    public PendenciaMotivo obterPorId(String id) {
        return pendenciaMotivoDao.obterPorId(id);
    }

    @Override
    public Observable<List<PendenciaMotivo>> obterTodos() {
        return pendenciaMotivoDao.obterTodos();
    }

    @Override
    public Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes) {
        return pendenciaMotivoDao.obterTodosComCliente(clientes);
    }

    @Override
    public Single<List<PendenciaMotivo>> serverGetAll(String vendedorId, String carga) {
        return service.getAll(vendedorId, carga);
    }

    @Override
    public Single<PendenciaMotivo> save(PendenciaMotivo pendenciaMotivo) {
        return service.save(pendenciaMotivo);
    }
}
