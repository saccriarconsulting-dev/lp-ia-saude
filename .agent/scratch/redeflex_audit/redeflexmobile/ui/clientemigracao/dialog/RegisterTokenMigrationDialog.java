package com.axys.redeflexmobile.ui.clientemigracao.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 31/03/2020
 */
public class RegisterTokenMigrationDialog extends BaseDialog {

    @BindView(R.id.dialog_insert_token_et_token) EditText etToken;
    @BindView(R.id.dialog_insert_token_bt_cancel) Button btCancel;
    @BindView(R.id.dialog_insert_token_bt_save) Button btSave;

    private TokenDialogListener callback;

    public static RegisterTokenMigrationDialog newInstance(final TokenDialogListener callback) {
        final RegisterTokenMigrationDialog dialog = new RegisterTokenMigrationDialog();
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

    public interface TokenDialogListener {
        void onSaveClick(final String token);
    }
}
