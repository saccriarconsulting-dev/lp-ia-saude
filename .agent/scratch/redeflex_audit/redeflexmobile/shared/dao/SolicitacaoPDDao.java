package com.axys.redeflexmobile.shared.dao;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;

import java.util.List;

import io.reactivex.Observable;

public interface SolicitacaoPDDao {

    @Nullable
//    SolicitacaoPrecoDiferenciado obterPorId(String id);

    Observable<List<SolicitacaoPrecoDiferenciado>> obterTodos();

//    Observable<List<Cliente>> obterTodosComCliente(List<Cliente> clientes);
}
