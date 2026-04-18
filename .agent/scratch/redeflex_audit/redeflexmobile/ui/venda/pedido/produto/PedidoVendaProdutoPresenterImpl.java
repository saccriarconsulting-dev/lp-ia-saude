package com.axys.redeflexmobile.ui.venda.pedido.produto;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.dao.PrecoDao;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VendaManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class PedidoVendaProdutoPresenterImpl extends BasePresenterImpl<PedidoVendaProdutoView>
        implements PedidoVendaProdutoPresenter {

    private final VendaManager vendaManager;
    private Produto produtoSelecionado;

    PedidoVendaProdutoPresenterImpl(PedidoVendaProdutoView view,
                                    SchedulerProvider schedulerProvider,
                                    ExceptionUtils exceptionUtils,
                                    VendaManager vendaManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.vendaManager = vendaManager;
    }

    @Override
    public void carregarProdutos() {
        Disposable disposable = vendaManager.obterProdutosVenda()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::carregarProdutosSucesso, Timber::e); 

        addDisposable(disposable);
    }

    @Override
    public void validarProduto(String quantidade, Double precoVenda, String idCliente) {
        if (produtoSelecionado == null) {
            String mensagem = getView()
                    .getStringByResId(R.string.pedido_venda_produto_alerta_erro_produto);
            getView().mostrarMensagem(mensagem);
            return;
        }

        int qtd = 0;
        try {
            qtd = Integer.parseInt(quantidade);
        } catch (NumberFormatException e) {
            Timber.e(e);
        }

        if (qtd <= 0) {
            String mensagem = getView()
                    .getStringByResId(R.string.pedido_venda_produto_alerta_erro_quantidade);
            getView().mostrarMensagem(mensagem);
            return;
        }

        boolean produtoCombo = vendaManager.produtoCombo(produtoSelecionado.getId());
        if (qtd > produtoSelecionado.getEstoqueAtual() && !produtoCombo) {
            String mensagem = getView()
                    .getStringByResId(R.string.pedido_venda_produto_alerta_erro_estoque);
            getView().mostrarMensagem(mensagem);
            return;
        }

        produtoSelecionado.setQtde(qtd);

        Preco preco = retornaPrecoDiferenciado(produtoSelecionado, idCliente);
        if (preco != null)
        {
            PrecoDiferenciado precoDif = obterPrecoDiferenciado(preco.getIdPreco());
            if (precoDif != null)
                produtoSelecionado.setPrecovenda(precoDif.getValor());
        }
        else {
            if (produtoSelecionado.getPrecovendaminimo() > 0.0) {
                if (precoVenda < produtoSelecionado.getPrecovendaminimo()) {
                    // Defina o formato para moeda brasileira
                    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    String valorFormatado = formatoMoeda.format(produtoSelecionado.getPrecovendaminimo());

                    String mensagem = "Preço mínimo de venda para este\nproduto -> " + valorFormatado;
                    getView().mostrarMensagem(mensagem);
                    return;
                } else
                    produtoSelecionado.setPrecovenda(precoVenda);
            }
        }

        safeExecute(view -> view.enviarProduto(produtoSelecionado));
    }

    @Override
    public void recuperarProduto(Produto produto) {
        produtoSelecionado = produto;
    }

    private void carregarProdutosSucesso(List<Produto> produtos) {
        getView().popularListaPesquisa(produtos);
    }

    @Override
    public Preco retornaPrecoDiferenciado(Produto produto, String idCliente) {
        List<PrecoDiferenciado> listaPreco = new DBPreco(getContext()).getPrecoDiferenciadoCliente(produto.getId(), idCliente);

        // FIXME: Tentando remover precos ja utilizados
        List<PrecoDiferenciado> itens = Stream.of(listaPreco)
                .filter(value -> {
                    String idPreco = String.valueOf(value.getId());
                    int quantidadeVendida = new DBVenda(getContext()).retornaQtdPrecoDiferenciado(idPreco);
                    quantidadeVendida = quantidadeVendida + produto.getQtde();
                    return quantidadeVendida <= value.getQtdPreco();
                })
                .toList();

        if (!itens.isEmpty()) {
            Preco preco = new Preco();
            preco.setIdPreco(String.valueOf(itens.get(0).getId()));
            preco.setValor(itens.get(0).getValor());
            return preco;
        }

        listaPreco = new DBPreco(getContext()).getPrecoDiferenciado(produto.getId());
        if (!listaPreco.isEmpty()) {
            Preco preco = new Preco();
            preco.setIdPreco(String.valueOf(listaPreco.get(0).getId()));
            preco.setValor(listaPreco.get(0).getValor());
            return preco;
        }

        return null;
    }

    @Override
    public PrecoDiferenciado obterPrecoDiferenciado(String codigoPreco) {
        PrecoDiferenciado precoDiferenciado = new DBPreco(getContext()).getPrecoById(codigoPreco);
        if (precoDiferenciado == null) {
            return null;
        }

        int quantidade = new DBVenda(getContext()).retornaQtdPrecoDiferenciado(codigoPreco);
        if (quantidade >= precoDiferenciado.getQtdPreco()) {
            return null;
        }

        return precoDiferenciado;
    }
}
