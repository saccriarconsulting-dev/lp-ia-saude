package com.axys.redeflexmobile.shared.repository;

import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface PedidoVendaRepository {

    Single<Cliente> obterCliente(String id);

    Single<List<ItemVendaCombo>> obterItensComboVenda(int vendaId);

    Single<Venda> obterVenda(int id);

    Single<List<ItemVenda>> obterItensVenda(int vendaId);

    Single<List<Produto>> obterProdutosVenda();

    Single<Preco> obterPrecoProduto(Produto produto, String idCliente);

    Single<PrecoDiferenciado> obterPrecoDiferenciado(String codigoPreco);

    Single<Boolean> removerProduto(String idProduto, int vendaItemId, int quantidade);

    Single<ItemVendaCombo> adicionarProdutoVenda(final PrecoDiferenciado precoDiferenciado,
                                                 Produto produto, Venda venda,
                                                 List<CodBarra> itensCod, boolean atualizarEstoque,
                                                 List<ComboVenda> itensCombo);

    boolean temEstoque(String produtoId, int quantidade, int quantidadeAtual);

    Single<Visita> obterVisita(int id);

    Single<List<AuditagemCliente>> obterAuditagem(String clienteId);

    Completable importarProdutos(List<AuditagemCliente> auditagens, Venda venda);

    Completable removerCodigoBarra(CodBarra codBarra);

    Completable removerVendaPelaVisita(int id);

    boolean produtoCombo(String idProduto);

    int retornaQtdPrecoDiferenciado(String idPreco);

    boolean isProdutoCombo(String codigoProd);

    Single<List<EstruturaProd>> getItemsCombo(String codigoProd);
}
