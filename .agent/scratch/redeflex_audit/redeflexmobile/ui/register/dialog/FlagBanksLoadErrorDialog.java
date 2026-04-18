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
public class FlagBanksLoadErrorDialog extends BaseDialog {

    private FlagBanksLoadErrorDialogListener confirmCallback;

    @BindView(R.id.bt_ok) Button btOk;

    public static FlagBanksLoadErrorDialog newInstance(FlagBanksLoadErrorDialogListener confirmCallback) {
        FlagBanksLoadErrorDialog dialog = new FlagBanksLoadErrorDialog();
        dialog.setCallbackConfirm(confirmCallback);
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_flag_banks_load_error;
    }

    @Override
    protected void initialize(View view) {
        btOk.setOnClickListener(ignored -> {
            confirmCallback.doOnClose();
            dismiss();
        });
    }

    private void setCallbackConfirm(FlagBanksLoadErrorDialogListener confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public interface FlagBanksLoadErrorDialogListener {
        void doOnClose();
    }
}
