package com.axys.redeflexmobile.shared.dao;

import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.models.Cliente;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface ClienteDao {

    @Nullable
    Cliente obterPorId(String id);

    void confirmaAuditagem(String pIdCliente);

    void confirmaAtualizacaoCadastral(String idCliente);

    void confirmaAtualizacaoBinario(String idCliente);

    Single<Cliente> obterClientePorId(int id);

    Observable<List<Cliente>> obterTodos();

    Boolean clientePossuiPendencias(String idCliente);

    Observable<List<Cliente>> obterClientesComPendencia();

    Observable<List<Cliente>> obterClientesMigracao();

    Observable<List<Cliente>> obterCadastroMigracaoSub();

    Observable<List<Cliente>> obterClientesComPendenciaNaoRespondido();

    Observable<List<Cliente>> obterClienteComPendenciaNaoRespondidoPorClienteId(String idCliente);

    Single<Boolean> updateNegotiationMigrateSub(int clintId);
    Observable<List<Cliente>> obterClientesPendentesMigracao();
}
