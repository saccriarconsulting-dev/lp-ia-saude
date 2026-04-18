package com.axys.redeflexmobile.shared.manager;

import androidx.annotation.Nullable;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Lucas Marciano on 28/02/2020
 */
public interface PendenciaManager{

    @Nullable
    Pendencia obterPorId(String id);

    Observable<List<Pendencia>> obterTodos();

    Single<List<Pendencia>> serverGetAll(String vendedorId, String carga);

    Single<Pendencia> save(Pendencia pendencia);

    Observable<List<Cliente>> obterTodosComCliente(List<Cliente> pendenciaClientes);
}
