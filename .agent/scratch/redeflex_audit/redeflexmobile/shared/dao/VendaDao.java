package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;

import java.util.List;

public interface VendaDao {

    List<ItemVendaCombo> getItensComboVendaByIdVenda(int idVenda);

    List<CodBarra> getCodBarraItens(String idVendaItem, int grupoProduto);

    Venda getVendaByIdVisita(int idVisita);

    long novaVenda(int idVisita, String idCliente);

    Venda getVendaById(int pId);

    List<ItemVenda> getItensVendaByIdVenda(int idVenda);

    ItemVenda getItemVendaById(int idVendaItem);

    void removeEstoqueComboByIdVendaItem(int idVendaItem);

    void deleteItemByIdItem(int idItem);

    long addItemVenda(Venda venda, Produto produto, List<CodBarra> listaCodigoBarra, double valor,
                      List<ComboVenda> listaCombos, String idPrecoDiferenciado);

    long addItemVenda(Venda venda, Produto produto, List<CodBarra> listaCodigoBarra,
                      double valor, List<ComboVenda> listaCombos,
                      String idPrecoDiferenciado, boolean isCombo, boolean unificar);

    void atualizarQuantidadeCombo(int quantidade, String idVendaItem);

    long addItemVenda(Venda venda, Produto produto, List<CodBarra> listaCodigoBarra, double valor,
                      List<ComboVenda> listaCombos, String idPrecoDiferenciado, long idItemVenda,
                      boolean atualizaQuantidade);

    int retornaQtdPrecoDiferenciado(String idPreco);

    void removerCodigoBarra(CodBarra codBarra);

    void deleteVendaByIdVisita(int idVisita);

    double retornaValorTotalVenda(int idVenda);

    boolean iccidVendido(CodBarra pCodBarra);
}
