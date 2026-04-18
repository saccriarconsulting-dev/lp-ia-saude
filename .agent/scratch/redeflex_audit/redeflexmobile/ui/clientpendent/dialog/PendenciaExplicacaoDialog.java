package com.axys.redeflexmobile.ui.clientpendent.dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 27/05/2020
 */
@SuppressLint("NonConstantResourceId")
public class PendenciaExplicacaoDialog extends BaseDialog {
    private final int MIN_VALUE = 10;
    private final int MAX_VALUE = 500;

    @BindView(R.id.et_explicacao) EditText etExplicacao;
    @BindView(R.id.tv_cancel) TextView tvCancelModal;
    @BindView(R.id.tv_confirm) TextView tvConfirmModal;

    private PendenciaExplicacaoCallback callback;
    private PendenciaCliente pendenciaCliente;
    private int motivoId;

    @Override
    protected int getContentView() {
        return R.layout.dialog_responder_explicacao_pendencia;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        initializeActions();
    }

    /**
     * Initialized the actions for the buttons in the modal.
     */
    private void initializeActions() {
        tvConfirmModal.setOnClickListener(v -> {
            String response = etExplicacao.getText().toString();
            if (response.length() >= MIN_VALUE && response.length() <= MAX_VALUE) {
                pendenciaCliente.setExplicacao(response);
                pendenciaCliente.setPendenciaMotivoId(motivoId);
                this.callback.doOnRespond(pendenciaCliente);
                dismiss();
            } else {
                if (getContext() != null) {
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(
                                    R.string.message_error_limit_explication_pendencie
                            ),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        tvCancelModal.setOnClickListener(v -> dismiss());
    }

    public static PendenciaExplicacaoDialog newInstance(PendenciaExplicacaoCallback callback,
                                                        PendenciaCliente pendenciaCliente,
                                                        int motivoId) {
        PendenciaExplicacaoDialog modal = new PendenciaExplicacaoDialog();
        modal.setCallback(callback);
        modal.setPendenciaCliente(pendenciaCliente);
        modal.setMotivoId(motivoId);
        return modal;
    }

    private void setCallback(PendenciaExplicacaoCallback callback) {
        this.callback = callback;
    }

    public void setPendenciaCliente(PendenciaCliente pendenciaCliente) {
        this.pendenciaCliente = pendenciaCliente;
    }
    public void setMotivoId(int motivoId) {
        this.motivoId = motivoId;
    }

    public interface PendenciaExplicacaoCallback {
        void doOnRespond(PendenciaCliente pendenciaCliente);
    }
}
