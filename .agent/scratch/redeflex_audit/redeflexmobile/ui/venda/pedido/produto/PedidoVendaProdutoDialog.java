package com.axys.redeflexmobile.ui.venda.pedido.produto;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseDaggerDialog;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.dialog.GenericaDialog;
import com.axys.redeflexmobile.ui.venda.pedido.PedidoVendaPresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class PedidoVendaProdutoDialog extends BaseDaggerDialog implements PedidoVendaProdutoView {

    public static final int TIMEOUT_CLICK = 1200;
    @Inject PedidoVendaProdutoPresenter presenter;
    @BindView(R.id.pedido_venda_produto_et_produto) AppCompatEditText etProduto;
    @BindView(R.id.pedido_venda_produto_et_quantidade) CustomEditText etQuantidade;
    @BindView(R.id.pedido_venda_produto_btn_adicionar) AppCompatButton btnAdicionar;
    @BindView(R.id.pedido_venda_produto_et_PrecoVenda) CustomEditText etPrecoVenda;

    private GenericaDialog dialogoPesquisaProduto;
    @NonNull private PedidoVendaProdutoEvento evento;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String idClienteSelecionado;
    private Produto produtoSelecionado;

    public static PedidoVendaProdutoDialog newInstance(@NonNull PedidoVendaProdutoEvento evento) {
        PedidoVendaProdutoDialog pedidoVendaProdutoDialog = new PedidoVendaProdutoDialog();
        pedidoVendaProdutoDialog.setEvento(evento);
        return pedidoVendaProdutoDialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.pedido_venda_produto_layout;
    }

    @Override
    protected void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        iniciarPresenter();
        iniciarEventos();
    }

    //@Override
    public void show(FragmentManager manager, String tag, String idCliente) {
        List<Fragment> frags = manager.getFragments();
        Optional<Fragment> fragmento = Stream.of(frags)
                .filter(frag -> frag.getTag() != null && frag.getTag().equals(tag))
                .findFirst();

        idClienteSelecionado = idCliente;

        if (fragmento.isEmpty() || !fragmento.get().isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
        compositeDisposable.dispose();
    }

    @Override
    public void popularListaPesquisa(List<Produto> produtos) {
        List<GenericaDialog.GenericaItem> lista = Stream.ofNullable(produtos)
                .map(value -> (GenericaDialog.GenericaItem) value)
                .toList();

        dialogoPesquisaProduto = GenericaDialog.newInstance(lista);

        dialogoPesquisaProduto.setOnSearchableItemClickListener(item -> {
            produtoSelecionado = new DBEstoque(getContext()).getProdutoById(item.getId());
            etProduto.setText(item.getDescricao());
            presenter.recuperarProduto(produtoSelecionado);

            if (produtoSelecionado != null) {
                etQuantidade.setEnabled(true);

                // Defina o formato para moeda brasileira
                NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                String valorFormatado = formatoMoeda.format(produtoSelecionado.getPrecovenda());
                etPrecoVenda.setText(valorFormatado);
                if (produtoSelecionado.getPrecovendaminimo() > 0)
                    etPrecoVenda.setEnabled(true);
                else
                    etPrecoVenda.setEnabled(false);
            }
            else {
                etPrecoVenda.setText("0");
                etPrecoVenda.setEnabled(false);
                etQuantidade.setEnabled(false);
            }
        });
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        Alerta alerta = new Alerta(
                getContext(),
                getString(R.string.app_name),
                mensagem
        );
        alerta.show();
    }

    @Override
    public void enviarProduto(Produto produto) {
        evento.quandoSelecionar(produto);
        dismiss();
    }

    private void iniciarPresenter() {
        presenter.attachView(this);
        presenter.carregarProdutos();
    }

    private void iniciarEventos() {
        Disposable disposableAdd = RxView.clicks(btnAdicionar)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (Util_IO.isNullOrEmpty(etQuantidade.getText().toString())) {
                        return;
                    }

                    if (Util_IO.isNullOrEmpty(etPrecoVenda.getText().toString())) {
                        return;
                    }

                    presenter.validarProduto(etQuantidade.getText().toString(),etPrecoVenda.getCurrencyDouble(), idClienteSelecionado);
                }, Timber::e);
        compositeDisposable.add(disposableAdd);

        Disposable disposableProd = RxView.clicks(etProduto)
                .throttleFirst(TIMEOUT_CLICK, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> mostrarListaProduto(), Timber::e);
        compositeDisposable.add(disposableProd);

        etQuantidade.setAfterTextListener(new CustomEditText.CustomEditAfterTextListener() {
            @Override
            public void afterTextChanged(String text) {
                if (!Util_IO.isNullOrEmpty(etQuantidade.getText().toString()))
                {
                    produtoSelecionado.setQtde(Integer.parseInt(etQuantidade.getText()));
                    // Defina o formato para moeda brasileira
                    if (produtoSelecionado != null) {
                        Preco preco = presenter.retornaPrecoDiferenciado(produtoSelecionado, idClienteSelecionado);
                        if (preco != null)
                        {
                            PrecoDiferenciado precoDif = presenter.obterPrecoDiferenciado(preco.getIdPreco());
                            if (precoDif != null)
                                produtoSelecionado.setPrecovenda(precoDif.getValor());
                            else
                                produtoSelecionado.setPrecovenda(new DBEstoque(getContext()).getProdutoById(produtoSelecionado.getId()).getPrecovenda());
                        }
                        else
                            produtoSelecionado.setPrecovenda(new DBEstoque(getContext()).getProdutoById(produtoSelecionado.getId()).getPrecovenda());

                        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        String valorFormatado = formatoMoeda.format(produtoSelecionado.getPrecovenda());
                        etPrecoVenda.setText(valorFormatado);
                    }
                    else {
                        etPrecoVenda.setText("0");
                        etPrecoVenda.setEnabled(false);
                    }
                }
                else
                {
                    produtoSelecionado.setPrecovenda(new DBEstoque(getContext()).getProdutoById(produtoSelecionado.getId()).getPrecovenda());
                    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    String valorFormatado = formatoMoeda.format(produtoSelecionado.getPrecovenda());
                    etPrecoVenda.setText(valorFormatado);
                }

                if (produtoSelecionado != null && produtoSelecionado.getPrecovendaminimo() > 0)
                    etPrecoVenda.setEnabled(true);
                else
                    etPrecoVenda.setEnabled(false);
            }
        });
    }

    private void mostrarListaProduto() {
        if (dialogoPesquisaProduto == null) {
            return;
        }

        dialogoPesquisaProduto.show(getChildFragmentManager(),
                GenericaDialog.class.getSimpleName());
    }

    private void setEvento(@NonNull PedidoVendaProdutoEvento evento) {
        this.evento = evento;
    }
}
