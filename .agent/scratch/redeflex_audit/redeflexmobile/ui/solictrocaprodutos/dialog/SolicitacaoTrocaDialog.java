package com.axys.redeflexmobile.ui.solictrocaprodutos.dialog;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.base.BaseDialog;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static java.lang.Integer.parseInt;

/**
 * Created by Rogério Massa on 05/10/18.
 */

public class SolicitacaoTrocaDialog extends BaseDialog {

    private static final String STRING_SPACE = " ";
    private static final String STRING_QUESTION = "?";

    @BindView(R.id.solicitacao_troca_dialog_tv_texto) TextView tvText;
    @BindView(R.id.solicitacao_troca_dialog_bt_ok) Button btOk;
    @BindView(R.id.solicitacao_troca_dialog_tv_produto) TextView tvProduto;
    @BindView(R.id.solicitacao_troca_dialog_ll_simnao) LinearLayout llSimNao;
    @BindView(R.id.solicitacao_troca_dialog_bt_positivo) Button segundoBotao;
    @BindView(R.id.solicitacao_troca_dialog_bt_negativo) Button primeiroBotao;
    @BindView(R.id.solicitacao_troca_dialog_ll_motivo) LinearLayout llMotivo;
    @BindView(R.id.solicitacao_troca_dialog_ss_motivo) SearchableSpinner ssMotivo;
    @BindView(R.id.solicitacao_troca_dialog_qtde_ll_produto) LinearLayout llQtdeProduto;
    @BindView(R.id.solicitacao_troca_dialog_qtde_tv_produto_nome) TextView tvQtdeProduto;
    @BindView(R.id.solicitacao_troca_dialog_qtde_tv_produto_texto) TextView tvQtdeText;
    @BindView(R.id.solicitacao_troca_dialog_qtde_et_produto_qtde) EditText etQtdeProduto;

    private String texto;
    private String primeiroBotaoTexto;
    private String segundoBotaoTexto;
    private List<SolicitacaoTrocaMotivo> motivos;
    private SolicitarTrocaListener primeiroBotaoListener;
    private SolicitarTrocaListener segundoBotaoListener;
    private SolicitarTrocaCallback botaoCallback;
    private String produtoNome;
    private ValidacaoDialogTipo tipoModal;
    private Context context;

    public static SolicitacaoTrocaDialog newInstance(String texto,
                                                     String primeiroBotaoTexto,
                                                     SolicitarTrocaListener primeiroBotaoListener) {
        SolicitacaoTrocaDialog modal = new SolicitacaoTrocaDialog();
        modal.setTexto(texto);
        modal.setPrimeiroBotaoTexto(primeiroBotaoTexto);
        modal.setPrimeiroBotaoListener(primeiroBotaoListener);
        modal.setTipoModal(ValidacaoDialogTipo.SIMPLES);
        return modal;
    }

    public static SolicitacaoTrocaDialog newInstance(String texto,
                                                     String primeiroBotaoTexto,
                                                     String segundoBotaoTexto,
                                                     SolicitarTrocaListener primeiroBotaoListener,
                                                     SolicitarTrocaListener segundoBotaoListener) {
        SolicitacaoTrocaDialog modal = new SolicitacaoTrocaDialog();
        modal.setTexto(texto);
        modal.setPrimeiroBotaoTexto(primeiroBotaoTexto);
        modal.setSegundoBotaoTexto(segundoBotaoTexto);
        modal.setPrimeiroBotaoListener(primeiroBotaoListener);
        modal.setSegundoBotaoListener(segundoBotaoListener);
        modal.setTipoModal(ValidacaoDialogTipo.SIMNAO);
        return modal;
    }

    public static SolicitacaoTrocaDialog newInstance(String primeiroBotaoTexto,
                                                     String segundoBotaoTexto,
                                                     List<SolicitacaoTrocaMotivo> motivos,
                                                     SolicitarTrocaListener primeiroBotaoListener,
                                                     SolicitarTrocaCallback botaoCallback) {
        SolicitacaoTrocaDialog modal = new SolicitacaoTrocaDialog();
        modal.setPrimeiroBotaoTexto(primeiroBotaoTexto);
        modal.setSegundoBotaoTexto(segundoBotaoTexto);
        modal.setMotivos(motivos);
        modal.setBotaoCallback(botaoCallback);
        modal.setPrimeiroBotaoListener(primeiroBotaoListener);
        modal.setTipoModal(ValidacaoDialogTipo.MOTIVO);
        return modal;
    }

