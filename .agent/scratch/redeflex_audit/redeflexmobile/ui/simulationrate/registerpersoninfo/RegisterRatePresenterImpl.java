package com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.PeriodNegotiationManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CNPJ;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FANTASY_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FIRST_PHONE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FULL_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ANTICIPATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_MCC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_PERSON_TYPE;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

public class RegisterRatePresenterImpl extends BasePresenterImpl<RegisterRateView> implements RegisterRatePresenter {

    private final PeriodNegotiationManager periodNegotiationManager;
    private final CustomerRegisterManager customerRegisterManager;
    private final ProspectingClientAcquisitionManager prospectingManager;

    private Colaborador seller;
    private int prospectId = 0;
    private boolean completeRegister = false;

    public RegisterRatePresenterImpl(RegisterRateView view,
                                     SchedulerProvider schedulerProvider,
                                     ExceptionUtils exceptionUtils,
                                     PeriodNegotiationManager periodNegotiationManager,
                                     CustomerRegisterManager customerRegisterManager,
                                     ProspectingClientAcquisitionManager prospectingManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.periodNegotiationManager = periodNegotiationManager;
        this.customerRegisterManager = customerRegisterManager;
        this.prospectingManager = prospectingManager;
    }

    @Override
    public void initializer(int prospectId, boolean completeRegister) {
        this.prospectId = prospectId;
        this.completeRegister = completeRegister;

        compositeDisposable.add(prospectingManager.getCurrentSeller()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(seller -> this.seller = seller, Timber::e));
    }

    @Override
    public void loadViewData() {
        compositeDisposable.add(prospectingManager.getById(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(getView()::fillViews, Timber::e));
    }

    @Override
    public void save(ProspectingClientAcquisition prospect) {
        List<EnumRegisterFields> errors = validateFields(prospect);
        if (errors.size() <= 0) {
            prospect.setIdSeller(seller.getId());
            compositeDisposable.add(prospectingManager.add(prospect)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(disposable -> getView().showLoading())
                    .doFinally(() -> getView().hideLoading())
                    .subscribe(getView()::goToNextScreen, Timber::e));
        } else {
            getView().setErrors(errors);
        }
    }

    @Override
    public void loadDataPersonType() {
        List<EnumRegisterPersonType> list = EnumRegisterPersonType.getList();
        getView().fillSpinnerPersonType(new ArrayList<>(list));
    }

    @Override
    public void loadDataTradingTerms() {
        compositeDisposable.add(periodNegotiationManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(list -> getView().fillSpinnerTradingTerms(new ArrayList<>(list)),
                        Timber::e));
    }

    @Override
    public void loadDataMCC(int personType) {
        compositeDisposable.add(customerRegisterManager.getMccList(personType)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(list -> getView().fillSpinnerMCC(new ArrayList<>(list)),
                        Timber::e));
    }

    @Override
    public void loadDataAnticipation() {
        List<EnumRegisterAnticipation> list = EnumRegisterAnticipation.getEnumList();
        getView().fillSpinnerAnticipation(new ArrayList<>(list));
        getView().disableAnticipation();
    }

    @Override
    public void queryDocumentPersonJuridic(String text) {
        compositeDisposable.add(customerRegisterManager.queryCnpj(text)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(v -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(getView()::setPersonJuridicInformation, Timber::e));
    }

    @Override
    public void rollbackProspect() {
        compositeDisposable.add(prospectingManager.deleteById(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(prospect -> {
                    if (prospect) getView().openListActivity();
                }, Timber::e));
    }

    @Override
    public void loadMccValueByIdMcc(ConsultaReceita consultReceipt) {
        compositeDisposable.add(customerRegisterManager.getAcquisitionTaxFromMcc(consultReceipt.getMcc())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(v -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(getView()::fillMccSpinnerValue, Timber::e));
    }

    @Override
    public void validateProspect() {
        String document = getView().getDocumentValue();
        Disposable disposable = customerRegisterManager.verifyRegister(
                StringUtils.returnOnlyNumbers(document),
                EnumRegisterCustomerType.ACQUISITION.getDescriptionValue()
        ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(exists -> {
                    if (exists) {
                        getView().showOneButtonDialog(
                                getView().getStringByResId(R.string.app_name),
                                getString(R.string.register_already_exists),
                                null
                        );

                        return;
                    }

                    getView().checkTaxes();

                }, Timber::e);

        compositeDisposable.add(disposable);
    }

    private List<EnumRegisterFields> validateFields(ProspectingClientAcquisition prospect) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (prospect.getPersonType() == null || prospect.getPersonType().trim().length() <= 0) {
            errors.add(SPN_PERSON_TYPE);
            errors.add(ET_CPF);
        }

        if (prospect.getPersonType() != null) {
            if (prospect.isPhysical()) {
                if (!StringUtils.isCpfValid(prospect.getCpfCnpjNumber())) errors.add(ET_CPF);
            } else {
                if (!StringUtils.isCnpjValid(prospect.getCpfCnpjNumber())) errors.add(ET_CNPJ);
            }
        }

        if (prospect.getIdMcc() == null) errors.add(SPN_MCC);
        if (prospect.getEstimatedAverageBilling() <= EMPTY_DOUBLE) errors.add(ET_FORESEEN_REVENUE);
        if (prospect.getIdTradingTerm() == null) errors.add(SPN_NEGOTIATION);
        if (prospect.isAnticipation() == null) errors.add(SPN_ANTICIPATION);
        if (prospect.getEmail().trim().length() > 0) {
            if (StringUtils.isEmailNotValid(prospect.getEmail())) errors.add(ET_EMAIL);
        }
        if (completeRegister) {
            if (prospect.getCompleteName().trim().length() <= 0) errors.add(ET_FULL_NAME);
            if (prospect.getFantasyName().trim().length() <= 0) errors.add(ET_FANTASY_NAME);
            if (prospect.getPhone().trim().length() <= 0) errors.add(ET_FIRST_PHONE);
            if (prospect.getEmail().trim().length() <= 0) errors.add(ET_EMAIL);
        }

        return errors;
    }
}
