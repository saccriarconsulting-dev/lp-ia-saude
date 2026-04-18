package com.axys.redeflexmobile.ui.clientemigracao.register.taxes;

import android.view.View;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterTaxType;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubTaxManager;
import com.axys.redeflexmobile.shared.manager.clientinfo.FlagsBankManager;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.register.customer.commercial.tax.RegisterCustomerCommercialTaxPresenterImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_DEBIT_AUTOMATIC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Lucas Marciano on 07/04/2020
 */
public class RegisterMigrationTaxesPresenterImpl
        extends BasePresenterImpl<RegisterMigrationTaxesView>
        implements RegisterMigrationTaxesPresenter {

    private final FlagsBankManager flagsBankManager;
    private final RegisterMigrationSubTaxManager registerMigrationSubTaxManager;
    private final ClienteManager clienteManager;
    private List<RegisterMigrationSubTax> registerMigrationSubTaxList = new ArrayList<>();
    private final RegisterMigrationSubManager registerMigrationSubManager;
    private final MotiveMigrationSubManager motiveMigrationSubManager;
    private final IGPSTracker gpsTracker;
    private final DBColaborador dbColaborador;
    private List<FlagsBank> flagsBanks;

    public RegisterMigrationTaxesPresenterImpl(RegisterMigrationTaxesView view,
                                               SchedulerProvider schedulerProvider,
                                               ExceptionUtils exceptionUtils,
                                               RegisterMigrationSubTaxManager registerMigrationSubTaxManager,
                                               ClienteManager clienteManager,
                                               RegisterMigrationSubManager registerMigrationSubManager,
                                               MotiveMigrationSubManager motiveMigrationSubManager,
                                               IGPSTracker gpsTracker,
                                               DBColaborador dbColaborador,
                                               FlagsBankManager flagsBankManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.registerMigrationSubTaxManager = registerMigrationSubTaxManager;
        this.clienteManager = clienteManager;
        this.registerMigrationSubManager = registerMigrationSubManager;
        this.motiveMigrationSubManager = motiveMigrationSubManager;
        this.gpsTracker = gpsTracker;
        this.dbColaborador = dbColaborador;
        this.flagsBankManager = flagsBankManager;
    }

   /* @Override
    public void initialize(RegisterMigrationSub registerMigrationSub) {
        if (registerMigrationSub.getTaxesList() != null && registerMigrationSub.getTaxesList().size() > 0) {
            registerMigrationSubTaxList = registerMigrationSub.getTaxesList();
            getView().initializeInterface(registerMigrationSubTaxList.get(0));
        } else {
            registerMigrationSubTaxList = initializeArrayTax();
        }
    }*/

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
            getView().onLoadFlagsBankSuccess(this.flagsBanks);
        }
    }

    @Override
    public void loadCurrentTaxes(RegisterMigrationSub registerMigrationSub) {
        compositeDisposable.add(registerMigrationSubTaxManager.getByClientId(registerMigrationSub.getIdCliente())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(this::setupListTaxMigration, e -> {
                    getView().showDialogNotFoundTaxes();
                    Timber.e(e);
                }));
    }

    @Override
    public void loadNewTaxes(RegisterMigrationSub registerMigrationSub) {
        compositeDisposable.add(clienteManager.obterClientePorId(registerMigrationSub.getIdCliente())
                .flatMap(client -> registerMigrationSubTaxManager.getTaxList(registerMigrationSub, client))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(this::setupListTaxMigrationByTaxMdr, e -> {
                    getView().showDialogNotFoundTaxes();
                    Timber.e(e);
                }));
    }

    @Override
    public void selectTaxOption(int flagType) {
        RegisterMigrationSubTax registerMigrationSubTax = Stream.ofNullable(registerMigrationSubTaxList)
                .filter(tax -> tax.getBandeiraTipoId() == flagType)
                .findFirst()
                .orElse(null);

        if (registerMigrationSubTax != null) {
            if (validateIfAllAreNull(registerMigrationSubTax)) {
                getView().initializeInterface(registerMigrationSubTax);
            } else {
                getView().showDialogNotFoundTaxes();
            }
        } else {
            getView().showDialogNotFoundTaxes();
        }
    }

    @Override
    public void doSave(RegisterMigrationSub registerMigrationSub) {
        List<EnumRegisterFields> errors = new ArrayList<>(validateFields(registerMigrationSub));
        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            finalizeFlow(false);
        }
    }

    private List<EnumRegisterFields> validateFields(RegisterMigrationSub request) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if(registerMigrationSubTaxList.isEmpty()){
            errors.add(EnumRegisterFields.TAX);
        }
        return errors;
    }

    @Override
    public void finalizeFlow(boolean isBack) {
        if (isBack) {
            registerMigrationSubTaxList.clear();
            getView().onValidationSuccessBack(registerMigrationSubTaxList);
        } else {
            getView().saveCurrentState();
            getView().onValidationSuccess(registerMigrationSubTaxList);
        }
    }

    @Override
    public void saveTaxChanged(int flagType, EnumRegisterTaxType taxType, Double value) {
        if (flagType > 0) {
            Stream.ofNullable(registerMigrationSubTaxList).forEach(itemList -> {
                if (itemList.getBandeiraTipoId() == flagType) {
                    switch (taxType) {
                        case DEBIT:
                            itemList.setDebito(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_IN_CASH:
                            itemList.setCreditoAVista(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_UNTIL_SIX:
                            itemList.setCreditoAte6(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case CREDIT_BIGGER_SIX:
                            itemList.setCreditoMaior6(value == null ? EMPTY_DOUBLE : value);
                            break;
                        case ANTICIPATION:
                            itemList.setAntecipacaoAutomatica(value == null ? EMPTY_DOUBLE : value);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void setupListTaxMigrationByTaxMdr(List<TaxaMdr> list) {
        registerMigrationSubTaxList = convertTaxObject(list);
        callViewMountInterface();
    }

    private void setupListTaxMigration(List<RegisterMigrationSubTax> list) {
        registerMigrationSubTaxList = list;
        callViewMountInterface();
    }

    private void callViewMountInterface() {
        if (validateIfAllAreNull(registerMigrationSubTaxList.get(0))) {
            getView().initializeInterface(registerMigrationSubTaxList.get(0));
        } else {
            getView().showDialogNotFoundTaxes();
        }
    }

    private List<RegisterMigrationSubTax> convertTaxObject(List<TaxaMdr> taxes) {
        List<RegisterMigrationSubTax> list = new ArrayList<>();
        for (TaxaMdr tax : taxes) {
            RegisterMigrationSubTax item = new RegisterMigrationSubTax();

            item.setBandeiraTipoId(tax.getFlagType());
            item.setDebito(tax.getTaxDebit());
            item.setCreditoAVista(tax.getTaxCredit());
            item.setCreditoAte6(tax.getTaxUntilSix());
            item.setCreditoMaior6(tax.getTaxBiggerSix());
            item.setAntecipacaoAutomatica(tax.getAnticipation());

            list.add(item);
        }
        return list;
    }

    @Override
    public void loadMotivesCancelMigration() {
        compositeDisposable.add(motiveMigrationSubManager.getAll()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(d -> getView().showLoading())
                .doFinally(getView()::hideLoading)
                .subscribe(getView()::callModalMotivesCancelMigration, Timber::e));
    }

    @Override
    public void saveMigrationCancelMotive(int motiveId, String observation) {
        Colaborador colaborador = dbColaborador.get();
        int customerId = getView().getParentActivity().recoverObjectMigration().getIdCliente();
        compositeDisposable.add(
                clienteManager.obterClientePorId(customerId)
                        .subscribeOn(schedulerProvider.io())
                        .flatMap(cliente -> {
                            RegisterMigrationSub register = new RegisterMigrationSub();
                            register.setIdCliente(Integer.parseInt(cliente.getId()));
                            register.setIdMotivoRecusa(motiveId);
                            register.setObservacaoRecusa(observation);
                            register.setIdMcc(cliente.getIdMcc());
                            register.setTelefoneCelular(cliente.getCelularCompleto().length() > 0 ? cliente.getCelularCompleto() : cliente.getCelularCompleto2());
                            register.setVersaoApp(BuildConfig.VERSION_NAME);
                            register.setLatitude(gpsTracker.getLatitude());
                            register.setLongitude(gpsTracker.getLongitude());
                            register.setPrecisao(gpsTracker.getPrecisao());
                            register.setIdVendedor(colaborador.getId());
                            register.setDataCadastro(Util_IO.dateTimeToString(new Date(), Config.FormatDateTimeStringBanco));

                            return registerMigrationSubManager.create(register);
                        })
                        .observeOn(schedulerProvider.ui())
                        .doOnSubscribe(d -> getView().showLoading())
                        .doFinally(getView()::hideLoading)
                        .subscribe(cliente -> getView().showResponseMotivesCancelMigration(), Timber::e)
        );
    }

    /*private List<RegisterMigrationSubTax> initializeArrayTax() {
        List<RegisterMigrationSubTax> list = new ArrayList<>();
        for (FlagsBank item: this.flagsBanks) {
            list.add(new RegisterMigrationSubTax(item.getId()));
        }
        return list;
    }*/

    private boolean validateIfAllAreNull(RegisterMigrationSubTax registerMigrationSubTax) {
        if (registerMigrationSubTax.getDebito() > EMPTY_DOUBLE)
            return true;
        else if (registerMigrationSubTax.getCreditoMaior6() > EMPTY_DOUBLE)
            return true;
        else if (registerMigrationSubTax.getCreditoAte6() > EMPTY_DOUBLE)
            return true;
        else if (registerMigrationSubTax.getAntecipacaoAutomatica() > EMPTY_DOUBLE)
            return true;
        else return registerMigrationSubTax.getCreditoAVista() > EMPTY_DOUBLE;
    }
}
