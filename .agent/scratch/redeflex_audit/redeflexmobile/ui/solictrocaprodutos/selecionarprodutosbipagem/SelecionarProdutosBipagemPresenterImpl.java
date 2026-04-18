package com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutosbipagem;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaCodBarras;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaDetalhes;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.solictrocaprodutos.selecionarprodutos.SelecionarProdutosManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rogério Massa on 15/10/18.
 */

public class SelecionarProdutosBipagemPresenterImpl extends BasePresenterImpl<SelecionarProdutosBipagemView>
        implements SelecionarProdutosBipagemPresenter {

    public static final int DEFAULT_INT_VALUE = -1;
    private SelecionarProdutosBipagemManager manager;
    private final SelecionarProdutosManager selecionarProdutosManager;
    private Produto produto;
    private String produtoAntigo;
    private List<SolicitacaoTrocaCodBarras> listaCodigoBarras;

    private boolean informarCodBarrasActivity;
    private Integer informarCodigoBarrasCodBarraId;
    private List<SolicitacaoTrocaCodBarras> informarCodigoBarrasLista;
    private boolean informarRemocao = false;

    SelecionarProdutosBipagemPresenterImpl(SelecionarProdutosBipagemActivity view,
                                           SchedulerProvider schedulerProvider,
                                           ExceptionUtils exceptionUtils,
                                           SelecionarProdutosBipagemManager manager,
                                           SelecionarProdutosManager selecionarProdutosManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
        this.selecionarProdutosManager = selecionarProdutosManager;
        this.listaCodigoBarras = new ArrayList<>();
    }

    @Override
    public boolean isInformarCodBarrasActivity() {
        return informarCodBarrasActivity;
    }

    @Override
    public void setInformarCodBarrasActivity(boolean ativa) {
        this.informarCodBarrasActivity = ativa;
    }

    @Override
    public void setInformarCodigoBarrasCodBarraId(Integer informarCodigoBarrasCodBarraId) {
        if (informarCodigoBarrasCodBarraId == DEFAULT_INT_VALUE)
            informarCodigoBarrasCodBarraId = null;
        this.informarCodigoBarrasCodBarraId = informarCodigoBarrasCodBarraId;
    }

    @Override
    public void setInformarCodigoBarrasLista(String lista) {
        if (Util_IO.isNullOrEmpty(lista)) return;
        this.informarCodigoBarrasLista = new ArrayList<>(Arrays.asList(new Gson().fromJson(lista, SolicitacaoTrocaCodBarras[].class)));
    }

    @Override
    public void inicializarListagem(String produtos) {
        if (Util_IO.isNullOrEmpty(produtos)) return;
        produtoAntigo = produtos;
        listaCodigoBarras = new ArrayList<>(Arrays.asList(new Gson().fromJson(produtos, SolicitacaoTrocaCodBarras[].class)));
        informarRemocao = true;
        getView().carregarListagem(listaCodigoBarras, false);
    }

    @Override
    public Produto getProduto() {
        return produto;
    }

    @Override
    public Produto getProdutoById(String id) {
        this.produto = manager.obterProdutoPorId(id);
        return this.produto;
    }

    @Override
    public void deletarCodBarra(String codBarra) {
        SolicitacaoTrocaCodBarras item = Stream.of(listaCodigoBarras)
                .filter(value -> value.getIccidDe().equals(codBarra)).findFirst().orElse(null);
        if (item != null) listaCodigoBarras.remove(item);
        getView().carregarListagem(listaCodigoBarras, false);
    }

    @Override
    public void inserirCodigoBarra(final String codBarra) {

        if (Util_IO.isNullOrEmpty(codBarra)) {
            Mensagens.codigoBarraNaoInformado(getView().getContext(), (dialog, which) -> getView().abrirCamera());
            return;
        }

        if (this.informarCodBarrasActivity) {
            if (this.informarCodigoBarrasCodBarraId == null) {
                validaInformarCodigoBarraAntigo(codBarra);
            } else {
                validaInformarCodigoBarraNovo(codBarra);
            }
            return;
        }


        if (Stream.ofNullable(this.listaCodigoBarras)
                .filter(value -> value.getIccidDe() != null && value.getIccidDe().equals(codBarra))
                .findFirst().orElse(null) != null) {

            Alerta alerta = new Alerta(getView().getContext(), codBarra, getView()
                    .getStringByResId(R.string.solicitacao_troca_produto_bipagem_dialog_codigo_ja_adicionado));
            alerta.show((dialog, which) -> getView().abrirCamera());
            return;
        }

        Alerta alerta = new Alerta(getView().getContext(), codBarra,
                getView().getStringByResId(R.string.solicitacao_troca_produto_bipagem_dialog_confirmar));
        alerta.showConfirm((dialog, which) -> addCodBarra(codBarra),
                (dialog, which) -> getView().abrirCamera());
    }

    private void validaInformarCodigoBarraAntigo(String codBarra) {

        SolicitacaoTrocaCodBarras solicitacaoTrocaCodBarras = Stream.ofNullable(this.informarCodigoBarrasLista)
                .filter(value -> value.getIccidDe().equals(codBarra))
                .findFirst()
                .orElse(null);

        if (solicitacaoTrocaCodBarras == null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_antigo_nao_confere));
            return;
        }

        if (solicitacaoTrocaCodBarras.isBipadoAntigo()) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_antigo_ja_trocado));
            return;
        }


        Alerta alerta = new Alerta(getView().getContext(), codBarra,
                getView().getStringByResId(R.string.solicitacao_troca_produto_bipagem_dialog_confirmar));
        alerta.showConfirm((dialog, which) -> getView()
                        .retornarInformarCodigoBarraActivity(solicitacaoTrocaCodBarras.getIdApp(), codBarra),
                (dialog, which) -> getView().abrirCamera());
    }

    private void validaInformarCodigoBarraNovo(String codBarra) {
        SolicitacaoTrocaCodBarras solicitacaoTrocaCodBarras = Stream.ofNullable(this.informarCodigoBarrasLista)
                .filter(value -> value.getIdApp() == informarCodigoBarrasCodBarraId && value.getIccidDe().equals(codBarra))
                .findFirst()
                .orElse(null);

        if (solicitacaoTrocaCodBarras != null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_codigo_duplicado));
            return;
        }

        Iccid iccid = manager.getIccIdById(codBarra);
        if (iccid == null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_codigo_indisponivel));
            return;
        }

        if (!Util_IO.isNullOrEmpty(produto.getIniciaCodBarra()) && !codBarra.startsWith(produto.getIniciaCodBarra())) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_inicio_invalido, produto.getIniciaCodBarra()));
            return;
        }

        if (produto.getQtdCodBarra() > 0 && codBarra.trim().length() != produto.getQtdCodBarra()) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_quantidade_invalida, produto.getQtdCodBarra()));
            return;
        }

        if (Stream.ofNullable(this.informarCodigoBarrasLista)
                .filter(value -> value.getIccidPara() != null && value.getIccidPara().equals(codBarra))
                .findFirst().orElse(null) != null) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_duplicidade));
            return;
        }

        if (!manager.verificaEstoque(produto.getId(), 1)) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_estoque_invalido));
            return;
        }

        if (!produto.getId().equals(iccid.getItemCode())) {
            getView().exibirErro(getView().getStringByResId(R.string.solicitacao_troca_informar_cod_barras_novo_invalido, produto.getNome()));
            return;
        }

        Alerta alerta = new Alerta(getView().getContext(),
                codBarra,
                getView().getStringByResId(R.string.solicitacao_troca_produto_bipagem_dialog_confirmar));

        alerta.showConfirm((dialog, which) -> getView().retornarInformarCodigoBarraActivity(
                informarCodigoBarrasCodBarraId, codBarra),
                (dialog, which) -> getView().abrirCamera());
    }

    @Override
    public SolicitacaoTrocaDetalhes obterProdutoSalvar() {
        if (listaCodigoBarras == null || listaCodigoBarras.isEmpty()) return null;
        SolicitacaoTrocaDetalhes solicitacaoTrocaDetalhes = new SolicitacaoTrocaDetalhes();
        solicitacaoTrocaDetalhes.setProdutoCodigo(produto.getId());
        solicitacaoTrocaDetalhes.setProdutoNome(produto.getNome());
        solicitacaoTrocaDetalhes.setQtde(listaCodigoBarras.size());
        solicitacaoTrocaDetalhes.setValorUnitario(produto.getValor());
        solicitacaoTrocaDetalhes.setValorTotal(listaCodigoBarras.size() * solicitacaoTrocaDetalhes.getValorUnitario());
        solicitacaoTrocaDetalhes.setIccids(listaCodigoBarras);
        return solicitacaoTrocaDetalhes;
    }

    @Override
    public boolean deveRemoverProduto() {
        return informarRemocao;
    }

    @Override
    public void exibirMotivo() {
        SolicitacaoTrocaDetalhes detalhes = obterProdutoSalvar();
        String produtoNovo = new Gson().toJson(detalhes.getIccids());
        if (produtoNovo.equals(produtoAntigo)) {
            getView().sairSemAlteracao();
            return;
        }
        getView().exibirMotivos(selecionarProdutosManager.obterMotivos(), detalhes);
    }

    private void addCodBarra(String codBarra) {
        if (listaCodigoBarras == null) listaCodigoBarras = new ArrayList<>();
        SolicitacaoTrocaCodBarras solicitacaoTrocaCodBarras = new SolicitacaoTrocaCodBarras();
        solicitacaoTrocaCodBarras.setCodigoItem(this.produto.getId());
        solicitacaoTrocaCodBarras.setIccidDe(codBarra);
        listaCodigoBarras.add(solicitacaoTrocaCodBarras);
        getView().carregarListagem(listaCodigoBarras, true);
    }
}
