package com.axys.redeflexmobile.ui.register.customer.address;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.register.customer.address.RegisterCustomerAddressListViewHolder.RegisterCustomerAddressListViewHolderListener;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class RegisterCustomerAddressListAdapter extends BaseAdapter<CustomerRegisterAddressType, RegisterCustomerAddressListViewHolder>
        implements BaseViewHolder.IBaseViewHolderListener {

    private RegisterCustomerAddressListViewHolderListener viewListener;

    RegisterCustomerAddressListAdapter(RegisterCustomerAddressListViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.fragment_customer_register_address_list_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RegisterCustomerAddressListViewHolder(view, this, viewListener);
    }
}