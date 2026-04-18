package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaMotivoManager {

    @Nullable
    PendenciaMotivo obterPorId(String id);

    Observable<List<PendenciaMotivo>> obterTodos();

    Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes);

    Single<List<PendenciaMotivo>> serverGetAll(String vendedorId, String carga);

    Single<PendenciaMotivo> save(PendenciaMotivo pendenciaMotivo);
}
