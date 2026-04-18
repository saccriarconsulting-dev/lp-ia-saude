package com.axys.redeflexmobile.ui.simulationrate.dialogs;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 05/05/2020
 */
public class ConfirmProspectDialog extends BaseDialog {

    private CallbackImportData confirmCallback;

    @BindView(R.id.bt_continue) Button btContinue;
    @BindView(R.id.bt_cancel) Button btCancel;

    public static ConfirmProspectDialog newInstance(CallbackImportData confirmCallback) {
        ConfirmProspectDialog dialog = new ConfirmProspectDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_confirm_prospect;
    }

    @Override
    protected void initialize(View view) {
        btContinue.setOnClickListener(ignored -> {
            confirmCallback.doOnImportData();
            dismiss();
        });

        btCancel.setOnClickListener(ignored -> dismiss());
    }

    private void setCallbackConfirm(CallbackImportData confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface CallbackImportData {
        void doOnImportData();
    }
}
