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
public class FlagBanksLoadEmptyDialog extends BaseDialog {

    private FlagBanksLoadEmptyDialogListener confirmCallback;

    @BindView(R.id.bt_ok) Button btOk;

    public static FlagBanksLoadEmptyDialog newInstance(FlagBanksLoadEmptyDialogListener confirmCallback) {
        FlagBanksLoadEmptyDialog dialog = new FlagBanksLoadEmptyDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_flag_banks_load_empty;
    }

    @Override
    protected void initialize(View view) {
        btOk.setOnClickListener(ignored -> {
            confirmCallback.doOnClose();
            dismiss();
        });
    }

    private void setCallbackConfirm(FlagBanksLoadEmptyDialogListener confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface FlagBanksLoadEmptyDialogListener {
        void doOnClose();
    }
}
