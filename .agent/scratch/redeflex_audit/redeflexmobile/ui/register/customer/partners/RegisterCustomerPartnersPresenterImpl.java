package com.axys.redeflexmobile.ui.register.customer.partners;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_CELULAR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_DATANASCIMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SOCIO_NOME;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;

public class RegisterCustomerPartnersPresenterImpl extends BasePresenterImpl<RegisterCustomerPartnersView>
        implements RegisterCustomerPartnersPresenter {

    static final int LIMIT_YEAR = 1900;
    private CustomerRegisterManager manager;
    private CustomerRegister customerRegister;

    public RegisterCustomerPartnersPresenterImpl(RegisterCustomerPartnersView view,
                                                  SchedulerProvider schedulerProvider,
                                                  ExceptionUtils exceptionUtils,
                                                  CustomerRegisterManager manager) {
        super(view, schedulerProvider, exceptionUtils);
        this.manager = manager;
    }

    @Override
    public void getProfissoes() {
        List<Completable> request = Arrays.asList(
                manager.getProfissoes().flatMapCompletable(profissoes -> {
                    safeExecute(view -> view.fillProfissoes(new ArrayList<>(profissoes)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    @Override
    public void getRenda() {
        List<Completable> request = Arrays.asList(
                manager.getRenda().flatMapCompletable(renda -> {
                    safeExecute(view -> view.fillRenda(new ArrayList<>(renda)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    @Override
    public void getPatrimonio() {
        List<Completable> request = Arrays.asList(
                manager.getPatrimonio().flatMapCompletable(patrimonio -> {
                    safeExecute(view -> view.fillPatrimonio(new ArrayList<>(patrimonio)));
                    return Completable.complete();
                }));

        compositeDisposable.add(Completable.concat(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeData, this::showError));
    }

    private void initializeData() {
        safeExecute(view -> {
            RegisterCustomerView parentActivity = view.getParentActivity();
            if (parentActivity != null)
                customerRegister = parentActivity.getCustomerRegister();

            if (customerRegister.getPartners() != null) {
                view.initializeFieldValues(customerRegister.getPartners(), customerRegister.getPartnerName());
            }

            // Ajusta o Numero da Sequencia do Titulo
            if ((customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue()) &&
                    customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue()) {
                view.setNumeroTitulo("2");
            }
        });
    }

    @Override
    public void doSave(CustomerRegisterPartners customer) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (StringUtils.isEmpty(customer.getNome())) {
            errors.add(ET_SOCIO_NOME);
        }

        if (!DateUtils.isDateValid(Util_IO.dateToStringBr(customer.getDataNascimento()), true, LIMIT_YEAR)) {
            errors.add(ET_SOCIO_DATANASCIMENTO);
        }

        if (!StringUtils.isCpfValid(customer.getCPF())) {
            errors.add(ET_SOCIO_CPF);
        }

        if (StringUtils.isEmpty(customer.getEmail())) {
            errors.add(ET_SOCIO_EMAIL);
        } else if (StringUtils.isEmailNotValid(customer.getEmail())) {
            errors.add(ET_SOCIO_EMAIL);
        }

        if (StringUtils.isEmpty(customer.getCelular())) {
            errors.add(ET_SOCIO_CELULAR);
        } else if (!StringUtils.isCellphone(customer.getCelular())) {
            errors.add(ET_SOCIO_CELULAR);
        }

        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            saveData(customer, false);
        }
    }

    @Override
    public void saveData(CustomerRegisterPartners customerRegisterPartners, boolean isBack) {
        CustomerRegister customerRegister;
        customerRegister = this.customerRegister;
        customerRegister.setPartners(customerRegisterPartners);

        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }
}
