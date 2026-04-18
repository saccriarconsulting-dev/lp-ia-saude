package com.axys.redeflexmobile.ui.register.dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 05/06/2020
 */
@SuppressLint("NonConstantResourceId")
public class NoTaxesFoundedDialog extends BaseDialog {

    private NoTaxesFoundedDialogListener confirmCallback;

    @BindView(R.id.bt_ok) Button btOk;

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
