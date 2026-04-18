package com.axys.redeflexmobile.ui.clientpendent.dialog;

import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteActivity;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 06/03/2020
 */
public class ConfirmDialog extends BaseDialog {

    private ClientePendenteActivity.ListenerCloseModal callback;

    @BindView(R.id.tv_confirm_dialog) TextView tvConfirmDialog;

    @Override
    protected int getContentView() {
        return R.layout.dialog_responder_pendencia_confirme;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);

        tvConfirmDialog.setOnClickListener(v -> {
            callback.sairActivity();
            dismiss();
        });
    }

    private void setCallback(ClientePendenteActivity.ListenerCloseModal callback) {
        this.callback = callback;
    }

    public static ConfirmDialog newInstance(ClientePendenteActivity.ListenerCloseModal callback) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCallback(callback);
        return dialog;
    }
}
