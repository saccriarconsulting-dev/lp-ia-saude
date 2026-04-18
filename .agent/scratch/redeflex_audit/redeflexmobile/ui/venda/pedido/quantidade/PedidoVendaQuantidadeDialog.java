package com.axys.redeflexmobile.ui.venda.pedido.quantidade;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

public class PedidoVendaQuantidadeDialog extends BaseDialog {

    @BindView(R.id.pedido_venda_quantidade_tv_titulo) AppCompatTextView tvTitulo;
    @BindView(R.id.pedido_venda_quantidade_tv_esperado) AppCompatTextView tvEsperado;
    @BindView(R.id.pedido_venda_quantidade_tv_total) AppCompatTextView tvTotal;
    @BindView(R.id.pedido_venda_quantidade_btn_continuar) AppCompatButton btnContinuar;
    @BindView(R.id.pedido_venda_quantidade_btn_nao) AppCompatButton btnNao;

    private PedidoVendaQuantidadeEvento evento;
    private int quantidadeEsperada;
    private int quantidadeTotal;

    public static PedidoVendaQuantidadeDialog newInstance(int quantidadeEsperada,
                                                          int quantidadeTotal,
                                                          @NonNull PedidoVendaQuantidadeEvento evento) {
        PedidoVendaQuantidadeDialog dialog = new PedidoVendaQuantidadeDialog();
        dialog.setEvento(evento);
        dialog.setQuantidadeEsperada(quantidadeEsperada);
        dialog.setQuantidadeTotal(quantidadeTotal);

        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.pedido_venda_quantidade_layout;
    }

    @Override
    protected void initialize(View view) {
        popularTela();
        iniciarEvento();
    }

    private void iniciarEvento() {
        btnContinuar.setOnClickListener(v -> {
            evento.quandoSelecionar();
            dismiss();
        });
        btnNao.setOnClickListener(v -> dismiss());
    }

    private void popularTela() {
        String titulo = getString(
                R.string.pedido_venda_quantidade_tv_titulo,
                String.valueOf(quantidadeTotal)
        );
        tvTitulo.setText(titulo);

        String esperado = getString(
                R.string.pedido_venda_quantidade_tv_esperado,
                String.valueOf(quantidadeEsperada)
        );
        tvEsperado.setText(esperado);

        String total = getString(
                R.string.pedido_venda_quantidade_tv_total,
                String.valueOf(quantidadeTotal)
        );
        tvTotal.setText(total);
    }

    private void setEvento(PedidoVendaQuantidadeEvento evento) {
        this.evento = evento;
    }

    private void setQuantidadeEsperada(int quantidadeEsperada) {
        this.quantidadeEsperada = quantidadeEsperada;
    }

    private void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
}
