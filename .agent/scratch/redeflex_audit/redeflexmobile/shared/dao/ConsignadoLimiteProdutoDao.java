package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;

import java.util.List;

public interface ConsignadoLimiteProdutoDao {
    ConsignadoLimiteProduto obterPorClienteProduto(String idCliente, String idProduto);
}
