package com.axys.redeflexmobile.ui.register.customer.address.addressregister;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_COMPLEMENT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_NUMBER;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_ADDRESS_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_NEIGHBORHOOD;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_POSTAL_CODE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_CITY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_STATE;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType.INSTALLATION;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType.MAIN;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddress;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAddressType.EnumRegisterAddressType;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerAddressPresenterImpl extends BasePresenterImpl<RegisterCustomerAddressView>
        implements RegisterCustomerAddressPresenter {

    static final int OPERATION_REGISTER = 0;
    static final int OPERATION_UPDATE = 1;

    private final CustomerRegisterManager manager;
    private CustomerRegisterAddress customerRegisterAddress;
    private CustomerRegister customerRegisterCached;
    private int operationType;
    private boolean isSearchingAddress;

    public RegisterCustomerAddressPresenterImpl(RegisterCustomerAddressView view,
                                                SchedulerProvider schedulerProvider,
                                                ExceptionUtils exceptionUtils,
                                                CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    public CustomerRegister getCustomerRegisterCached() {
        return customerRegisterCached;
    }

    @Override
    public void initializeData(CustomerRegisterAddress address, int operationType) {
        this.customerRegisterAddress = address;
        this.operationType = operationType;

        RegisterCustomerView parentActivity = getView().getParentActivity();
        if (customerRegisterAddress == null) {
            getView().showAddressError();
            return;
        }

        if (!MAIN.equals(EnumRegisterAddressType.getEnumByValue(
                customerRegisterAddress.getIdAddressType()))) {
            getView().prepareNonMainAddressLayout();
        }

        List<Completable> requests = Arrays.asList(
                manager.getTipoLogradouro()
                        .flatMapCompletable(logradouros -> {
                            safeExecute(view -> view.fillTipoLogradouro(new ArrayList<>(logradouros)));
                            return Completable.complete();
                        }),
                manager.getStates()
                        .flatMapCompletable(estados -> {
                            safeExecute(view -> view.fillStates(new ArrayList<>(estados)));
                            return Completable.complete();
                        })
        );

        compositeDisposable.add(Completable.concat(requests)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeFormValues,this::showError));
    }

    private void initializeFormValues() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        customerRegisterCached = parentActivity.getCustomerRegisterCommercialCache() != null
                ? parentActivity.getCustomerRegisterCommercialCache()
                : new CustomerRegister();

        ConsultaReceita consultaReceita = parentActivity.getConsultaReceita();
        if (consultaReceita == null
                || operationType != OPERATION_REGISTER
                || (!customerRegisterAddress.getIdAddressType().equals(MAIN.getValue())
                && !customerRegisterAddress.getIdAddressType().equals(INSTALLATION.getValue()))) {
            getView().initializeFieldValues(customerRegisterAddress);

            if (consultaReceita != null && customerRegisterAddress.getIdAddressType().equals(MAIN.getValue())) {
                validateFieldsToDisabled(consultaReceita);
            }
            return;
        }

        CustomerRegisterAddress address = new CustomerRegisterAddress(consultaReceita);
        address.setIdAddressType(customerRegisterAddress.getIdAddressType());
        getView().initializeFieldValues(address);

        if (customerRegisterAddress.getIdAddressType().equals(MAIN.getValue())) {
            validateFieldsToDisabled(consultaReceita);
        }
    }

    private void validateFieldsToDisabled(ConsultaReceita consultaReceita) {
        List<EnumRegisterFields> fieldsToBlock = new ArrayList<>();
        if (StringUtils.isNotEmpty(consultaReceita.getCep())) {
            fieldsToBlock.add(ET_POSTAL_CODE);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getLogradouro())) {
            fieldsToBlock.add(ET_ADDRESS);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getNumero())) {
            fieldsToBlock.add(ET_ADDRESS_NUMBER);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getBairro())) {
            fieldsToBlock.add(ET_NEIGHBORHOOD);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getComplemento())) {
            fieldsToBlock.add(ET_ADDRESS_COMPLEMENT);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getUf())) {
            fieldsToBlock.add(SPN_STATE);
        }
        if (StringUtils.isNotEmpty(consultaReceita.getCidade())) {
            fieldsToBlock.add(SPN_CITY);
        }
        if (!fieldsToBlock.isEmpty()) {
            getView().blockFieldFromSearchData(fieldsToBlock);
        }
    }

    @Override
    public void getAddress(String postalCode) {
        if (isSearchingAddress) {
            return;
        }
        this.isSearchingAddress = true;
        compositeDisposable.add(manager.getAddress(postalCode)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(v -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .doOnError(throwable -> this.isSearchingAddress = false)
                .subscribe(address -> {
                    this.isSearchingAddress = false;
                    getView().fillAddress(address);
                }, Timber::e));
    }

    @Override
    public void doSave(CustomerRegisterAddress address) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (!StringUtils.isCepValid(address.getPostalCode())) {
            errors.add(ET_POSTAL_CODE);
        }
        if (StringUtils.isEmpty(address.getAddressType())) {
            errors.add(ET_ADDRESS_TYPE);
        }
        if (StringUtils.isEmpty(address.getAddressName())) {
            errors.add(ET_ADDRESS);
        }
        if (StringUtils.isEmpty(address.getAddressNumber())) {
            errors.add(ET_ADDRESS_NUMBER);
        }
        if (StringUtils.isEmpty(address.getNeighborhood())) {
            errors.add(ET_NEIGHBORHOOD);
        }
        if (StringUtils.isEmpty(address.getFederalState())) {
            errors.add(SPN_STATE);
        }
        if (StringUtils.isEmpty(address.getCity())) {
            errors.add(SPN_CITY);
        }

        boolean isMainAddress = MAIN.equals(EnumRegisterAddressType
                .getEnumByValue(address.getIdAddressType()));

        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            prepareData(address, isMainAddress);
        }
    }

    @Override
    public void doRemove() {
        if (customerRegisterCached.getAddresses() == null) {
            customerRegisterCached.setAddresses(new ArrayList<>());
        }

        for (CustomerRegisterAddress address : customerRegisterCached.getAddresses()) {
            if (address.getAddressType().equals(customerRegisterAddress.getAddressType())) {
                customerRegisterCached.getAddresses().remove(address);
                getView().onBackPressed();
                break;
            }
        }
    }

    private void prepareData(CustomerRegisterAddress address, boolean isMainAddress) {
        customerRegisterAddress.setPostalCode(address.getPostalCode());
        customerRegisterAddress.setAddressType(address.getAddressType());
        customerRegisterAddress.setAddressName(address.getAddressName());
        customerRegisterAddress.setAddressNumber(address.getAddressNumber());
        customerRegisterAddress.setNeighborhood(address.getNeighborhood());
        customerRegisterAddress.setAddressComplement(address.getAddressComplement());
        customerRegisterAddress.setFederalState(address.getFederalState());
        customerRegisterAddress.setCity(address.getCity());
        customerRegisterAddress.setValidated(true);
        customerRegisterAddress.setContactName(null);
        customerRegisterAddress.setPhoneNumber(null);
        customerRegisterAddress.setCellphone(null);
        customerRegisterAddress.setEmail(null);

        saveData(customerRegisterAddress);
    }

    private void saveData(CustomerRegisterAddress addressLocal) {

        if (customerRegisterCached.getAddresses() == null) {
            customerRegisterCached.setAddresses(new ArrayList<>());
        }

        for (int i = EMPTY_INT; i < customerRegisterCached.getAddresses().size(); i++) {
            CustomerRegisterAddress address = customerRegisterCached.getAddresses().get(i);
            if (addressLocal.getId() != null
                    && address.getId() != null
                    && addressLocal.getId().equals(address.getId())
                    && address.isActivate()
                    && address.getSync() == SINGLE_INT) {
                address.setActivate(false);
                customerRegisterCached.getAddresses().set(i, address);
                customerRegisterCached.getAddresses().add(addressLocal);
                getView().onBackPressed();
                return;
            } else if (addressLocal.getId() != null
                    && address.getId() != null
                    && addressLocal.getId().equals(address.getId())
                    && address.isActivate()) {
                customerRegisterCached.getAddresses().set(i, addressLocal);
                getView().onBackPressed();
                return;
            } else if (address.getId() == null
                    && address.getIdAddressType().equals(addressLocal.getIdAddressType())) {
                customerRegisterCached.getAddresses().set(i, addressLocal);
                getView().onBackPressed();
                return;
            }
        }

        customerRegisterCached.getAddresses().add(addressLocal);
        getView().onBackPressed();
    }
}
