package com.axys.redeflexmobile.ui.register.customer.personal.dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author lucasmarciano on 14/07/20
 */
@SuppressLint("NonConstantResourceId")
public class FillCnpjInfoDialog extends BaseDialog {

    private ListenerModal callback;
    private String cnpj;

    @BindView(R.id.tv_cancel) TextView tvCancel;
    @BindView(R.id.tv_confirm) TextView tvConfirm;

    @Override
    protected int getContentView() {
        return R.layout.dialog_fill_cnpj_information;
    }

    @Override
    protected void initialize(View view) {
        setCancelable(false);

        tvConfirm.setOnClickListener(v -> {
            callback.doOnConfirm(true, cnpj);
            dismiss();
        });

        tvCancel.setOnClickListener(v -> {
            callback.doOnConfirm(false, cnpj);
            dismiss();
        });
    }

    private void setCallback(ListenerModal callback) {
        this.callback = callback;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public static FillCnpjInfoDialog newInstance(ListenerModal confirmCallback, String cnpj) {
        FillCnpjInfoDialog dialog = new FillCnpjInfoDialog();
        dialog.setCallback(confirmCallback);
        dialog.setCnpj(cnpj);
        return dialog;
    }

    public interface ListenerModal {
        void doOnConfirm(boolean response, String cnpj);
    }

}
