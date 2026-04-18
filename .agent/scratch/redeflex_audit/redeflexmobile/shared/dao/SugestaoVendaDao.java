package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.SugestaoVenda;

import java.util.ArrayList;

public interface SugestaoVendaDao {

    void salvar(SugestaoVenda sugestaoVenda);

    SugestaoVenda obterPorCliente(String idCliente, String idOperador, String grupo);

    SugestaoVenda obterPorId(String id);
}
