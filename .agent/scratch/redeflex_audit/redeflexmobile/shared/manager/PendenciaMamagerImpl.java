package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.dao.PendenciaDao;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.services.PendenciaService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public class PendenciaMamagerImpl implements PendenciaManager {

    private PendenciaService service;
    private PendenciaDao pendenciaDao;

    public PendenciaMamagerImpl(PendenciaService service, PendenciaDao pendenciaDao) {
        this.service = service;
        this.pendenciaDao = pendenciaDao;
    }

    @Nullable
    @Override
    public Pendencia obterPorId(String id) {
        return pendenciaDao.obterPorId(id);
    }

    @Override
    public Observable<List<Pendencia>> obterTodos() {
        return pendenciaDao.obterTodos();
    }

    @Override
    public Single<List<Pendencia>> serverGetAll(String vendedorId, String carga) {
        return service.getAll(vendedorId, carga);
    }

    @Override
    public Single<Pendencia> save(Pendencia pendencia) {
        return service.save(pendencia);
    }

    @Override
    public Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes) {
        return pendenciaDao.obterTodosComCliente(clientes);
    }
}
