package com.axys.redeflexmobile.ui.register.customer.address;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.SUBADQUIRENCIA;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType.COLLECTION;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType.INSTALLATION;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType.MAIN;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rogério Massa on 07/02/19.
 */

public class RegisterCustomerAddressListPresenterImpl extends BasePresenterImpl<RegisterCustomerAddressListView>
        implements RegisterCustomerAddressListPresenter {

    private CustomerRegister customerRegister;
    private List<CustomerRegisterAddressType> addressTypeList;
    private List<CustomerRegisterAddress> addressList;

    public RegisterCustomerAddressListPresenterImpl(RegisterCustomerAddressListView view,
                                                    SchedulerProvider schedulerProvider,
                                                    ExceptionUtils exceptionUtils) {
        super(view, schedulerProvider, exceptionUtils);
        this.addressList = new ArrayList<>();
    }

    @Override
    public List<CustomerRegisterAddress> getAddressList() {
        return addressList;
    }

    @Override
    public void initializeData() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        customerRegister = parentActivity.getCustomerRegister();
        CustomerRegister cachedCustomerRegister = parentActivity.getCustomerRegisterCommercialCache();
        parentActivity.setCustomerRegisterCommercialCache(null);

        this.addressTypeList = ACQUISITION.equals(customerRegister.getCustomerType()) || SUBADQUIRENCIA.equals(customerRegister.getCustomerType())
                //&& JURIDICAL.equals(customerRegister.getPersonType())
                ? CustomerRegisterAddressType.getAcquisitionJuridicalList()
                : CustomerRegisterAddressType.getDefaultList();

        addressList = new ArrayList<>();
        if (cachedCustomerRegister != null) {
            addressList = cachedCustomerRegister.getAddresses();
        } else if (customerRegister.getAddresses() != null && !customerRegister.getAddresses().isEmpty()) {
            addressList = customerRegister.getAddresses();
        }

        loadAddressTypeList();


        // Ajusta o Numero da Sequencia do Titulo
        if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue())
        {
            if (customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                getView().setNumeroTitulo("5");
            else
                getView().setNumeroTitulo("4");
        }
    }

    @Override
    public void loadAddressTypeList() {
        getView().fillAddressType(Stream.of(addressTypeList)
                .map(addressType -> {
                    addressType.setChecked(false);
                    Stream.ofNullable(addressList).forEach(address -> {
                        if (address.getIdAddressType().equals(addressType.getType().getValue())
                                && !addressType.isChecked()
                                && address.isActivate()
                                && address.isValidated()) {
                            addressType.setChecked(true);
                        }
                    });
                    return addressType;
                })
                .toList());
    }

    @Override
    public CustomerRegisterAddress getAddress(CustomerRegisterAddressType addressType) {
        return Stream.ofNullable(addressList)
                .filter(value -> value.getIdAddressType() == addressType.getType().getValue())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeAddress(CustomerRegisterAddress address) {
        for (int i = EMPTY_INT; i < addressList.size(); i++) {
            CustomerRegisterAddress addressSaved = addressList.get(i);
            if (addressSaved.getId() != null
                    && address.getId() != null
                    && addressSaved.getId().equals(address.getId())
                    && addressSaved.getSync() == SINGLE_INT) {

                addressSaved.setActivate(false);
                addressList.set(i, addressSaved);
                break;
            } else if (addressSaved.getIdAddressType().equals(address.getIdAddressType())) {
                addressList.remove(address);
                break;
            }
        }
        loadAddressTypeList();
    }

    @Override
    public void saveAddress(boolean isBack) {

        if (isBack) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            CustomerRegister customerRegisterClone = parentActivity.getCustomerRegisterClone();
            customerRegisterClone.setAddresses(addressList);
            getView().stepValidatedBack();
            return;
        }

        CustomerRegisterAddress main = null;
        CustomerRegisterAddress installation = null;
        CustomerRegisterAddress proprietario = null;
        for (CustomerRegisterAddress address : addressList) {
            if (address.isValidated()) {
                if (address.getIdAddressType() == MAIN.getValue()) {
                    main = address;
                }
                if (address.getIdAddressType().equals(INSTALLATION.getValue())) {
                    installation = address;
                }
                if (address.getIdAddressType().equals(COLLECTION.getValue())) {
                    proprietario = address;
                }
            }
        }

        if (main == null) {
            getView().showError(getView().getStringByResId(R.string.customer_register_address_error_main));
            return;
        }

        //if (ACQUISITION.equals(customerRegister.getCustomerType()) && JURIDICAL.equals(customerRegister.getPersonType()) && installation == null) {
        if (ACQUISITION.equals(customerRegister.getCustomerType()) && installation == null) {
            getView().showError(getView().getStringByResId(R.string.customer_register_address_error_installation));
            return;
        }

        if (ACQUISITION.equals(customerRegister.getCustomerType()) && proprietario == null) {
            getView().showError("Informe o endereço do Proprietário para continuar.");
            return;
        }

        customerRegister.setAddresses(addressList);
        getView().stepValidated();
    }
}
