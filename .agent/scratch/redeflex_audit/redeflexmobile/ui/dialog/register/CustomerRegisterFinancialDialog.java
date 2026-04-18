package com.axys.redeflexmobile.ui.dialog.register;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Rogério Massa on 08/02/19.
 */

public class CustomerRegisterFinancialDialog extends BaseDialog {

    @BindView(R.id.customer_register_financial_bt_confirm) Button btConfirm;

    private CustomerRegisterFinancialDialogListener callback;

    public static CustomerRegisterFinancialDialog newInstance(CustomerRegisterFinancialDialogListener callback) {
        CustomerRegisterFinancialDialog dialog = new CustomerRegisterFinancialDialog();
        dialog.setCallback(callback);
        return dialog;
    }

    public void setCallback(CustomerRegisterFinancialDialogListener callback) {
        this.callback = callback;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_customer_register_financial;
    }

    @Override
    protected void initialize(View view) {
        btConfirm.setOnClickListener(v -> onConfirmClick());
    }

    private void onConfirmClick() {
        if (callback != null) callback.onNext();
        dismiss();
    }

    public interface CustomerRegisterFinancialDialogListener {
        void onNext();
    }
}
