package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBTipoConta;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.manager.ClienteManager;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.MotiveMigrationSubManager;
import com.axys.redeflexmobile.shared.manager.RegisterMigrationSubManager;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_ACCOUNT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_BANK_AGENCY;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_DATA_FUNDACAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_EMAIL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FIRST_PHONE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_RG;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ACCOUNT_TYPE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_BANK;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEXO;

/**
 * @author Lucas Marciano on 02/04/2020.
 */
public class RegisterMigrationPersonalPresenterImpl extends BasePresenterImpl<RegisterMigrationPersonalView>
        implements RegisterMigrationPersonalPresenter {

    private final CustomerRegisterManager customerRegisterManager;
    private final ClienteManager clienteManager;
    private final DBColaborador dbColaborador;
    private Cliente cliente;
    private DBTipoConta dbTipoConta;
    private final MotiveMigrationSubManager motiveMigrationSubManager;
    private final IGPSTracker gpsTracker;
    private final RegisterMigrationSubManager registerMigrationSubManager;

    public RegisterMigrationPersonalPresenterImpl(RegisterMigrationPersonalView view,
                                                  SchedulerProvider schedulerProvider,
                                                  ExceptionUtils exceptionUtils,
                                                  CustomerRegisterManager customerRegisterManager,
                                                  ClienteManager clienteManager,
                                                  DBColaborador dbColaborador,
                                                  DBTipoConta dbTipoConta,
                                                  MotiveMigrationSubManager motiveMigrationSubManager,
                                                  IGPSTracker gpsTracker,
                                                  RegisterMigrationSubManager registerMigrationSubManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.customerRegisterManager = customerRegisterManager;
        this.clienteManager = clienteManager;
        this.dbColaborador = dbColaborador;
        this.dbTipoConta = dbTipoConta;
        this.motiveMigrationSubManager = motiveMigrationSubManager;
        this.registerMigrationSubManager = registerMigrationSubManager;
        this.gpsTracker = gpsTracker;
    }

    @Override
    public void initializeData(int clientId) {
        if (getView().getParentActivity() != null) {
            compositeDisposable.add(clienteManager.obterClientePorId(clientId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(d -> getView().showLoading())
                    .doFinally(getView()::hideLoading)
                    .subscribe(this::convertToSpinner, Timber::e));

            getView().fillHorarioFuncList(getView().getParentActivity().recoverObjectMigration().getHorarioFuncionamento());
        }
    }

    @Override
    public void loadTypeCount() {
        getView().fillSpinnerTypeCount(new ArrayList<>(dbTipoConta.getAllActive()));
    }

    @Override
    public void saveMigrationCancelMotive(int motiveId, String observation) {
        Colaborador colaborador = dbColaborador.get();
        int customerId = getView().getParentActivity().getClientId();

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

    @Override
    public void loadDataBank() {
        compositeDisposable.add(customerRegisterManager.getBanks()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(banks -> getView().fillSpinnerBank(
                        new ArrayList<>(banks)
                ), Timber::e));
    }

    public void loadProfissoes() {
        compositeDisposable.add(customerRegisterManager.getProfissoes()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(profissoes -> getView().fillProfissoes(
                        new ArrayList<>(profissoes)
                ), Timber::e));
    }

    public void loadSexo() {
        getView().fillSexo(new ArrayList<>(EnumRegisterSexo.getList()));
    }

    public void loadFaixaSalarial() {
        compositeDisposable.add(customerRegisterManager.getRenda()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(renda -> getView().fillRenda(
                        new ArrayList<>(renda)
                ), Timber::e));
    }

    public void loadPatrimonio() {
        compositeDisposable.add(customerRegisterManager.getPatrimonio()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(patrimonio -> getView().fillPatrimonio(
                        new ArrayList<>(patrimonio)
                ), Timber::e));
    }

    @Override
    public void doSave(RegisterMigrationSub request) {
        List<EnumRegisterFields> errors = new ArrayList<>(validateFields(request));
        if (!errors.isEmpty()) {
            getView().setErrors(errors);
        } else {
            finalizeFlow(request, false);
        }
    }

    @Override
    public void finalizeFlow(RegisterMigrationSub request, boolean isBack) {
        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            Colaborador colaborador = dbColaborador.get();
            request.setIdCliente(Integer.parseInt(cliente.getId()));
            request.setIdMcc(cliente.getIdMcc());
            request.setIdVendedor(colaborador.getId());
            request.setVersaoApp(BuildConfig.VERSION_NAME);

            if (cliente.isAntecipacaoAutomatica() != null)
                request.setAntecipacaoAutomatica(cliente.isAntecipacaoAutomatica());

            if (cliente.getPrazoDeNegociacao() != null) {
                request.setIdPrazoNegociacao(Integer.parseInt(cliente.getPrazoDeNegociacao()));
            }
            getView().onValidationSuccess(request);
        }
    }

    @Override
    public void fillMigration(final RegisterMigrationSub recoverMigration) {
        if (recoverMigration != null && StringUtils.isNotEmpty(recoverMigration.getEmail())) {
            getView().fillMigrationFields(recoverMigration);
        }
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

    private List<EnumRegisterFields> validateFields(RegisterMigrationSub request) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (StringUtils.isEmailNotValid(request.getEmail())) {
            errors.add(ET_EMAIL);
        }
        if (!StringUtils.isCellphone(request.getTelefoneCelular())) {
            errors.add(ET_FIRST_PHONE);
        }
        if (!request.getTipoMigracao().equals("ADQ")) {
            if (StringUtils.isEmpty(request.getAgencia())) {
                errors.add(ET_BANK_AGENCY);
            }
            if (StringUtils.isEmpty(request.getConta())) {
                errors.add(ET_BANK_ACCOUNT);
            }
            if (request.getTipoConta() == null) {
                errors.add(SPN_ACCOUNT_TYPE);
            }
            if (request.getIdBanco() == null) {
                errors.add(SPN_BANK);
            }
        }

        if (cliente.getCpf_cnpj().length()<=11) {
            if (StringUtils.isEmpty(request.getRG())) {
                errors.add(ET_RG);
            }
            if (request.getSexo() == null) {
                errors.add(SPN_SEXO);
            }
        }

        if (!DateUtils.isDateValid(Util_IO.dateToStringBr(request.getDataFundacaoPF()), true, 1900)) {
            errors.add(ET_DATA_FUNDACAO);
        }

        return errors;
    }

    private void convertToSpinner(Cliente client) {
        this.cliente = client;
        compositeDisposable.add(customerRegisterManager.getBankById(client.getIdBanco())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(bank -> getView().initializeInterface(client, null, bank),
                        e -> {
                            Timber.e(e);
                            getView().initializeInterface(client, null, null);
                        })
        );
    }
}