    public static SolicitacaoTrocaDialog newInstance(String texto,
                                                     String produtoNome,
                                                     String primeiroBotaoTexto,
                                                     String segundoBotaoTexto,
                                                     SolicitarTrocaListener primeiroBotaoListener,
                                                     SolicitarTrocaListener segundoBotaoListener) {
        SolicitacaoTrocaDialog modal = new SolicitacaoTrocaDialog();
        modal.setTexto(texto);
        modal.setProdutoNome(produtoNome);
        modal.setPrimeiroBotaoTexto(primeiroBotaoTexto);
        modal.setSegundoBotaoTexto(segundoBotaoTexto);
        modal.setPrimeiroBotaoListener(primeiroBotaoListener);
        modal.setSegundoBotaoListener(segundoBotaoListener);
        modal.setTipoModal(ValidacaoDialogTipo.CANCELARPRODUTO);
        return modal;
    }

    public static SolicitacaoTrocaDialog newInstance(String texto,
                                                     String produtoNome,
                                                     SolicitarTrocaCallback botaoCallback,
                                                     SolicitarTrocaListener segundoBotaoListener) {
        SolicitacaoTrocaDialog modal = new SolicitacaoTrocaDialog();
        modal.setTexto(texto);
        modal.setProdutoNome(produtoNome);
        modal.setBotaoCallback(botaoCallback);
        modal.setSegundoBotaoListener(segundoBotaoListener);
        modal.setTipoModal(ValidacaoDialogTipo.CONFIRMARPRODUTO);
        return modal;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setSegundoBotaoTexto(String segundoBotaoTexto) {
        this.segundoBotaoTexto = segundoBotaoTexto;
    }

    public void setPrimeiroBotaoTexto(String primeiroBotaoTexto) {
        this.primeiroBotaoTexto = primeiroBotaoTexto;
    }

    public void setMotivos(List<SolicitacaoTrocaMotivo> motivos) {
        this.motivos = motivos;
    }

    public void setSegundoBotaoListener(SolicitarTrocaListener segundoBotaoListener) {
        this.segundoBotaoListener = segundoBotaoListener;
    }

    public void setPrimeiroBotaoListener(SolicitarTrocaListener primeiroBotaoListener) {
        this.primeiroBotaoListener = primeiroBotaoListener;
    }

    public void setBotaoCallback(SolicitarTrocaCallback botaoCallback) {
        this.botaoCallback = botaoCallback;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setTipoModal(ValidacaoDialogTipo tipoModal) {
        this.tipoModal = tipoModal;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_validacao;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        this.context = getContext();

        switch (tipoModal) {
            case MOTIVO:
                this.inicializarModalMotivo();
                break;
            case SIMNAO:
                this.inicializarModalSimNao();
                break;
            case CANCELARPRODUTO:
                this.inicializarModalCancelarProduto();
                break;
            case CONFIRMARPRODUTO:
                this.inicializarModalConfirmarProduto();
                break;
            default:
                this.inicializarModalSimples();
        }
    }

    private void inicializarModalSimples() {
        tvText.setText(texto);
        tvText.setVisibility(View.VISIBLE);
        btOk.setVisibility(View.VISIBLE);
        btOk.setText(primeiroBotaoTexto != null && !primeiroBotaoTexto.isEmpty() ? primeiroBotaoTexto : "OK");
        btOk.setOnClickListener(view -> {
            if (primeiroBotaoListener != null) primeiroBotaoListener.click();
            dismiss();
        });
    }

    private void inicializarModalSimNao() {
        tvText.setText(texto);
        tvText.setVisibility(View.VISIBLE);

        llSimNao.setVisibility(View.VISIBLE);
        primeiroBotao.setText(primeiroBotaoTexto != null && !primeiroBotaoTexto.isEmpty() ? primeiroBotaoTexto : "NÃO");
        segundoBotao.setText(segundoBotaoTexto != null && !segundoBotaoTexto.isEmpty() ? segundoBotaoTexto : "SIM");

        primeiroBotao.setOnClickListener(view -> {
            if (primeiroBotaoListener != null) primeiroBotaoListener.click();
            dismiss();
        });
        segundoBotao.setOnClickListener(view -> {
            if (segundoBotaoListener != null) segundoBotaoListener.click();
            dismiss();
        });
    }

    private void inicializarModalMotivo() {
        llMotivo.setVisibility(View.VISIBLE);
        ssMotivo.setAdapter(obterAdapterMotivos(motivos));

        llSimNao.setVisibility(View.VISIBLE);
        primeiroBotao.setText(primeiroBotaoTexto != null && !primeiroBotaoTexto.isEmpty() ? primeiroBotaoTexto : "NÃO");
        segundoBotao.setText(segundoBotaoTexto != null && !segundoBotaoTexto.isEmpty() ? segundoBotaoTexto : "SIM");

        primeiroBotao.setOnClickListener(view -> {
            if (primeiroBotaoListener != null) primeiroBotaoListener.click();
            dismiss();
        });
        segundoBotao.setOnClickListener(view -> {
            SolicitacaoTrocaMotivo motivo;
            try {
                motivo = motivos.get(ssMotivo.getSelectedItemPosition());
            } catch (IndexOutOfBoundsException e) {
                return;
            }
            botaoCallback.click(parseInt(motivo.getMotivoId()));
            dismiss();
        });
    }

    private void inicializarModalCancelarProduto() {
        tvText.setText(texto);
        tvText.setVisibility(View.VISIBLE);

        llSimNao.setVisibility(View.VISIBLE);
        tvProduto.setText(obterNomeProduto(), TextView.BufferType.SPANNABLE);
        tvProduto.setVisibility(View.VISIBLE);

        primeiroBotao.setText(primeiroBotaoTexto != null && !primeiroBotaoTexto.isEmpty() ? primeiroBotaoTexto : "NÃO");
        segundoBotao.setText(segundoBotaoTexto != null && !segundoBotaoTexto.isEmpty() ? segundoBotaoTexto : "SIM");

        primeiroBotao.setOnClickListener(view -> {
            if (primeiroBotaoListener != null) primeiroBotaoListener.click();
            dismiss();
        });
        segundoBotao.setOnClickListener(view -> {
            if (segundoBotaoListener != null) segundoBotaoListener.click();
            dismiss();
        });
    }

    private void inicializarModalConfirmarProduto() {

        llQtdeProduto.setVisibility(View.VISIBLE);
        tvQtdeProduto.setText(produtoNome);
        tvQtdeText.setText(texto);

        llSimNao.setVisibility(View.VISIBLE);
        primeiroBotao.setText(context.getString(R.string.solicitacao_troca_dialog_bt_sim));
        segundoBotao.setText(context.getString(R.string.solicitacao_troca_dialog_bt_nao));

        primeiroBotao.setOnClickListener(view -> {
            String qtde = etQtdeProduto.getText().toString();
            if (Util_IO.isNullOrEmpty(qtde)) return;
            if (botaoCallback != null) botaoCallback.click(Integer.parseInt(qtde));
            dismiss();
        });
        segundoBotao.setOnClickListener(view -> {
            if (segundoBotaoListener != null) segundoBotaoListener.click();
            dismiss();
        });
    }

    private SpannableStringBuilder obterNomeProduto() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString primeiraString = new SpannableString(produtoNome);
        primeiraString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,
                R.color.preto)), 0, produtoNome.length(), 0);
        builder.append(primeiraString);
        builder.append(STRING_SPACE);
        String interrgacao = STRING_QUESTION;
        SpannableString segundaString = new SpannableString(interrgacao);
        segundaString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,
                R.color.colorAccent)), 0, interrgacao.length(), 0);
        builder.append(segundaString);
        return builder;
    }

    public StringAdapter obterAdapterMotivos(List<SolicitacaoTrocaMotivo> lista) {
        ArrayList<String> retorno = new ArrayList<>();
        for (SolicitacaoTrocaMotivo item : lista) retorno.add(item.getDescricao());
        return new StringAdapter(context, R.layout.custom_spinner_title_bar, retorno);
    }

    public enum ValidacaoDialogTipo {
        SIMPLES, SIMNAO, MOTIVO, CANCELARPRODUTO, CONFIRMARPRODUTO
    }

    public interface SolicitarTrocaListener {
        void click();
    }

    public interface SolicitarTrocaCallback {
        void click(int intValue);
    }
}
