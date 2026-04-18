package com.axys.redeflexmobile.ui.register.customer.address;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.customer.address.RegisterCustomerAddressListViewHolder.RegisterCustomerAddressListViewHolderListener;
import com.axys.redeflexmobile.ui.register.customer.address.addressregister.RegisterCustomerAddressFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class RegisterCustomerAddressListFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerAddressListView,
        RegisterCustomerAddressListViewHolderListener {

    @Inject RegisterCustomerAddressListPresenter presenter;
    @Inject RegisterCustomerAddressListAdapter adapter;

    @BindView(R.id.customer_register_address_list_rv_list) RecyclerView rvList;
    @BindView(R.id.customer_register_addres_tv_title_number) TextView txtNumeroTitulo;

    private CompositeDisposable compositeDisposable;

    public static RegisterCustomerAddressListFragment newInstance() {
        return new RegisterCustomerAddressListFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_address_list;
    }

    @Override
    public void initialize() {
        compositeDisposable = new CompositeDisposable();
        presenter.attachView(this);

        rvList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvList.setNestedScrollingEnabled(false);
        rvList.setAdapter(adapter);
        presenter.initializeData();
        rvList.post(() -> rvList.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void persistData() {
        presenter.saveAddress(false);
    }

    @Override
    public void persistCloneData() {
        presenter.saveAddress(true);
    }

    @Override
    public void showError(String error) {
        showOneButtonDialog(getString(R.string.app_name), error, null);
    }

    @Override
    public void stepValidated() {
        parentActivity.stepValidated();
    }

    @Override
    public void stepValidatedBack() {
        parentActivity.stepValidatedBack();
    }

    @Override
    public void fillAddressType(List<CustomerRegisterAddressType> addressType) {
        adapter.setList(addressType);
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onClickItem(CustomerRegisterAddressType addressType) {
        CustomerRegister customerRegister = new CustomerRegister();
        customerRegister.setAddresses(presenter.getAddressList());
        parentActivity.setCustomerRegisterCommercialCache(customerRegister);

        CustomerRegisterAddress address = presenter.getAddress(addressType);
        if (address != null) {
            parentActivity.openFragmentWithoutBottomBar(RegisterCustomerAddressFragment.newInstance(address));
        } else {
            parentActivity.openFragmentWithoutBottomBar(RegisterCustomerAddressFragment.newInstance(addressType));
        }
    }
}
