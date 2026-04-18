package com.axys.redeflexmobile.ui.dialog.register;

import android.view.View;
import android.widget.Button;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseDialog;

import butterknife.BindView;

/**
 * @author Rogério Massa on 08/02/19.
 */

public class CustomerRegisterAddressRemoveDialog extends BaseDialog {

    @BindView(R.id.customer_register_address_remove_bt_cancel) Button btCancel;
    @BindView(R.id.customer_register_address_remove_bt_confirm) Button btConfirm;

    private CustomerRegisterAddressRemoveDialogListener callback;

    public static CustomerRegisterAddressRemoveDialog newInstance(CustomerRegisterAddressRemoveDialogListener callback) {
        CustomerRegisterAddressRemoveDialog dialog = new CustomerRegisterAddressRemoveDialog();
        dialog.setCallback(callback);
        return dialog;
    }

    public void setCallback(CustomerRegisterAddressRemoveDialogListener callback) {
        this.callback = callback;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_customer_register_address_remove;
    }

    @Override
    protected void initialize(View view) {
        btCancel.setOnClickListener(v -> dismiss());
        btConfirm.setOnClickListener(v -> onConfirmClick());
    }

    private void onConfirmClick() {
        if (callback != null) callback.onNext();
        dismiss();
    }

    public interface CustomerRegisterAddressRemoveDialogListener {
        void onNext();
    }
}
