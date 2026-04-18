package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.dao.PendenciaClienteDao;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.services.PendenciaClienteService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaClienteManagerImpl implements PendenciaClienteManager {

    private final PendenciaClienteService service;
    private final PendenciaClienteDao pendenciaClienteDao;

    public PendenciaClienteManagerImpl(PendenciaClienteService service, PendenciaClienteDao pendenciaClienteDao) {
        this.service = service;
        this.pendenciaClienteDao = pendenciaClienteDao;
    }

    @Nullable
    @Override
    public PendenciaCliente obterPorId(String id) {
        return pendenciaClienteDao.obterPorId(id);
    }

    @Override
    public Observable<List<PendenciaCliente>> obterTodos() {
        return pendenciaClienteDao.obterTodos();
    }

    @Override
    public Observable<Integer> updateMotivo(PendenciaCliente pendenciaCliente) {
        return pendenciaClienteDao.updateMotivo(pendenciaCliente);
    }

    @Override
    public Single<List<PendenciaCliente>> serverGetAll(String vendedorId, String carga) {
        return service.getAll(vendedorId, carga);
    }

    @Override
    public Single<PendenciaCliente> save(PendenciaCliente pendenciaCliente) {
        return service.save(pendenciaCliente);
    }
}
