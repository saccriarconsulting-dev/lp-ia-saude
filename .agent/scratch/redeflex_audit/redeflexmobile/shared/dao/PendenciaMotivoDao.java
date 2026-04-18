package com.axys.redeflexmobile.shared.dao;

import io.reactivex.Observable;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;

import java.util.List;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaMotivoDao {

    @Nullable
    PendenciaMotivo obterPorId(String id);

    Observable<List<PendenciaMotivo>> obterTodos();

    Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes);
}
