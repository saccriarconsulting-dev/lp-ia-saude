package com.axys.redeflexmobile.ui.simulationrate.registertaxes;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.flagsbank.LoadFlagsBankEvent;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.manager.registerrate.RegistrationSimulationFeeManager;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;

/**
 * @author Lucas Marciano on 28/04/2020
 */
public class RegisterTaxesPresenterImpl extends BasePresenterImpl<RegisterTaxesView>
        implements RegisterTaxesPresenter, LoadFlagsBankEvent {

    private final ProspectingClientAcquisitionManager prospectManager;
    private final RegistrationSimulationFeeManager feeManager;
    private final CustomerRegisterManager customerRegisterManager;
    private final FlagsBankManager flagsBankManager;
    private List<FlagsBank> flagsBanks;

    private int prospectId;
    private ProspectingClientAcquisition prospect;

    private List<RegistrationSimulationFee> listSimulatedFee = new ArrayList<>();

    public RegisterTaxesPresenterImpl(RegisterTaxesView view,
                                      SchedulerProvider schedulerProvider,
                                      ExceptionUtils exceptionUtils,
                                      ProspectingClientAcquisitionManager prospectManager,
                                      RegistrationSimulationFeeManager feeManager,
                                      CustomerRegisterManager customerRegisterManager,
                                      FlagsBankManager flagsBankManager
    ) {
        super(view, schedulerProvider, exceptionUtils);
        this.prospectManager = prospectManager;
        this.feeManager = feeManager;
        this.customerRegisterManager = customerRegisterManager;
        this.flagsBankManager = flagsBankManager;
    }

    @Override
    public void initializer(int prospectId) {
        this.prospectId = prospectId;
        compositeDisposable.add(prospectManager.getById(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(prospect -> {
                    this.prospect = prospect;
                    getView().updateInterfaceVisibility(prospect);
                    this.loadPreSavedTaxes();
                }, Timber::e));
    }

    @Override
    public void loadTaxes() {
        if (prospect != null) {
            EnumRegisterPersonType personType = EnumRegisterPersonType.getEnumByCharValue(prospect.getPersonType());

            compositeDisposable.add(feeManager.loadTaxes(personType.getIdValue(),
                    prospect.getEstimatedAverageBilling(),
                    prospect.getIdTradingTerm(),
                    prospect.getIdMcc())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(disposable -> getView().showLoading())
                    .doFinally(() -> getView().hideLoading())
                    .subscribe(response -> {
                        listSimulatedFee = response;
                        getView().fillInterface(listSimulatedFee.get(0));
                    }, error -> getView().showDialogNotFoundTaxes()));
        }
    }

    @Override
    public void loadPreSavedTaxes() {
        compositeDisposable.add(feeManager.getByProspectId(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(response -> {
                    if (response.size() > 0) {
                        this.listSimulatedFee = response;
                        getView().fillInterface(listSimulatedFee.get(0));
                    } else {
                        this.loadTaxes();
                    }
                }, Timber::e));
    }

    @Override
    public void rollbackProspect() {
        compositeDisposable.add(prospectManager.deleteById(prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(prospect -> {
                    if (prospect) getView().endProspectProcess(true);
                }, Timber::e));
    }

    @Override
    public void selectTaxOption(int flagId) {
        RegistrationSimulationFee filteredTax = Stream.ofNullable(listSimulatedFee)
                .filter(tax -> tax.getIdFlag() == flagId)
                .findFirst()
                .orElse(null);

        if (filteredTax != null) {
            if (validateIfAllOfNull(filteredTax)) {
                getView().fillInterface(filteredTax);
            } else {
                getView().showDialogNotFoundTaxes();
            }
        } else {
            getView().showDialogNotFoundTaxes();
        }
    }

    @Override
    public void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value) {
        if (flagType > 0) {
            Stream.ofNullable(listSimulatedFee).forEach(itemList -> {
                if (itemList.getIdFlag() == flagType) {
                    switch (taxType) {
                        case DEBIT:
                            itemList.setDebitValue(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_IN_CASH:
                            itemList.setCreditValue(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_UNTIL_SIX:
                            itemList.setCreditSixValue(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_BIGGER_SIX:
                            itemList.setCreditMoreThanSixValue(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case ANTICIPATION:
                            itemList.setAutomaticAnticipation(value == null ? EMPTY_DOUBLE : value);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void loadGenerateProposalFunction() {
        saveTaxesToProspect();
    }

    @Override
    public void loadProspectFunction() {
        saveTaxes();
    }

    @Override
    public void saveTaxes() {
        compositeDisposable.add(feeManager.addAll(listSimulatedFee, prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(response -> {
                    if (checkPersonalInformation())
                        getView().openDialogToCheckPersonalInfo();
                    else
                        getView().endProspectProcess(false);
                }, Timber::e));
    }

    @Override
    public void saveTaxesToProspect() {
        compositeDisposable.add(feeManager.addAll(listSimulatedFee, prospectId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(response -> {
                    if (checkPersonalInformation())
                        getView().openDialogToCheckPersonalInfo();
                    else
                        getView().validateProspect();
                }, Timber::e));
    }

    @Override
    public boolean checkAnticipation() {
        return prospect != null ? prospect.isAnticipation() : false;
    }

    @Override
    public void validateProspect() {
        String document = prospect.getCpfCnpjNumber();
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

                    getView().goToGenerateNewProspect();

                }, Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadFlagsBank() {
        compositeDisposable.add(flagsBankManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::initializeFlagsBankSuccess,
                        throwable -> getView().onLoadFlagsBankError()
                ));
    }

    private void initializeFlagsBankSuccess(List<FlagsBank> list) {
        if(list.size() == 0){
            getView().onLoadFlagsBankEmpty();
        }else {
            this.flagsBanks = list;
            initializeListOfTaxes();
            getView().onLoadFlagsBankSuccess(this.flagsBanks);
        }
    }

    private void initializeListOfTaxes() {
        for (FlagsBank item: this.flagsBanks) {
            listSimulatedFee.add(new RegistrationSimulationFee(item.getId()));
        }
    }

    private boolean checkPersonalInformation() {
        if (prospect.getEmail().length() <= 0) return true;
        if (prospect.getPhone().length() <= 0) return true;
        if (prospect.getFantasyName().length() <= 0) return true;
        return prospect.getCompleteName().length() <= 0;
    }

    private boolean validateIfAllOfNull(RegistrationSimulationFee taxes) {
        if (taxes.getDebitValue() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getCreditValue() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getCreditSixValue() > EMPTY_DOUBLE)
            return true;
        else if (taxes.getAutomaticAnticipation() > EMPTY_DOUBLE)
            return true;
        else return taxes.getCreditMoreThanSixValue() > EMPTY_DOUBLE;
    }
}
