package com.axys.redeflexmobile.ui.register.customer.commercial;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCCARTAOPRESENTE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCECOMMERCE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAIMEDIATA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAPOSTERIOR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCFATURAMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ANTICIPATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_DEBIT_AUTOMATIC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_EXEMPTION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_POS_MODEL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_DEBIT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerCommercialPresenterImpl extends BasePresenterImpl<RegisterCustomerCommercialView>
        implements RegisterCustomerCommercialPresenter {

    private final CustomerRegisterManager customerRegisterManager;
    private CustomerRegister customerRegister;
    private CustomerRegister localCustomerRegister;
    private List<EnumRegisterFields> errorList;

    public RegisterCustomerCommercialPresenterImpl(RegisterCustomerCommercialView view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils,
                                                   CustomerRegisterManager customerRegisterManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.customerRegisterManager = customerRegisterManager;
        this.errorList = new ArrayList<>();
    }

    @Override
    public CustomerRegister getLocalCustomerRegister() {
        return localCustomerRegister;
    }

    @Override
    public void setErrorList(List<EnumRegisterFields> errorList) {
        this.errorList = errorList;
    }

    @Override
    public void initializeData() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        customerRegister = parentActivity.getCustomerRegister();
        CustomerRegister cachedCustomerRegister = parentActivity.getCustomerRegisterCommercialCache();
        parentActivity.setCustomerRegisterCommercialCache(null);
        errorList = parentActivity.getCustomerRegisterCommercialErrorsCache();
        parentActivity.setCustomerRegisterCommercialErrorsCache(null);

        ProspectingClientAcquisition prospect = parentActivity.getProspectObject();
        getView().fillInterfaceWithProspectData(prospect);
        localCustomerRegister = cachedCustomerRegister != null
                ? new CustomerRegister(cachedCustomerRegister)
                : new CustomerRegister(customerRegister);

        List<Completable> requests = new ArrayList<>();
        requests.add(customerRegisterManager.getNegotiationTerms()
                .flatMapCompletable(negotiationTermList -> {
                    getView().fillNegotiation(new ArrayList<>(negotiationTermList), prospect);
                    return Completable.complete();
                }));
        requests.add(customerRegisterManager.getAnticipation(localCustomerRegister.getPersonType())
                .flatMapCompletable(anticipation -> {
                    getView().fillAnticipation(new ArrayList<>(anticipation), prospect);
                    return Completable.complete();
                }));
        getView().fillAnticipationValue(new ArrayList<>(localCustomerRegister.getTaxList()));
        requests.add(customerRegisterManager.getDueList()
                .flatMapCompletable(dueDates -> {
                    getView().fillDueList(new ArrayList<>(dueDates));
                    return Completable.complete();
                }));
        requests.add(customerRegisterManager.getExemptionList()
                .flatMapCompletable(exemptions -> {
                    getView().fillExemption(new ArrayList<>(exemptions));
                    return Completable.complete();
                }));

        // Ajusta o Numero da Sequencia do Titulo
        if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue()) {
            if (customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                getView().setNumeroTitulo("7");
            else
                getView().setNumeroTitulo("6");
        }

        compositeDisposable.add(Completable.merge(requests)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeFormValues, this::showError));
    }

    private void initializeFormValues() {
        getView().initializeFieldValues(localCustomerRegister);
        loadPosList();

        if (errorList != null && !errorList.isEmpty()) {
            initializeWithErrorEnabled(localCustomerRegister);
        }
    }

    private void initializeWithErrorEnabled(CustomerRegister customerRegister) {
        errorList = validateFormValues(customerRegister);
        if (!errorList.isEmpty()) {
            getView().setErrors(errorList);
        }
    }

    @Override
    public void removePos(int position) {
        localCustomerRegister.getPosList().remove(position);
        loadPosList();
    }

    private void loadPosList() {
        getView().fillPosList(localCustomerRegister.getPosList());
        calculatePosList();
    }

    private void calculatePosList() {
        double[] value = new double[]{EMPTY_DOUBLE};
        Stream.ofNullable(localCustomerRegister.getPosList())
                .forEach(machineType -> value[EMPTY_INT] += machineType.getSelectedRentValue());
        getView().fillTotalValue(StringUtils.maskCurrencyDouble(value[EMPTY_INT]));
    }

    @Override
    public void doSave(CustomerRegister customer) {
        errorList = validateFormValues(customer);
        if (errorList.isEmpty()) {
            saveData(customer, false);
        } else {
            getView().setErrors(errorList);
        }
    }

    private List<EnumRegisterFields> validateFormValues(CustomerRegister customerRegister) {
        List<EnumRegisterFields> errorList = new ArrayList<>();
        if (NumberUtils.isEmptyDouble(customerRegister.getForeseenRevenue())) {
            errorList.add(ET_FORESEEN_REVENUE);
        }
        if (customerRegister.getNegotiationTermId() == null) {
            errorList.add(SPN_NEGOTIATION);
        }
        if (customerRegister.getAnticipation() == null) {
            errorList.add(SPN_ANTICIPATION);
        }
        if (customerRegister.getDebitAutomatic() == null) {
            errorList.add(SPN_DEBIT_AUTOMATIC);
        }
        if (Util_IO.isEmptyOrNullList(customerRegister.getTaxList())) {
            errorList.add(TAX_DEBIT);
        }
        if (Util_IO.isEmptyOrNullList(localCustomerRegister.getPosList())) {
            errorList.add(SPN_POS_MODEL);
        }
        //if (StringUtils.isEmpty(customerRegister.getRentalMachineDue())) {
        //    errorList.add(SPN_RENTAL_DUE);
        //}
        if (customerRegister.getExemption() == null) {
            errorList.add(SPN_EXEMPTION);
        }

        if (customerRegister.getPercFaturamento() != null) {
            if (customerRegister.getPercFaturamento() > 100)
                errorList.add(ET_PERCFATURAMENTO);
        }

        if (customerRegister.getPercVendaCartao() != null) {
            if (customerRegister.getPercVendaCartao() > 100)
                errorList.add(ET_PERCCARTAOPRESENTE);
        }

        if (customerRegister.getPercVendaEcommerce() != null) {
            if (customerRegister.getPercVendaEcommerce() > 100)
                errorList.add(ET_PERCECOMMERCE);
        }

        if (customerRegister.getPercEntregaImediata() != null) {
            if (customerRegister.getPercEntregaImediata() > 100)
                errorList.add(ET_PERCENTREGAIMEDIATA);
        }

        if (customerRegister.getPercEntregaPosterior() != null) {
            if (customerRegister.getPercEntregaPosterior() > 100)
                errorList.add(ET_PERCENTREGAPOSTERIOR);
        }

        return errorList;
    }

    @Override
    public void saveData(CustomerRegister customer, boolean isBack) {
        CustomerRegister customerRegister;

        if (isBack) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            customerRegister = parentActivity.getCustomerRegisterClone();
        } else {
            customerRegister = this.customerRegister;
        }

        customerRegister.setForeseenRevenue(customer.getForeseenRevenue());
        customerRegister.setNegotiationTermId(customer.getNegotiationTermId());
        customerRegister.setAnticipation(customer.getAnticipation());
        customerRegister.setAnticipationValue(customer.getAnticipationValue());
        customerRegister.setDebitAutomatic(customer.getDebitAutomatic());
        customerRegister.setTaxList(localCustomerRegister.getTaxList());
        customerRegister.setPosList(localCustomerRegister.getPosList());
        customerRegister.setRentalMachineDue(customer.getRentalMachineDue());
        customerRegister.setExemption(customer.getExemption());
        customerRegister.setObservation(customer.getObservation());
        customerRegister.setFaturamentoBruto(customer.getFaturamentoBruto());
        customerRegister.setPercFaturamento(customer.getPercFaturamento());
        customerRegister.setPercVendaCartao(customer.getPercVendaCartao());
        customerRegister.setPercVendaEcommerce(customer.getPercVendaEcommerce());
        customerRegister.setPercEntregaImediata(customer.getPercEntregaImediata());
        customerRegister.setPercEntregaPosterior(customer.getPercEntregaPosterior());
        customerRegister.setPrazoEntrega(customer.getPrazoEntrega());
        customerRegister.setEntregaPosCompra(customer.getEntregaPosCompra());

        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }

    @Override
    public void cacheValues(CustomerRegister customerRegister) {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        parentActivity.setCustomerRegisterCommercialCache(customerRegister);
        parentActivity.setCustomerRegisterCommercialErrorsCache(errorList);
    }
}
