package com.axys.redeflexmobile.ui.clientemigracao.dialog;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Lucas Marciano on 05/06/2020
 */
public class NoTaxesSelectedDialog extends BaseDialog {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_ok) Button btOk;

    public static NoTaxesSelectedDialog newInstance() {
        NoTaxesSelectedDialog dialog = new NoTaxesSelectedDialog();
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_no_taxes_selected;
    }

    @Override
    protected void initialize(View view) {
        btOk.setOnClickListener(ignored -> {
            dismiss();
        });
    }
}
