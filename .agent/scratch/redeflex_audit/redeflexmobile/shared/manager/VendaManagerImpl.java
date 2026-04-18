package com.axys.redeflexmobile.shared.manager;

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
import com.axys.redeflexmobile.shared.repository.PedidoVendaRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class VendaManagerImpl implements VendaManager {

    private final PedidoVendaRepository pedidoVendaRepository;

    public VendaManagerImpl(PedidoVendaRepository pedidoVendaRepository) {
        this.pedidoVendaRepository = pedidoVendaRepository;
    }

    @Override
    public Single<Cliente> obterCliente(String id) {
        return pedidoVendaRepository.obterCliente(id);
    }

    @Override
    public Single<List<ItemVendaCombo>> obterItensComboVenda(int vendaId) {
        return pedidoVendaRepository.obterItensComboVenda(vendaId);
    }

    @Override
    public Single<Venda> obterVenda(int id) {
        return pedidoVendaRepository.obterVenda(id);
    }

    @Override
    public Single<List<ItemVenda>> obterItensVenda(int vendaId) {
        return pedidoVendaRepository.obterItensVenda(vendaId);
    }

    @Override
    public Single<List<Produto>> obterProdutosVenda() {
        return pedidoVendaRepository.obterProdutosVenda();
    }

    @Override
    public Single<Preco> obterPrecoProduto(Produto produto, String idCliente) {
        return pedidoVendaRepository.obterPrecoProduto(produto, idCliente);
    }

    @Override
    public Single<PrecoDiferenciado> obterPrecoDiferenciado(String codigoPreco) {
        return pedidoVendaRepository.obterPrecoDiferenciado(codigoPreco);
    }

    @Override
    public Single<Boolean> removerProduto(String idProduto, int vendaItemId, int quantidade) {
        return pedidoVendaRepository.removerProduto(idProduto, vendaItemId, quantidade);
    }

    @Override
    public Single<ItemVendaCombo> adicionarProdutoVenda(PrecoDiferenciado precoDiferenciado,
                                                        Produto produto, Venda venda,
                                                        List<CodBarra> itensCod,
                                                        boolean atualizarEstoque,
                                                        List<ComboVenda> itensCombo) {
        return pedidoVendaRepository.adicionarProdutoVenda(
                precoDiferenciado,
                produto,
                venda,
                itensCod,
                atualizarEstoque,
                itensCombo
        );
    }

    @Override
    public boolean temSaldo(String produtoId, int quantidade, int quantidadeAtual) {
        return pedidoVendaRepository.temEstoque(produtoId, quantidade, quantidadeAtual);
    }

    @Override
    public Single<Visita> obterVisita(int id) {
        return pedidoVendaRepository.obterVisita(id);
    }

    @Override
    public Single<List<AuditagemCliente>> obterAuditagem(String clienteId) {
        return pedidoVendaRepository.obterAuditagem(clienteId);
    }

    @Override
    public Completable importarProdutos(List<AuditagemCliente> auditagens, Venda venda) {
        return pedidoVendaRepository.importarProdutos(auditagens, venda);
    }

    @Override
    public Completable removerCodigoBarra(CodBarra codBarra) {
        return pedidoVendaRepository.removerCodigoBarra(codBarra);
    }

    @Override
    public Completable removerVendaPelaVisita(int id) {
        return pedidoVendaRepository.removerVendaPelaVisita(id);
    }

    @Override
    public boolean produtoCombo(String produtoId) {
        return pedidoVendaRepository.produtoCombo(produtoId);
    }

    @Override
    public int retornaQtdPrecoDiferenciado(String idPreco) {
        return pedidoVendaRepository.retornaQtdPrecoDiferenciado(idPreco);
    }

    @Override
    public boolean isProdutoCombo(String codigoProd) {
        return pedidoVendaRepository.isProdutoCombo(codigoProd);
    }

    @Override
    public Single<List<EstruturaProd>> getItemsCombo(String id) {
        return pedidoVendaRepository.getItemsCombo(id);
    }
}
