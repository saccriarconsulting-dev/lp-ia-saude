package com.axys.redeflexmobile.ui.clientevalidacao.dialog;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 31/03/2020
 */
public class OpenMigrationDialog extends BaseDialog {

    @BindView(R.id.bt_migration_continue) Button btContinue;

    private OpenMigrationConfirmDialogListener confirmCallback;

    public static OpenMigrationDialog newInstance(OpenMigrationConfirmDialogListener confirmCallback) {
        OpenMigrationDialog dialog = new OpenMigrationDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_alert_open_migration;
    }

    @Override
    protected void initialize(View view) {
        btContinue.setOnClickListener(ignored -> {
            confirmCallback.doOnContinue();
            dismiss();
        });
    }

    private void setCallbackConfirm(OpenMigrationConfirmDialogListener confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface OpenMigrationConfirmDialogListener {
        void doOnContinue();
    }
}
