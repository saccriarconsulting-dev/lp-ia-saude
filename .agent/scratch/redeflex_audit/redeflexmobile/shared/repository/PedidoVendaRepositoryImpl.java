package com.axys.redeflexmobile.shared.repository;

import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.dao.ClienteDao;
import com.axys.redeflexmobile.shared.dao.ConsignadoLimiteProdutoDao;
import com.axys.redeflexmobile.shared.dao.EstoqueDao;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.dao.SugestaoVendaDao;
import com.axys.redeflexmobile.shared.dao.VendaDao;
import com.axys.redeflexmobile.shared.dao.VisitaDao;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.ConsignadoLimiteProduto;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SugestaoVenda;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class PedidoVendaRepositoryImpl implements PedidoVendaRepository {

    private static final String ITENS_NULL = "itens == null";

    private final ClienteDao clienteDao;
    private final VisitaDao visitaDao;
    private final VendaDao vendaDao;
    private final EstoqueDao estoqueDao;
    private final PrecoDao precoDao;
    private final SugestaoVendaDao sugestaoVendaDao;

    public PedidoVendaRepositoryImpl(ClienteDao clienteDa, VisitaDao visitaDao, VendaDao vendaDao,
                                     EstoqueDao estoqueDao, PrecoDao precoDao,
                                     SugestaoVendaDao sugestaoVendaDao) {
        this.clienteDao = clienteDa;
        this.visitaDao = visitaDao;
        this.vendaDao = vendaDao;
        this.estoqueDao = estoqueDao;
        this.precoDao = precoDao;
        this.sugestaoVendaDao = sugestaoVendaDao;
    }

    @Override
    public Single<Cliente> obterCliente(String id) {
        return Single.create(emitter -> {
            Cliente cliente = clienteDao.obterPorId(id);
            if (cliente == null) {
                emitter.onError(new NullPointerException("cliente == null"));
                return;
            }

            emitter.onSuccess(cliente);
        });
    }

    @Override
    public Single<List<ItemVendaCombo>> obterItensComboVenda(int vendaId) {
        return Single.create(emitter -> {
            List<ItemVendaCombo> itens = vendaDao.getItensComboVendaByIdVenda(vendaId);
            /*if (itens.isEmpty()) {
                emitter.onError(new NullPointerException(ITENS_NULL));
                return;
            }*/

            for (ItemVendaCombo itemVendaCombo : itens) {
                boolean isCombo = estoqueDao.isProdutoCombo(itemVendaCombo.getIdProduto());
                List<EstruturaProd> prod = estoqueDao.getEstruturaByItemPai(
                        itemVendaCombo.getIdProduto()
                );
                if (!isCombo && prod.isEmpty()) {
                    continue;
                }

                itemVendaCombo.setCombo(true);
                for (EstruturaProd estruturaProd : prod) {
                    CodBarra temp = Stream.of(itemVendaCombo.getCodigosList())
                            .filter(value -> estruturaProd.getItemFilho().equalsIgnoreCase(value.getIdProduto()))
                            .findFirst()
                            .orElse(null);
                    if (temp != null) {
                        continue;
                    }
                    Produto produto = estoqueDao.getProdutoById(estruturaProd.getItemFilho());
                    if(produto == null){
                        emitter.onError(new Exception("Não existe [Produto] para [EstruturaProd].[itemFilho] = '"
                                + estruturaProd.getItemFilho()
                                +". [EstruturaProd].[id] = '"
                                + estruturaProd.getId()
                                +"'. Item Pai = " + itemVendaCombo.getIdProduto() + "\nPor favor entre em contato com o suporte."));
                        return;
                    }
                    CodBarra codBarra = new CodBarra();
                    codBarra.setNomeProduto(produto.getNome());
                    codBarra.setIdProduto(produto.getId());
                    codBarra.setGrupoProduto(produto.getGrupo());
                    codBarra.setQuantidadeTemporaria(estruturaProd.getQtd() * itemVendaCombo.getQtde());
                    codBarra.setIndividual(true);

                    itemVendaCombo.getCodigosList().add(codBarra);
                }
            }

            emitter.onSuccess(itens);
        });
    }

    @Override
    public Single<Venda> obterVenda(int id) {
        return Single.create(emitter -> {
            Venda venda = vendaDao.getVendaByIdVisita(id);
            if (venda == null) {
                Visita visita = visitaDao.obterVisitaPorId(id);
                long codigo = vendaDao.novaVenda(visita.getId(), visita.getIdCliente());
                venda = vendaDao.getVendaById((int) codigo);
            }

            emitter.onSuccess(venda);
        });
    }

    @Override
    public Single<List<ItemVenda>> obterItensVenda(int vendaId) {
        return Single.create(emitter -> {
            List<ItemVenda> itens = vendaDao.getItensVendaByIdVenda(vendaId);
            if (itens.isEmpty()) {
                emitter.onError(new NullPointerException(ITENS_NULL));
                return;
            }

            emitter.onSuccess(itens);
        });
    }


    @Override
    public Single<List<Produto>> obterProdutosVenda() {
        return Single.create(emitter -> {
            List<Produto> itens = estoqueDao.getProdutosComboENaoPistolados();
            if (itens.isEmpty()) {
                emitter.onError(new NullPointerException(ITENS_NULL));
                return;
            }

            emitter.onSuccess(itens);
        });
    }

    @Override
    public Single<Preco> obterPrecoProduto(Produto produto, String idCliente) {
        return Single.create(emitter -> {
            List<PrecoDiferenciado> listaPreco = precoDao.getPrecoDiferenciadoCliente(
                    produto.getId(),
                    idCliente
            );

            // FIXME: Tentando remover precos ja utilizados
            List<PrecoDiferenciado> itens = Stream.of(listaPreco)
                    .filter(value -> {
                        String idPreco = String.valueOf(value.getId());
                        int quantidadeVendida = vendaDao.retornaQtdPrecoDiferenciado(idPreco);
                        quantidadeVendida = quantidadeVendida + produto.getQtde();
                        return quantidadeVendida <= value.getQtdPreco();
                    })
                    .toList();
            if (!itens.isEmpty()) {
                Preco preco = new Preco();
                preco.setIdPreco(String.valueOf(itens.get(0).getId()));
                preco.setValor(itens.get(0).getValor());

                emitter.onSuccess(preco);

                return;
            }

            listaPreco = precoDao.getPrecoDiferenciado(produto.getId());
            if (!listaPreco.isEmpty()) {
                Preco preco = new Preco();
                preco.setIdPreco(String.valueOf(listaPreco.get(0).getId()));
                preco.setValor(listaPreco.get(0).getValor());

                emitter.onSuccess(preco);

                return;
            }

            Preco preco = new Preco();
            preco.setIdPreco("");
            preco.setValor(produto.getPrecovenda());

            emitter.onSuccess(preco);
        });
    }

    @Override
    public Single<PrecoDiferenciado> obterPrecoDiferenciado(String codigoPreco) {
        return Single.create(emitter -> {
            PrecoDiferenciado precoDiferenciado = precoDao.getPrecoById(codigoPreco);
            if (precoDiferenciado == null) {
                emitter.onError(new IllegalStateException(ITENS_NULL));
                return;
            }

            int quantidade = vendaDao.retornaQtdPrecoDiferenciado(codigoPreco);
            if (quantidade >= precoDiferenciado.getQtdPreco()) {
                emitter.onError(new IllegalStateException(ITENS_NULL));
                return;
            }
            emitter.onSuccess(precoDiferenciado);
        });
    }

    @Override
    public Single<Boolean> removerProduto(String idProduto, int vendaItemId, int quantidade) {
        return Single.create(emitter -> {
            ItemVenda itemVenda = vendaDao.getItemVendaById(vendaItemId);
            if (itemVenda.isCombo()) {
                vendaDao.removeEstoqueComboByIdVendaItem(itemVenda.getId());
            } else {
                estoqueDao.atualizaEstoque(idProduto, false, quantidade);
            }

            precoDao.removeIdVenda(String.valueOf(vendaItemId));
            vendaDao.deleteItemByIdItem(vendaItemId);

            emitter.onSuccess(true);
        });
    }

    @Override
    public Single<ItemVendaCombo> adicionarProdutoVenda(final PrecoDiferenciado precoDiferenciado,
                                                        Produto produto, Venda venda,
                                                        List<CodBarra> itensCod, boolean atualizarEstoque,
                                                        List<ComboVenda> itensCombo) {
        return Single.create(emitter -> {
            PrecoDiferenciado tempPreco = precoDiferenciado;
            double valor = produto.getPrecovenda();

            boolean comboTemp = estoqueDao.isProdutoCombo(produto.getId());
            long idVendaItem;
            boolean deveAtualizar = atualizarEstoque;
            if (comboTemp) {
                deveAtualizar = true;
            }

            int quantidadePreco = tempPreco != null
                    ? vendaDao.retornaQtdPrecoDiferenciado(String.valueOf(tempPreco.getId()))
                    : 0;
            quantidadePreco += produto.getQtde();
            if ((tempPreco != null) && ((tempPreco.getQtdPreco() > 0) && (quantidadePreco > tempPreco.getQtdPreco()))) {
                tempPreco = null;
                valor = estoqueDao.getProdutoById(produto.getId()).getPrecovenda();
            }

            if (tempPreco == null) {
                idVendaItem = vendaDao.addItemVenda(
                        venda,
                        produto,
                        itensCod,
                        valor,
                        itensCombo,
                        null,
                        comboTemp,
                        false
                );
                atualizaEstoque(produto.getId(), deveAtualizar, produto.getQtde());

                ItemVenda itemVenda = vendaDao.getItemVendaById((int) idVendaItem);

                if (montarCombo(emitter, itemVenda)) return;

                emitter.onSuccess((ItemVendaCombo) itemVenda);

                return;
            }

            idVendaItem = vendaDao.addItemVenda(
                    venda,
                    produto,
                    itensCod,
                    valor,
                    itensCombo,
                    String.valueOf(tempPreco.getId()),
                    comboTemp,
                    false
            );
            precoDao.atualizaIdVenda(
                    String.valueOf(tempPreco.getId()),
                    String.valueOf(venda.getId()),
                    String.valueOf(idVendaItem),
                    produto.getQtde()
            );

            ItemVenda item = vendaDao.getItemVendaById((int) idVendaItem);
            item.setSituacaosugestaovenda(produto.getSituacaoSugestaoVenda());

            atualizaEstoque(produto.getId(), deveAtualizar, produto.getQtde());

            if (montarCombo(emitter, item)) return;

            emitter.onSuccess((ItemVendaCombo) item);
        });
    }

    private boolean montarCombo(SingleEmitter<ItemVendaCombo> emitter, ItemVenda item) {
        boolean isCombo = estoqueDao.isProdutoCombo(item.getIdProduto());
        item.setCombo(isCombo);
        if (!isCombo) {
            emitter.onSuccess((ItemVendaCombo) item);

            return true;
        }

        List<EstruturaProd> prod = estoqueDao
                .getEstruturaByItemPai(item.getIdProduto());
        if (prod.isEmpty()) {
            emitter.onSuccess((ItemVendaCombo) item);

            return true;
        }

        item.setCombo(true);
        for (EstruturaProd estruturaProd : prod) {
            CodBarra temp = Stream.of(item.getCodigosList())
                    .filter(value -> estruturaProd.getItemFilho().equalsIgnoreCase(value.getIdProduto()))
                    .findFirst()
                    .orElse(null);
            if (temp != null) {
                temp.setQuantidadeTemporaria(estruturaProd.getQtd() * item.getQtde());
                temp.setQuantidadeFormacaoCombo(estruturaProd.getQtd());
                continue;
            }
            Produto tempProduto = estoqueDao.getProdutoById(estruturaProd.getItemFilho());
            CodBarra codBarra = new CodBarra();
            codBarra.setNomeProduto(tempProduto.getNome());
            codBarra.setIdProduto(tempProduto.getId());
            codBarra.setGrupoProduto(tempProduto.getGrupo());
            codBarra.setQuantidadeTemporaria(estruturaProd.getQtd() * item.getQtde());
            codBarra.setQuantidadeFormacaoCombo(estruturaProd.getQtd());
            codBarra.setIndividual(true);

            item.getCodigosList().add(codBarra);
        }
        return false;
    }

    @Override
    public boolean temEstoque(String produtoId, int quantidade, int quantidadeAtual) {
        return estoqueDao.verificaEstoque(produtoId, quantidade, quantidadeAtual);
    }

    @Override
    public Single<Visita> obterVisita(int id) {
        return Single.create(emitter -> {
            Visita visita = visitaDao.obterVisitaPorId(id);
            if (visita == null) {
                emitter.onError(new NullPointerException("visita == null"));
                return;
            }

            emitter.onSuccess(visita);
        });
    }

    @Override
    public Single<List<AuditagemCliente>> obterAuditagem(String clienteId) {
        return Single.create(emitter -> {
            List<AuditagemCliente> itens = Stream.ofNullable(estoqueDao.getAuditagensCliente(clienteId))
                    .filter(value -> !value.isImportado())
                    .toList();
            /*if (itens.isEmpty()) {
                emitter.onError(new NullPointerException("auditagem == null"));
                return;
            }*/

            emitter.onSuccess(itens);
        });
    }

    private AuditagemCliente findAuditagemByIdProduto(String idProduto, List<AuditagemCliente> customers) {
        for (AuditagemCliente customer : customers) {
            if (customer.getIdProduto().equals(idProduto)) {
                return customer;
            }
        }
        return null;
    }

    private String obterIdPreco(Produto produto,String idCliente){
        List<PrecoDiferenciado> precos = precoDao.getPrecoDiferenciadoCliente(produto.getId(),idCliente);
        Preco preco = new Preco();
        preco.setIdPreco("");
        preco.setValor(produto.getPrecovenda());

        if (!precos.isEmpty()) {
            preco.setIdPreco(String.valueOf(precos.get(0).getId()));
            preco.setValor(precos.get(0).getValor());
        }

        precos = precoDao.getPrecoDiferenciado(produto.getId());
        if (!precos.isEmpty()) {
            preco.setIdPreco(String.valueOf(precos.get(0).getId()));
            preco.setValor(precos.get(0).getValor());
        }
        return preco.getIdPreco();
    }

    private PrecoDiferenciado obterPrecoDiferenciado(Produto produto, Venda venda, String idCliente){
        String idPreco = obterIdPreco(produto, idCliente);
        PrecoDiferenciado tempPreco = precoDao.getPrecoById(idPreco);
        int quantidadePreco = tempPreco != null
                ? vendaDao.retornaQtdPrecoDiferenciado(String.valueOf(tempPreco.getId()))
                : 0;

        quantidadePreco += produto.getQtde();

        if ((tempPreco != null) && ((tempPreco.getQtdPreco() > 0) && (quantidadePreco > tempPreco.getQtdPreco()))) {
            return null;
        }

        return tempPreco;
    }

    private void criarSugestaoVenda(Produto produto,PrecoDiferenciado tempPreco,Venda venda, AuditagemCliente auditagem,boolean isCombo){
        if(tempPreco == null){
            double valor = produto.getPrecovenda();
            produto.setPrecovenda(valor);
            vendaDao.addItemVenda(venda, produto, new ArrayList<>(), valor, new ArrayList<>(), null, isCombo, false);
        } else {
            double valor = tempPreco.getValor();
            produto.setPrecovenda(valor);
            long idVendaItem =
                    vendaDao.addItemVenda(venda, produto, new ArrayList<>(), valor, new ArrayList<>(), String.valueOf(tempPreco.getId()), isCombo, false);

            precoDao.atualizaIdVenda(
                    String.valueOf(tempPreco.getId()),
                    String.valueOf(venda.getId()),
                    String.valueOf(idVendaItem),
                    produto.getQtde()
            );
        }

        if(auditagem != null) {
            estoqueDao.atualizarImportado(String.valueOf(auditagem.getId()));
        }
        atualizaEstoque(produto.getId(), true, produto.getQtde());
    }

    @Override
    public Completable importarProdutos(List<AuditagemCliente> auditagens, Venda venda) {
        return Completable.create(emitter -> {
            ArrayList<Produto> produtos = estoqueDao.getProdutosPorSugestaoVenda(venda.getIdCliente());
            for(Produto produto:produtos){
                AuditagemCliente auditagem = findAuditagemByIdProduto(produto.getId(), auditagens);
                SugestaoVenda sugestaoVenda = sugestaoVendaDao.obterPorCliente(
                        venda.getIdCliente(),
                        String.valueOf(produto.getOperadora()),
                        String.valueOf(produto.getGrupo())
                );
                if(!deveIgnorarProduto(produto, sugestaoVenda, auditagem)) {
                    calcularQuantidade(produto, sugestaoVenda, auditagem);
                    boolean isCombo = estoqueDao.isProdutoCombo(produto.getId());
                    PrecoDiferenciado tempPreco = obterPrecoDiferenciado(produto,venda,venda.getIdCliente());
                    criarSugestaoVenda(produto,tempPreco,venda,auditagem,isCombo);
                }
            }
            emitter.onComplete();
        });
    }

    @Override
    public Completable removerCodigoBarra(CodBarra codBarra) {
        return Completable.create(emitter -> {
            vendaDao.removerCodigoBarra(codBarra);
            estoqueDao.atualizaEstoque(
                    codBarra.getIdProduto(),
                    false,
                    Integer.parseInt(codBarra.retornaQuantidade(UsoCodBarra.GERAL))
            );
            emitter.onComplete();
        });
    }

    @Override
    public Completable removerVendaPelaVisita(int id) {
        return Completable.create(emitter -> {
            // TODO: Testar e tentar reproduzir esse cenario.
            Venda venda = vendaDao.getVendaByIdVisita(id);
            if (venda == null) {
                emitter.onComplete();
                return;
            }

            List<ItemVendaCombo> itens = vendaDao.getItensComboVendaByIdVenda(venda.getId());
            for (ItemVendaCombo itemVendaCombo : itens) {
                if (itemVendaCombo.isCombo()) {
                    for (CodBarra codbarra : itemVendaCombo.getCodigosList()) {
                        estoqueDao.atualizaEstoque(
                                codbarra.getIdProduto(),
                                false,
                                Integer.parseInt(codbarra.retornaQuantidade(UsoCodBarra.GERAL))
                        );
                    }

                    estoqueDao.atualizaEstoque(
                            itemVendaCombo.getIdProduto(),
                            false,
                            itemVendaCombo.getQtde()
                    );

                    vendaDao.deleteVendaByIdVisita(id);
                    emitter.onComplete();
                    return;
                }

                int quantidade = itemVendaCombo.getQtde();
                for (CodBarra codbarra : itemVendaCombo.getCodigosList()) {
                    quantidade += Integer.parseInt(codbarra.retornaQuantidade(UsoCodBarra.GERAL));
                }

                itemVendaCombo.setQtde(quantidade);

                estoqueDao.atualizaEstoque(
                        itemVendaCombo.getIdProduto(),
                        false,
                        itemVendaCombo.getQtde()
                );
            }
            vendaDao.deleteVendaByIdVisita(id);
            emitter.onComplete();
        });
    }

    @Override
    public boolean produtoCombo(String idProduto) {
        return estoqueDao.isProdutoCombo(idProduto);
    }

    @Override
    public int retornaQtdPrecoDiferenciado(String idPreco) {
        return vendaDao.retornaQtdPrecoDiferenciado(idPreco);
    }

    @Override
    public boolean isProdutoCombo(String codigoProd) {
        return estoqueDao.isProdutoCombo(codigoProd);
    }

    @Override
    public Single<List<EstruturaProd>> getItemsCombo(String codigoProd) {
        return estoqueDao.getItemsCombo(codigoProd);
    }

    private void atualizaEstoque(String idProduto, boolean atualizarEstoque, int quantidade) {
        if (atualizarEstoque) {
            estoqueDao.atualizaEstoque(idProduto, true, quantidade);
        }
    }

    private void calcularQuantidade(Produto produto, SugestaoVenda sugestaoVenda,AuditagemCliente auditagemCliente) {
        int quantidade = 0;
        if(auditagemCliente == null){
            quantidade = sugestaoVenda.getEstoqueIdeal();
        }else {
            quantidade = sugestaoVenda.getEstoqueIdeal() - auditagemCliente.getQuantidade();
        }
        int estoque = produto.getEstoqueAtual();
        if(estoque >= quantidade ){
            produto.setQtde(quantidade);
        }else{
            produto.setQtde(estoque);
        }
    }

    private void calcularQuantidadeConsignado(Produto produto, ConsignadoLimiteProduto consignadoLimiteProduto, AuditagemCliente auditagemCliente) {
        int quantidade = 0;
        if(auditagemCliente == null){
            quantidade = consignadoLimiteProduto.getQuantidade();
        }else {
            quantidade = consignadoLimiteProduto.getQuantidade() - auditagemCliente.getQuantidade();
        }
        int estoque = produto.getEstoqueAtual();
        if(estoque >= quantidade ){
            produto.setQtde(quantidade);
        }else{
            produto.setQtde(estoque);
        }
    }

    private boolean deveIgnorarProduto(Produto produto, SugestaoVenda sugestaoVenda,
                                       AuditagemCliente auditagemCliente) {
        if (sugestaoVenda == null || produto.getGrupo() != sugestaoVenda.getGrupoProduto() || produto.getOperadora() != sugestaoVenda.getIdOperadora()) {
            return true;
        }

        if(auditagemCliente != null) {
            if (auditagemCliente.getQuantidade() > sugestaoVenda.getEstoqueIdeal()) {
                return true;
            }

            int quantidadeCalculada = sugestaoVenda.getEstoqueIdeal() - auditagemCliente.getQuantidade();
            return quantidadeCalculada <= 0;
        }
        return false;
    }
}
