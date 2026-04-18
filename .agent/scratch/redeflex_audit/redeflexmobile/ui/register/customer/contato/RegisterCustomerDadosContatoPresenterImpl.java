package com.axys.redeflexmobile.ui.register.customer.contato;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_CELULAR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CONTATOPRINC_NOME;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;

public class RegisterCustomerDadosContatoPresenterImpl extends BasePresenterImpl<RegisterCustomerDadosContatoView>
        implements RegisterCustomerDadosContatoPresenter {

    private CustomerRegisterManager manager;
    private CustomerRegister customerRegister;

    public RegisterCustomerDadosContatoPresenterImpl(RegisterCustomerDadosContatoView view,
                                                     SchedulerProvider schedulerProvider,
                                                     ExceptionUtils exceptionUtils,
                                                     CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void doSave(CustomerRegister customerRegister) {
        List<EnumRegisterFields> errors = new ArrayList<>();

        if ((this.customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || this.customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue()) &&
                this.customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.PHYSICAL.getIdValue()) {
            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getNome())) {
                errors.add(ET_CONTATOPRINC_NOME);
            }

            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getEmail())) {
                errors.add(ET_CONTATOPRINC_EMAIL);
            } else if (StringUtils.isEmailNotValid(customerRegister.getContatoPrincipal().getEmail()))
                errors.add(ET_CONTATOPRINC_EMAIL);

            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getCelular())) {
                errors.add(ET_CONTATOPRINC_CELULAR);
            } else if (!StringUtils.isCellphone(customerRegister.getContatoPrincipal().getCelular()))
                errors.add(ET_CONTATOPRINC_CELULAR);
        } else if (EnumRegisterCustomerType.ELECTRONIC.getIdValue().equals(this.customerRegister.getCustomerType().getIdValue()) || EnumRegisterCustomerType.PHYSICAL.getIdValue().equals(this.customerRegister.getCustomerType().getIdValue())) {
            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getNome())) {
                errors.add(ET_CONTATOPRINC_NOME);
            }

            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getEmail())) {
                errors.add(ET_CONTATOPRINC_EMAIL);
            } else if (StringUtils.isEmailNotValid(customerRegister.getContatoPrincipal().getEmail()))
                errors.add(ET_CONTATOPRINC_EMAIL);

            if (StringUtils.isEmpty(customerRegister.getContatoPrincipal().getCelular())) {
                errors.add(ET_CONTATOPRINC_CELULAR);
            } else if (!StringUtils.isCellphone(customerRegister.getContatoPrincipal().getCelular()))
                errors.add(ET_CONTATOPRINC_CELULAR);
        } else
            errors.clear();

        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            saveData(customerRegister, false);
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

        // Dados os Contatos Referencia
        customerRegister.setContatos(new ArrayList<>());
        customerRegister.setContatos(customer.getContatos());

        // Dados Contato Principal
        customerRegister.setContatoPrincipal(customer.getContatoPrincipal());


        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }

    @Override
    public void initializeData() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        if (parentActivity != null)
            customerRegister = parentActivity.getCustomerRegister();

        if (customerRegister != null) {
            getView().initializeFieldValues(customerRegister);

            // Ajusta o Numero da Sequencia do Titulo
            if ((customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue()) &&
                    customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue()) {
                getView().setNumeroTitulo("4");
                getView().setTitulo("Dados de Contato de Referência");
            } else if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ELECTRONIC.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.PHYSICAL.getIdValue()) {
                getView().setNumeroTitulo("2");
                getView().setTitulo("Dados de Contato");
            } else {
                getView().setNumeroTitulo("3");
                getView().setTitulo("Dados de Contato");
            }

            List<Completable> requests = new ArrayList<>();
            compositeDisposable.add(Completable.merge(requests)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::initializeFormValues, this::showError));
        }
    }

    private void initializeFormValues() {
        loadDadosContatoList();
    }

    private void loadDadosContatoList() {
        getView().fillDadosContatoList(customerRegister.getContatos());
    }
}
