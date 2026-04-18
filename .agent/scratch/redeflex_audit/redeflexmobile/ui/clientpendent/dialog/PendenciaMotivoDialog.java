package com.axys.redeflexmobile.ui.clientpendent.dialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.ui.base.BaseDialog;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 05/03/2020
 */
public class PendenciaMotivoDialog extends BaseDialog {

    private List<PendenciaMotivo> pendenciaMotivos = new ArrayList<>();
    private PendenciaCliente pendenciaCliente = new PendenciaCliente();
    private ClientePendenteActivity.ListenerOpenModal callback;

    @BindView(R.id.rg_motivos) RadioGroup rgMotivos;
    @BindView(R.id.tv_cancel) TextView tvCancelModal;
    @BindView(R.id.tv_confirm) TextView tvConfirmModal;

    @Override
    protected int getContentView() {
        return R.layout.dialog_responder_pendencia;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);
        initializeRadioGroup();
        initializeActions();
    }

    /**
     * Initialized radio group to set a list of pendencies.
     */
    private void initializeRadioGroup() {
        if (getContext() != null) {
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN);

            for (PendenciaMotivo motivo : pendenciaMotivos) {
                RadioButton rb = new RadioButton(getContext());
                rb.setText(motivo.getDescricao());
                rb.setId(motivo.getPendenciaMotivoId());
                rb.setLayoutParams(params);
                rb.setTextColor(getContext().getResources().getColor(R.color.textoCinzaEscuro));
                rgMotivos.addView(rb);
            }
        }
    }

    /**
     * Initialized the actions for the buttons in the modal.
     */
    private void initializeActions() {
        tvConfirmModal.setOnClickListener(v -> {
            if (rgMotivos.getCheckedRadioButtonId() != NO_SELECTED_ITEM) {
                callback.respostaModal(rgMotivos.getCheckedRadioButtonId(), pendenciaCliente);
            } else {
                if (getContext() != null) {
                    Toast.makeText(
                            getContext(),
                            getContext().getResources().getString(R.string.message_motivo_not_selected_item),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
            dismiss();
        });

        tvCancelModal.setOnClickListener(v -> dismiss());
    }

    public static PendenciaMotivoDialog newInstance(List<PendenciaMotivo> motivos,
                                                    PendenciaCliente pendenciaCliente,
                                                    ClientePendenteActivity.ListenerOpenModal callback) {
        PendenciaMotivoDialog modal = new PendenciaMotivoDialog();
        modal.setPendenciasMotivo(motivos);
        modal.setPendenciaCliente(pendenciaCliente);
        modal.setCallback(callback);
        return modal;
    }

    private void setPendenciasMotivo(List<PendenciaMotivo> motivos) {
        this.pendenciaMotivos = motivos;
    }

    private void setPendenciaCliente(PendenciaCliente pendenciaCliente) {
        this.pendenciaCliente = pendenciaCliente;
    }

    private void setCallback(ClientePendenteActivity.ListenerOpenModal callback) {
        this.callback = callback;
    }

    private static final int NO_SELECTED_ITEM = -1;
    private static final int VERTICAL_MARGIN = 22;
    private static final int HORIZONTAL_MARGIN = 0;
}
