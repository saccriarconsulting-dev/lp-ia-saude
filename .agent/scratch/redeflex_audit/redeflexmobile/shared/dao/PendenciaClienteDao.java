package com.axys.redeflexmobile.shared.dao;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.PendenciaCliente;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaClienteDao {

    @Nullable
    PendenciaCliente obterPorId(String id);

    Observable<List<PendenciaCliente>> obterTodos();

    Observable<Integer> updateMotivo(PendenciaCliente pendenciaCliente);
}
