package com.axys.redeflexmobile.ui.simulationrate.dialogs;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 27/04/2020
 */
public class CancelRegisterRateDialog extends BaseDialog {

    private CancelRegisterRateDialogListener confirmCallback;

    @BindView(R.id.bt_continue) Button btContinue;
    @BindView(R.id.bt_cancel) Button btCancel;

    public static CancelRegisterRateDialog newInstance(CancelRegisterRateDialogListener confirmCallback) {
        CancelRegisterRateDialog dialog = new CancelRegisterRateDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_cancel_register_rate;
    }

    @Override
    protected void initialize(View view) {
        btContinue.setOnClickListener(ignored -> {
            confirmCallback.doOnClose();
            dismiss();
        });

        btCancel.setOnClickListener(ignored -> dismiss());
    }

    private void setCallbackConfirm(CancelRegisterRateDialogListener confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface CancelRegisterRateDialogListener {
        void doOnClose();
    }
}
