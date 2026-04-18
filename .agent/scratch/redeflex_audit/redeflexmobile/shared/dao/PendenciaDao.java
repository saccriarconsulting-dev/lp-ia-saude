package com.axys.redeflexmobile.shared.dao;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;

import java.util.List;
import io.reactivex.Observable;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaDao {

    @Nullable
    Pendencia obterPorId(String id);

    Observable<List<Pendencia>> obterTodos();

    Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes);
}
