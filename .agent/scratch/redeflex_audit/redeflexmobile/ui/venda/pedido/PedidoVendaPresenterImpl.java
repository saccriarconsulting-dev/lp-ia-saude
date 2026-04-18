package com.axys.redeflexmobile.ui.venda.pedido;

import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBSugestaoVenda;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.manager.VendaManager;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.Visita;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class PedidoVendaPresenterImpl extends BasePresenterImpl<PedidoVendaView>
        implements PedidoVendaPresenter {

    private static final int SEM_VALOR = 0;
    private static final double VALUE = 0.0;
    private int visitaId;
    private final VendaManager vendaManager;
    private Cliente cliente;
    private Venda venda;
    private Visita visita;
    private double total;

    PedidoVendaPresenterImpl(PedidoVendaView view, SchedulerProvider schedulerProvider,
                             ExceptionUtils exceptionUtils, VendaManager vendaManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.vendaManager = vendaManager;
    }

    @Override
    public void informarVisita(int id) {
        this.visitaId = id;

        Disposable disposable = vendaManager.obterVisita(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    visita = value;
                    getView().iniciarTimer();
                }, Timber::e);

        addDisposable(disposable);
    }

    @Override
    public void carregarProdutos() {
        Disposable disposable = vendaManager.obterVenda(visitaId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    venda = value;
                    obterItensComboVenda(venda.getId());
                }, throwable -> {
                    Alerta alerta = new Alerta(getContext(), getContext().getResources().getString(R.string.app_name), "Erro na função obterVenda(visitaId): " + throwable.getLocalizedMessage());
                    alerta.show();
                });
        addDisposable(disposable);

        /*Disposable disposable = vendaManager.obterVenda(visitaId)
                .subscribeOn(schedulerProvider.io())
                .flatMap(value -> {
                    venda = value;
                    return vendaManager.obterAuditagem(value.getIdCliente());
                })
                .flatMap(value -> vendaManager.obterItensComboVenda(venda.getId()))
                .onErrorResumeNext(throwable -> vendaManager.obterItensComboVenda(venda.getId()))
                .observeOn(schedulerProvider.ui())
                .subscribe(itens -> {
                    getView().popularAdapter(itens);
                    calcularTotal(itens);
                }, throwable -> {
                    Timber.e(throwable);
                    getView().atualizarTotal(Util_IO.formatDoubleToDecimalNonDivider(VALUE));
                });

        addDisposable(disposable);*/
    }

    private void obterItensComboVenda(int vendaId) {
        Disposable disposable = vendaManager.obterItensComboVenda(vendaId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    getView().popularAdapter(value);
                    calcularTotal(value);
                }, throwable -> {
                    Alerta alerta = new Alerta(getContext(), getContext().getResources().getString(R.string.app_name), "Erro na função obterItensComboVenda(vendaId): " + throwable.getLocalizedMessage());
                    alerta.show();
                });
        addDisposable(disposable);
    }

    private void importarProdutos(List<AuditagemCliente> auditagems, Venda venda) {

        Disposable disposable = vendaManager.importarProdutos(auditagems, venda)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    obterItensComboVenda(venda.getId());
                }, throwable -> {
                    Alerta alerta = new Alerta(getContext(), getContext().getResources().getString(R.string.app_name), "Erro na função importarProdutos(auditagems,venda): " + throwable.getLocalizedMessage());
                    alerta.show();
                });
        addDisposable(disposable);
    }

    private void obterAuditagem(String idCliente) {
        Disposable disposable = vendaManager.obterAuditagem(idCliente)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    importarProdutos(value, venda);
                }, throwable -> {
                    Alerta alerta = new Alerta(getContext(), getContext().getResources().getString(R.string.app_name), "Erro na função obterAuditagem(idCliente): " + throwable.getLocalizedMessage());
                    alerta.show();
                });
        addDisposable(disposable);
    }

    @Override
    public void importarProdutosSugestaoVenda() {
        Disposable disposable = vendaManager.obterVenda(visitaId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    venda = value;
                    obterAuditagem(venda.getIdCliente());
                }, throwable -> {
                    Alerta alerta = new Alerta(getContext(), getContext().getResources().getString(R.string.app_name), "Erro na função obterVenda(visitaId): " + throwable.getLocalizedMessage());
                    alerta.show();
                });
        addDisposable(disposable);
    }

    @Override
    public void carregarCliente() {
        //Roni
        Disposable disposable = vendaManager.obterVisita(visitaId)
                .subscribeOn(schedulerProvider.io())
                .flatMap(value -> vendaManager.obterCliente(value.getIdCliente()))
                .observeOn(schedulerProvider.ui())
                .subscribe(value -> {
                    this.cliente = value;
                    getView().popularCliente(value);
                }, Timber::e);

        addDisposable(disposable);
    }

    public Cliente retornaCliente() {
        return this.cliente;
    }

    @Override
    public void adicionarProdutoBase(Produto produto) {
        Disposable disposable = vendaManager.obterPrecoProduto(produto, cliente.getId())
                .subscribeOn(schedulerProvider.io())
                .flatMap(preco -> vendaManager.obterPrecoDiferenciado(preco.getIdPreco()))
                .onErrorResumeNext(throwable -> Single.just(new PrecoDiferenciado()))
                .flatMap(precoDiferenciado -> {
                    PrecoDiferenciado temp = precoDiferenciado.getIdCliente() == null
                            ? null
                            : precoDiferenciado;

                    if (temp != null) {
                        produto.setPrecovenda(temp.getValor());
                    }

                    // Carrega Situacao Cliente Sugestao Venda
                    DBSugestaoVenda dbSugestaoVenda = new DBSugestaoVenda(getContext());
                    produto.setSituacaoSugestaoVenda(dbSugestaoVenda.getSituacaoSugestaoRuptura(venda.getId(), produto.getId()));

                    boolean valido = validarIccid(produto, temp);
                    if (!valido) {
                        String mensagem = "Produto esta invalido " +
                                produto.getId() + ": " + produto.getNome();
                        return Single.error(new IllegalStateException(mensagem));
                    }

                    return vendaManager.adicionarProdutoVenda(
                            temp,
                            produto,
                            venda,
                            new ArrayList<>(),
                            true,
                            new ArrayList<>()
                    );
                })
                .observeOn(schedulerProvider.ui())
                .subscribe(item -> {
                    getView().atualizarAdicaoAdapter(item);
                    adicionarTotal(item);
                }, Timber::e);

        addDisposable(disposable);
    }

    @Override
    public void removerProduto(ItemVenda itemVenda) {
        Disposable disposable = vendaManager.removerProduto(
                        itemVenda.getIdProduto(),
                        itemVenda.getId(),
                        itemVenda.getQtde()
                )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(removido -> removerTotal((ItemVendaCombo) itemVenda), Timber::e);

        addDisposable(disposable);
    }

    @Override
    public void lerIccid(String produtoId, String itemVendaId, int quantidade,
                         int quantidadeLido, boolean produtoCombo, String produtoComboId,
                         int quantidadeCombo, int quantidadeComboLido) {
        getView().lerIccid(
                visitaId,
                produtoId,
                itemVendaId,
                quantidade,
                quantidadeLido,
                produtoCombo,
                produtoComboId,
                quantidadeCombo,
                quantidadeComboLido
        );
    }

    @Override
    public Date dataInicioVisita() {
        return visita.getDataInicio();
    }

    @Override
    public void finalizarPedido() {
        if (getView().obterDadosAdapter().isEmpty()) {
            String mensagem = getView().getStringByResId(
                    R.string.pedido_venda_alerta_finalizar_pedido_sem_item
            );
            getView().mostrarMensagem(mensagem);

            return;
        }

        List<ItemVendaCombo> itens = Stream.ofNullable(getView().obterDadosAdapter())
                .filter(value -> value.getBipagem().equalsIgnoreCase(ItemVenda.LER_CODIGO_BARRA))
                .filter(value -> value.getQtdCombo() == 0 && value.getQtde() != value.getQuantidadeSerial() || value.getQtdCombo() > 0 && value.getQuantidadeSerial() != (value.getQtde() * value.getQtdCombo()))
                .toList();

        if (!itens.isEmpty()) {
            String mensagem = getView().getStringByResId(
                    R.string.pedido_venda_alerta_finalizar_pedido
            );
            getView().mostrarMensagem(mensagem);

            return;
        }

        getView().fechar();
    }

    @Override
    public void removerCodigoBarra(CodBarra codBarra, int posicao) {
        Disposable disposable = vendaManager.removerCodigoBarra(codBarra)
                .subscribeOn(schedulerProvider.io())
                .doOnComplete(() -> {
                    List<ItemVendaCombo> itens = getView().obterDadosAdapter();
                    ItemVendaCombo item = itens.get(posicao);
                    int novaQuantidade = item.getQuantidadeSerial() + Integer.parseInt(codBarra.retornaQuantidade(UsoCodBarra.GERAL));
                    item.getCodigosList().remove(codBarra);
                    item.setQuantidadeSerial(novaQuantidade);
                })
                .observeOn(schedulerProvider.ui())
                .subscribe(this::carregarProdutos, Timber::e);

        addDisposable(disposable);
    }

    @Override
    public void cancelarVenda() {
        Disposable disposable = vendaManager.removerVendaPelaVisita(visitaId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> getView().fechar(), Timber::e);

        addDisposable(disposable);
    }

    private boolean validarIccid(Produto produto, PrecoDiferenciado precoDiferenciado) {
        if (produto.getPrecovenda() <= SEM_VALOR && !produto.permiteVendaSemValor()) {
            String msg = getView().getStringByResId(R.string.pedido_venda_alerta_sem_valor);
            getView().mostrarMensagem(msg);
            return false;
        }
        int quantidadeBipada = produto.getQtde();
        if (quantidadeBipada <= SEM_VALOR) {
            String msg = getView().getStringByResId(R.string.pedido_venda_alerta_sem_item);
            getView().mostrarMensagem(msg);
            return false;
        }

        boolean temSaldo = vendaManager.temSaldo(produto.getId(), quantidadeBipada, produto.getQtde());
        if (!temSaldo) {
            String msg = getView().getStringByResId(R.string.pedido_venda_alerta_sem_estoque);
            getView().mostrarMensagem(msg);
            return false;
        }

        if (precoDiferenciado != null) {
            int quantidadePreco = vendaManager.retornaQtdPrecoDiferenciado(String.valueOf(precoDiferenciado.getId()));
            quantidadeBipada += quantidadePreco;
        }
        if ((precoDiferenciado != null)
                && (precoDiferenciado.getQtdPreco() > SEM_VALOR)
                && (quantidadeBipada > precoDiferenciado.getQtdPreco())) {
            String msg = getView().getStringByResId(
                    R.string.pedido_venda_alerta_promocao_quantidade,
                    precoDiferenciado.getQtdPreco()
            );
            getView().mostrarMensagem(msg);
            return false;
        }

        return true;
    }

    private void calcularTotal(List<ItemVendaCombo> itens) {
        total = 0;
        for (ItemVendaCombo item : itens) {
            total += item.getQtde() * item.getValorUN();
        }

        getView().atualizarTotal(Util_IO.formatDoubleToDecimalNonDivider(total));
    }

    private void adicionarTotal(ItemVendaCombo item) {
        total += item.getQtde() * item.getValorUN();
        getView().atualizarTotal(Util_IO.formatDoubleToDecimalNonDivider(total));
    }

    private void removerTotal(ItemVendaCombo item) {
        total -= item.getQtde() * item.getValorUN();
        getView().atualizarTotal(Util_IO.formatDoubleToDecimalNonDivider(total));
    }
}
