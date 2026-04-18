package com.axys.redeflexmobile.shared.repository;


import com.axys.redeflexmobile.shared.models.AtualizaCliente;

public interface AtualizarClienteRepository {

    void salvar(AtualizaCliente atualizaCliente);

    void obterPorId(String id, AsyncRepository<AtualizaCliente> async);
}
