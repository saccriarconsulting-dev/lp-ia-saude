package com.axys.redeflexmobile.ui.register.customer;

import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.List;

/**
 * @author Bruno Pimentel on 30/11/18.
 */
public class RegisterCustomerPresenterImpl extends BasePresenterImpl<RegisterCustomerView>
        implements RegisterCustomerPresenter {

    private final CustomerRegisterManager customerRegisterManager;
    private final ProspectingClientAcquisitionManager prospectingClientAcquisitionManager;

    private ConsultaReceita consultaReceita;
    private CustomerRegister customerRegister;
    private CustomerRegister customerRegisterClone;

    private CustomerRegister customerRegisterCommercialCache;
    private List<EnumRegisterFields> customerRegisterCommercialErrorsCache;

    public RegisterCustomerPresenterImpl(RegisterCustomerView view,
                                         SchedulerProvider schedulerProvider,
                                         ExceptionUtils exceptionUtils,
                                         CustomerRegisterManager customerRegisterManager,
                                         ProspectingClientAcquisitionManager prospectingClientAcquisitionManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.customerRegister = new CustomerRegister();
        this.customerRegisterClone = new CustomerRegister();
        this.customerRegisterManager = customerRegisterManager;
        this.prospectingClientAcquisitionManager = prospectingClientAcquisitionManager;
    }

    @Override
    public CustomerRegister getCustomerRegister() {
        return customerRegister;
    }

    @Override
    public CustomerRegister getCustomerRegisterClone() {
        return customerRegisterClone;
    }

    @Override
    public ConsultaReceita getConsultaReceita() {
        return consultaReceita;
    }

    @Override
    public void setConsultaReceita(ConsultaReceita consultaReceita) {
        this.consultaReceita = consultaReceita;
    }

    @Override
    public CustomerRegister getCustomerRegisterCommercialCache() {
        return customerRegisterCommercialCache;
    }

    @Override
    public void setCustomerRegisterCommercialCache(CustomerRegister customerRegisterCommercialCache) {
        this.customerRegisterCommercialCache = customerRegisterCommercialCache;
    }

    @Override
    public List<EnumRegisterFields> getCustomerRegisterCommercialErrorsCache() {
        return customerRegisterCommercialErrorsCache;
    }

    @Override
    public void setCustomerRegisterCommercialErrorsCache(List<EnumRegisterFields> customerRegisterCommercialErrorsCache) {
        this.customerRegisterCommercialErrorsCache = customerRegisterCommercialErrorsCache;
    }

    @Override
    public void getCustomerByCustomer(String idCustomer) {
        compositeDisposable.add(customerRegisterManager.getCustomerById(idCustomer)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getCustomerSuccess, this::showError));
    }

    @Override
    public void getProspectValue(int prospectMdcIdExtra) {
        compositeDisposable.add(prospectingClientAcquisitionManager.getById(prospectMdcIdExtra)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(getView()::setProspectObject, this::showError));
    }

    @Override
    public void getCustomerByCustomerRegister(int idCustomer) {
        compositeDisposable.add(customerRegisterManager.getCustomerRegisterById(idCustomer)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getCustomerSuccess, this::showError));
    }

    @Override
    public void getCustomerByProspect(int idProspect) {
        compositeDisposable.add(customerRegisterManager.getProspectById(idProspect)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getCustomerSuccess, this::showError));
    }

    private void getCustomerSuccess(CustomerRegister customerRegister) {
        if (this.customerRegister.getAttachments() != null && !this.customerRegister.getAttachments().isEmpty()) {
            customerRegister.setAttachments(this.customerRegister.getAttachments());
        }
        this.customerRegister = customerRegister;
        this.customerRegisterClone = new CustomerRegister(customerRegister);
        getView().onInitializeCustomerSuccess();
    }

    @Override
    public void saveCustomerRegister() {
        if (!Validacoes.validacaoDataAparelhoAdquirencia(getContext())) {
            return;
        }

        compositeDisposable.add(customerRegisterManager.saveCustomerRegister(customerRegister)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(() -> getView().onSaveSuccess(), this::showError));
    }

    @Override
    public void getCustomerBySolicitacaoPid(int idSolicitacaoPidServer) {
        compositeDisposable.add(customerRegisterManager.getSolicitacaoPid(idSolicitacaoPidServer)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::getCustomerSuccess, this::showError));
    }
}
