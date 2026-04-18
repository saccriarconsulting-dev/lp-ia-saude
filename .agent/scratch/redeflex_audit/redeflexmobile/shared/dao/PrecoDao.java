package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;

import java.util.ArrayList;

import javax.annotation.Nullable;

public interface PrecoDao {

    ArrayList<PrecoDiferenciado> getPrecoDiferenciadoCliente(String pIdProduto, String pIdCliente);

    ArrayList<PrecoDiferenciado> getPrecoDiferenciado(String pIdProduto, String pIdCliente);

    ArrayList<PrecoDiferenciado> getPrecoDiferenciado(String pIdProduto);

    @Nullable PrecoDiferenciado getPrecoById(String pId);

    void removeIdVenda(String pIdVendaItem);

    void atualizaIdVenda(String pIdPreco, String pIdVenda, String pIdVendaItem, int pQuantidade);
}
