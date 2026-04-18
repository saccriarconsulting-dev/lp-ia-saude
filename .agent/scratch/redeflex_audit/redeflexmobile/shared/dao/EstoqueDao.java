package com.axys.redeflexmobile.shared.dao;

import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public interface EstoqueDao {

    boolean isProdutoCombo(String pItemCode);

    ArrayList<EstruturaProd> getEstruturaByItemPai(String pItemCode);

    Produto getProdutoById(String pId);

    ArrayList<Produto> getProdutos();

    ArrayList<Produto> getProdutosComboENaoPistolados();

    void atualizaEstoque(String pIdProduto, boolean pRemove, int pQtd);

    boolean verificaEstoque(String pIdProduto, int pQtd, int quantidadeAtual);

    ArrayList<AuditagemCliente> getAuditagensCliente(String pIdCliente);

    void atualizarImportado(String id);

    void addAuditagemCliente(AuditagemCliente pAuditagemCliente);

    void deletaAuditagemClienteNaoConfirmada(String pIdCliente);

    void confirmaAuditagemCliente(String pIdCliente);

    void confirmaAuditagem(AuditagemEstoque auditagemEstoque);

    void deletaTodaAuditagemCliente(String pIdCliente);

    ArrayList<Produto> getProdutosComEstoque();

    void deletarPistolagemByComboId(int idCombo);

    EstruturaProd obterEstruturaPeloPaiEFilho(String itemPai, String itemFilho);

    void confirmarPistolagens(Venda venda);

    void confirmarPistolagens(Consignado consignado);

    List<CodBarra> getPistolagemNaoFinalizada(String idProduto);

    // Pistolagens de Venda
    ItemVendaCombo getPistolagem(Venda venda, String idProduto, String idPreco);

    // Pistolagens de Consignado
    ItemVendaCombo getPistolagem(Consignado consignado, String idProduto, String idPreco, int pStatus);

    List<ItemVendaCombo> getPistolagensComboNaoFinalizada(Venda venda);

    List<ItemVendaCombo> getPistolagensComboNaoFinalizada(Consignado pConsignado);

    void deletarPistolagensComboNaoFinalizadas(Venda pVenda);

    void deletarPistolagensComboNaoFinalizadas(Consignado pConsignado);
    void deletarPistolagensConsignado(Consignado pConsignado);

    void incluirComboPistolagem(ItemVendaCombo itemVendaCombo, Venda venda);
    void incluirComboPistolagem(ItemVendaCombo itemVendaCombo, Consignado consignado);

    Single<List<EstruturaProd>> getItemsCombo(String codigoProd);

    ArrayList<Produto> getProdutosPorSugestaoVenda(String clienteId);

    ArrayList<Produto> getProdutosPorConsignacao(String clienteId);

    void confirmarPistolagensConsignadoAuditagem(Consignado consignado, Produto produto);

    void deletarPistolagemByIdConsignado(int IdConsignado);

    ArrayList<ItemVendaCombo> getPistolagemItem(Consignado consignado, String idProduto);

    List<ItemVendaCombo> getPistolagensConsignadoItens(Consignado pConsignado);
}
