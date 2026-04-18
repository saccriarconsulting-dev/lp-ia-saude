package com.axys.redeflexmobile.ui.clientemigracao.dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 05/06/2020
 */
public class NoTaxesFoundedDialog extends BaseDialog {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_ok) Button btOk;

    private NoTaxesFoundedDialogListener confirmCallback;

    public static NoTaxesFoundedDialog newInstance(NoTaxesFoundedDialogListener confirmCallback) {
        NoTaxesFoundedDialog dialog = new NoTaxesFoundedDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_no_taxes_found;
    }

    @Override
    protected void initialize(View view) {
        btOk.setOnClickListener(ignored -> {
            confirmCallback.doOnClose();
            dismiss();
        });
    }

    private void setCallbackConfirm(NoTaxesFoundedDialogListener confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface NoTaxesFoundedDialogListener {
        void doOnClose();
    }
}
