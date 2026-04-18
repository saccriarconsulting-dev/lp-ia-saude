package com.axys.redeflexmobile.ui.register.list.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Bruno Pimentel on 16/09/2019
 */
public class RegisterTokenDialog extends BaseDialog {

    @BindView(R.id.dialog_insert_token_et_token) EditText etToken;
    @BindView(R.id.dialog_insert_token_bt_cancel) Button btCancel;
    @BindView(R.id.dialog_insert_token_bt_save) Button btSave;

    private RegisterTokenDialogListener callback;

    public static RegisterTokenDialog newInstance(final RegisterTokenDialogListener callback) {
        final RegisterTokenDialog dialog = new RegisterTokenDialog();
        dialog.callback = callback;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_insert_token;
    }

    @Override
    protected void initialize(final View view) {
        btCancel.setOnClickListener(ignored -> dismiss());
        btSave.setOnClickListener(ignored -> {
            dismiss();
            callback.onSaveClick(etToken.getText().toString());
        });
    }

    @Override
    public void showLoading() {
        // não utilizado
    }

    @Override
    public void hideLoading() {
        // não utilizado
    }

    public interface RegisterTokenDialogListener {
        void onSaveClick(final String token);
    }
}
