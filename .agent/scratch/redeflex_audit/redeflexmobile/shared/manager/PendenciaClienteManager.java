package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.PendenciaCliente;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaClienteManager {

    @Nullable
    PendenciaCliente obterPorId(String id);

    Observable<List<PendenciaCliente>> obterTodos();

    Observable<Integer> updateMotivo(PendenciaCliente pendenciaCliente);

    Single<List<PendenciaCliente>> serverGetAll(String vendedorId, String carga);

    Single<PendenciaCliente> save(PendenciaCliente pendenciaCliente);
}
