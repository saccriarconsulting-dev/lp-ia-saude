package com.axys.redeflexmobile.ui.simulationrate.dialogs;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 27/04/2020
 */
public class ChooseImportDataRegisterDialog extends BaseDialog {

    private CallbackImportData confirmCallback;
    private boolean isNewProspect;

    @BindView(R.id.bt_continue) Button btContinue;
    @BindView(R.id.bt_cancel) Button btCancel;
    @BindView(R.id.tv_observation) TextView tvObservation;

    public static ChooseImportDataRegisterDialog newInstance(CallbackImportData confirmCallback, boolean isNewProspect) {
        ChooseImportDataRegisterDialog dialog = new ChooseImportDataRegisterDialog();
        dialog.setCallbackConfirm(confirmCallback);
        dialog.setNewProspect(isNewProspect);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_import_data_register;
    }

    @Override
    protected void initialize(View view) {
        btContinue.setOnClickListener(ignored -> {
            confirmCallback.doOnImportData();
            dismiss();
        });

        btCancel.setOnClickListener(ignored -> dismiss());

        tvObservation.setVisibility(isNewProspect ? View.GONE : View.VISIBLE);
    }

    private void setCallbackConfirm(CallbackImportData confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public void setNewProspect(boolean isNewProspect) {
        this.isNewProspect = isNewProspect;
    }

    public interface CallbackImportData {
        void doOnImportData();
    }
}
