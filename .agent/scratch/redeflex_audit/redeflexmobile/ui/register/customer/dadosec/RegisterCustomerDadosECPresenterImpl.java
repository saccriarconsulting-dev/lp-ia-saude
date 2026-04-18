package com.axys.redeflexmobile.ui.register.customer.dadosec;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_MCC_DADOSEC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEGMENTO_DADOSEC;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;

public class RegisterCustomerDadosECPresenterImpl extends BasePresenterImpl<RegisterCustomerDadosECView>
        implements RegisterCustomerDadosECPresenter {

    static final int LIMIT_YEAR = 1900;
    private CustomerRegisterManager manager;
    private CustomerRegister customerRegister;

    public RegisterCustomerDadosECPresenterImpl(RegisterCustomerDadosECView view,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    public void initializeData() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        if (parentActivity != null)
            customerRegister = parentActivity.getCustomerRegister();

        if (customerRegister != null) {
            getView().initializeFieldValues(customerRegister);

            List<Completable> requests = new ArrayList<>();
            compositeDisposable.add(Completable.merge(requests)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::initializeFormValues, this::showError));


            // Ajusta o Numero da Sequencia do Titulo
            if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue())
            {
                if (customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                    getView().setNumeroTitulo("3");
                else
                    getView().setNumeroTitulo("2");
            }
        }
    }

    private void initializeFormValues() {
        loadHorarioFuncList();
    }

    private void loadHorarioFuncList() {
        getView().fillHorarioFuncList(customerRegister.getHorarioFunc());
    }

    @Override
    public void getMcc() {
        RegisterCustomerView parentActivityAux = getView().getParentActivity();
        CustomerRegister customerRegisterAux = new CustomerRegister();
        int _typePerson = 1;

        if (parentActivityAux != null)
            customerRegisterAux = parentActivityAux.getCustomerRegister();

        if (customerRegisterAux != null)
            _typePerson = customerRegisterAux.getPersonType().getIdValue();

        List<Completable> request = Arrays.asList(
                manager.getMccList(_typePerson).flatMapCompletable(mcc -> {
                    safeExecute(view -> view.carregaLista_Mcc(new ArrayList<>(mcc)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    @Override
    public void getSegmento() {
        List<Completable> request = Arrays.asList(
                manager.getSegments().flatMapCompletable(segmento -> {
                    safeExecute(view -> view.carregaLista_Segmento(new ArrayList<>(segmento)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    @Override
    public void doSave(CustomerRegister customer) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (customer.getSegment() == null) {
            errors.add(SPN_SEGMENTO_DADOSEC);
        }

        if (customer.getMcc() == null) {
            errors.add(SPN_MCC_DADOSEC);
        }

        if (customer.getHorarioFunc().size() <= 0)
        {
            showError("Obrigatório informar Horário de Funcionamento");
            return;
        }

        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            saveData(customer, false);
        }
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

        if (customer.getSgvCode() != null)
            customerRegister.setSgvCode(customer.getSgvCode());
        customerRegister.setSegment(customer.getSegment());
        customerRegister.setMcc(customer.getMcc());

        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }
}
