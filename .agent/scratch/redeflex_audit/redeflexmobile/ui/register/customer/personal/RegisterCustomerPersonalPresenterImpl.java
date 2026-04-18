package com.axys.redeflexmobile.ui.register.customer.personal;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.SUBADQUIRENCIA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF_INSERTED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_DATANASCCRITERIO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_DATA_FUNDACAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_EMPRESA_BAIXADA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FANTASY_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FULL_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_RG;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_SEGMENT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_STARTING_DATE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEXO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.JURIDICAL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;

import android.util.Log;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.manager.CustomerRegisterManager;
import com.axys.redeflexmobile.shared.manager.registerrate.ProspectingClientAcquisitionManager;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.mvp.BaseView;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * @author Bruno Pimentel on 26/11/18.
 */
public class RegisterCustomerPersonalPresenterImpl extends BasePresenterImpl<RegisterCustomerPersonalView>
        implements RegisterCustomerPersonalPresenter {

    static final int LIMIT_YEAR = 1900;
    static final boolean DEFAULT_BOOLEAN_RESPONSE = true;
    private final CustomerRegisterManager customerRegisterManager;
    private CustomerRegister customerRegister;
    private boolean verifyDocumentBlock, verificaCadastro;
    private boolean documentInvalidated;
    private boolean empresaBaixada;
    private final ProspectingClientAcquisitionManager prospectingClientAcquisitionManager;
    private ProspectingClientAcquisition prospecting = null;
    private List<TaxaMdr> listTaxes = new ArrayList<>();

    public RegisterCustomerPersonalPresenterImpl(RegisterCustomerPersonalView view,
                                                 SchedulerProvider schedulerProvider,
                                                 ExceptionUtils exceptionUtils,
                                                 CustomerRegisterManager customerRegisterManager,
                                                 ProspectingClientAcquisitionManager prospectingClientAcquisitionManager) {
        super(view, schedulerProvider, exceptionUtils);
        this.customerRegisterManager = customerRegisterManager;
        this.prospectingClientAcquisitionManager = prospectingClientAcquisitionManager;
    }

    @Override
    public void initializeData() {
        getView().fillClientType(new ArrayList<>(EnumRegisterCustomerType.getList()));
        getView().fillPersonType(new ArrayList<>(EnumRegisterPersonType.getList()));
        getView().fillSexo(new ArrayList<>(EnumRegisterSexo.getList()));

        RegisterCustomerView parentActivity = getView().getParentActivity();
        customerRegister = parentActivity.getCustomerRegister();
        int prospectId = parentActivity.getProspectingClientAcquisitionId();

        List<Completable> requests = Arrays.asList(
                customerRegisterManager.getMccList(customerRegister.getPersonType().getIdValue())
                        .flatMapCompletable(tax -> {
                            this.listTaxes = tax;
                            safeExecute(view -> view.fillMccList(new ArrayList<>(tax)));
                            return Completable.complete();
                        }),
                customerRegisterManager.getSegments()
                        .flatMapCompletable(segments -> {
                            safeExecute(view -> view.fillSegmentList(new ArrayList<>(segments)));
                            return Completable.complete();
                        }),
                customerRegisterManager.getProfissoes()
                        .flatMapCompletable(profissoes -> {
                            safeExecute(view -> view.fillProfissoes(new ArrayList<>(profissoes)));
                            return Completable.complete();
                        }),
                customerRegisterManager.getRenda()
                        .flatMapCompletable(rendas -> {
                            safeExecute(view -> view.fillRenda(new ArrayList<>(rendas)));
                            return Completable.complete();
                        }),
                customerRegisterManager.getPatrimonio()
                        .flatMapCompletable(patrimonios -> {
                            safeExecute(view -> view.fillPatrimonio(new ArrayList<>(patrimonios)));
                            return Completable.complete();
                        })
        );

        compositeDisposable.add(Completable.concat(requests)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        () -> safeExecute(view -> view.initializeSpinnersSelection(customerRegister)),
                        this::showError
                ));

        if (prospectId > 0) {
            compositeDisposable.add(prospectingClientAcquisitionManager.getById(prospectId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(item -> {
                                if (item != null && customerRegister != null) {
                                    this.prospecting = item;
                                    EnumRegisterPersonType personType = item.isPhysical() ? PHYSICAL : JURIDICAL;
                                    boolean isPhysical = PHYSICAL.getCharValue().equals(item.getPersonType());

                                    if (customerRegister.getCpfCnpj() == null) {
                                        customerRegister.setPersonType(personType);
                                        customerRegister.setCpfCnpj(prospecting.getCpfCnpjNumber());
                                    }
                                    ICustomSpinnerDialogModel mcc = findItem(item.getIdMcc());
                                    getView().fillInterfaceWithProspectData(item, personType, isPhysical, mcc);
                                    getView().initializeFieldValues(customerRegister);
                                    if (!isPhysical && parentActivity.getProspectModalIsShowed()) {
                                        parentActivity.setProspectModalIsShowed(false);
                                        getView().showDialogToFillTheJuridicalInformation(prospecting);
                                    }
                                }
                            }, this::showError
                    ));
        }
    }

    @Override
    public void loadMccList(int personType) {
        compositeDisposable.add(customerRegisterManager.getMccList(personType)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        tax -> safeExecute(view -> view.fillMccList(new ArrayList<>(tax))),
                        this::showError
                ));
    }

    @Override
    public void clearAndChangeFields(ICustomSpinnerDialogModel selectedCustomerType,
                                     ICustomSpinnerDialogModel selectedPersonType,
                                     boolean clearValues) {

        boolean acquisition = selectedCustomerType == null
                || ACQUISITION.getIdValue().equals(selectedCustomerType.getIdValue())
                || SUBADQUIRENCIA.getIdValue().equals(selectedCustomerType.getIdValue());
        boolean physical = selectedPersonType == null
                || PHYSICAL.getIdValue().equals(selectedPersonType.getIdValue());

        if (acquisition && physical) {
            getView().makeAcquisitionPhysicalLayout();
        } else if (acquisition) {
            getView().makeAcquisitionJuridicalLayout();
        } else if (physical) {
            getView().makeNonAcquisitionPhysicalLayout();
        } else {
            getView().makeNonAcquisitionJuridicalLayout();
        }

        if (clearValues) {
            clearPersonalFields();
        }
        getView().initializeFieldValues(customerRegister);
    }


    public void verifyRegisterCPF(String pCPF, String pDataNasc, ICustomSpinnerDialogModel selectedPersonType, ICustomSpinnerDialogModel customerType) {
        if (Util_IO.isNullOrEmpty(pCPF)) {
            showError("Informe o CPF");
            return;
        }

        if (verifyDocumentBlock) {
            return;
        }

        if (verificaCadastro) {
            return;
        }

        if (customerType == null) {
            showError(getString(R.string.customer_register_personal_error_customer_type));
            return;
        }

        verifyDocumentBlock = true;
        compositeDisposable.add(customerRegisterManager.verifyRegister(pCPF, customerType.getDescriptionValue())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(exists -> {
                    documentInvalidated = false;
                    verifyDocumentBlock = false;
                    if (exists) {
                        documentInvalidated = true;
                        showError(getString(R.string.register_already_exists));
                        return;
                    }

                    verificaCadastro = true;
                    compositeDisposable.add(customerRegisterManager.getDadosCliente(pCPF)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .doOnSubscribe(dispose -> getView().showLoading())
                            .doFinally(() -> getView().hideLoading())
                            .subscribe(cadastro -> {
                                verificaCadastro = false;
                                if (cadastro.getIdServer() != null)
                                {
                                    if (cadastro.getPosList() != null || cadastro.getPosList().size() > 0) {
                                        showError("Cliente já possui cadastro na base CPF: " + pCPF + " Proposta: " + cadastro.getIdServer());
                                        return;
                                    }
                                    else
                                    {
                                        customerRegister = cadastro;
                                        getView().initializeFieldValues(customerRegister);
                                        return;
                                    }
                                }
                                else
                                {
                                    if (Util_IO.isNullOrEmpty(pDataNasc)) {
                                        showError("Informe a Data de Nascimento");
                                        return;
                                    }
                                }
                            }, Erro -> {
                                verificaCadastro = false;
                                if (Util_IO.isNullOrEmpty(pDataNasc)) {
                                    showError("Informe a Data de Nascimento");
                                    return;
                                }

                                boolean physical = selectedPersonType == null || PHYSICAL.getIdValue().equals(selectedPersonType.getIdValue());
                                if (StringUtils.isCpfValid(pCPF) && physical) {
                                    String[] arrayData = pDataNasc.toString().split("/");
                                    String pDataFormatada = "";
                                    if (arrayData.length > 0)
                                        pDataFormatada = arrayData[2] + "-" + arrayData[1] + "-" + arrayData[0];

                                    queryCPF(pCPF, pDataFormatada, DEFAULT_BOOLEAN_RESPONSE);
                                }
                            })
                    );
                }, this::showError));

    }

    @Override
    public void queryCPF(String cpf, String pDataNasc, boolean dialogResponse) {
        try {
            compositeDisposable.add(customerRegisterManager.getPessoaFisica(cpf, pDataNasc)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSubscribe(v -> getView().showLoading())
                    .doFinally(() -> getView().hideLoading())
                    .subscribe(
                            consultaPessoaFisica -> safeExecute(view -> setCPFQueryFields(consultaPessoaFisica, dialogResponse)),
                            throwable -> {
                                setCPFQueryFields(null, DEFAULT_BOOLEAN_RESPONSE);
                                showError(throwable);
                            }
                    ));
        } catch (Exception ex) {
            Log.d("Roni", "queryCPF: " + ex.getMessage());
        }
    }

    public void getCadastrCliente(String pCpfCNPJ) {
        compositeDisposable.add(customerRegisterManager.getDadosCliente(pCpfCNPJ)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(dispose -> getView().showLoading())
                .doAfterTerminate(() -> getView().hideLoading())
                .subscribe(this::carregaCustomer, this::showError)
        );
    }

    private void carregaCustomer(CustomerRegister retorno) {
        if (retorno == null) return;

        Log.d("Roni", "carregaCustomer: " + retorno.getFullName());

    }

    private void setCPFQueryFields(ConsultaPessoaFisica query, boolean responseDialog) {
        if (query == null) return;

        if (query.getRetornoErro() != null) {
            if (Util_IO.isNullOrEmpty(query.getRetornoErro().getErroMensagem())) {
                showError(query.getRetornoErro().getErroMensagem());
                return;
            }
        }

        if (query.getRetornoDados() != null)
            getView().onQueryCpfSuccess(query);

    }

    @Override
    public void verifyRegister(String text,
                               ICustomSpinnerDialogModel selectedPersonType,
                               ICustomSpinnerDialogModel customerType) {
        if (verifyDocumentBlock) {
            return;
        }

        if (verificaCadastro) {
            return;
        }

        if (customerType == null) {
            showError(getString(R.string.customer_register_personal_error_customer_type));
            return;
        }

        verifyDocumentBlock = true;
        compositeDisposable.add(customerRegisterManager.verifyRegister(text, customerType.getDescriptionValue())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(exists -> {
                    documentInvalidated = false;
                    verifyDocumentBlock = false;
                    if (exists) {
                        documentInvalidated = true;
                        showError(getString(R.string.register_already_exists));
                        return;
                    }

                    verificaCadastro = true;
                    compositeDisposable.add(customerRegisterManager.getDadosCliente(text)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .doOnSubscribe(dispose -> getView().showLoading())
                            .doFinally(() -> getView().hideLoading())
                            .subscribe(cadastro -> {
                                verificaCadastro = false;
                                if (cadastro.getIdServer() != null)
                                {
                                    if (cadastro.getPosList() != null || cadastro.getPosList().size() > 0) {
                                        showError("Cliente já possui cadastro na base CNPJ: " + text + " Proposta: " + cadastro.getIdServer());
                                        return;
                                    }
                                    else
                                    {
                                        customerRegister = cadastro;
                                        getView().initializeFieldValues(customerRegister);
                                        return;
                                    }
                                }
                                else
                                {
                                    boolean physical = selectedPersonType == null
                                            || PHYSICAL.getIdValue().equals(selectedPersonType.getIdValue());
                                    if (StringUtils.isCnpjValid(text) && !physical) {
                                        queryCnpj(text, DEFAULT_BOOLEAN_RESPONSE);
                                    }
                                }
                            }, Erro -> {
                                verificaCadastro = false;
                                if (customerRegister.getIdServer() != null)
                                    return;
                                else {
                                    boolean physical = selectedPersonType == null
                                            || PHYSICAL.getIdValue().equals(selectedPersonType.getIdValue());
                                    if (StringUtils.isCnpjValid(text) && !physical) {
                                        queryCnpj(text, DEFAULT_BOOLEAN_RESPONSE);
                                    }
                                }
                            })
                    );
                }, this::showError));
    }

    @Override
    public void queryCnpj(String cnpj, boolean dialogResponse) {
        compositeDisposable.add(customerRegisterManager.queryCnpj(cnpj)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(v -> getView().showLoading())
                .doFinally(() -> getView().hideLoading())
                .subscribe(
                        consultaReceita -> safeExecute(view -> setCnpjQueryFields(consultaReceita, dialogResponse)),
                        throwable -> {
                            setCnpjQueryFields(null, DEFAULT_BOOLEAN_RESPONSE);
                            showError(throwable);
                        }
                ));
    }

    private void setCnpjQueryFields(ConsultaReceita query, boolean responseDialog) {
        if (query == null) return;

        // Não permite fazer Cadastro de Empresa Juridica caso esta esteja Baixada na Receita
        empresaBaixada = false;
        if (!Util_IO.isNullOrEmpty(query.getSituacao())) {
            if (!query.getSituacao().equals("ATIVA")) {
                empresaBaixada = true;
                showError("Documento não pode ser cadastrado.\nEmpresa " + query.getSituacao() + " na receita federal!");
                return;
            }
        }

        RegisterCustomerView parentActivity = getView().getParentActivity();
        parentActivity.setConsultaReceita(query);

        if (responseDialog) {
            getView().onQueryCnpjSuccess(query);
        } else {
            getView().onQueryCnpjResponseNoDialog(query);
        }
        if (StringUtils.isEmpty(query.getMcc())) {
            getView().hideLoading();
        } else {
            compositeDisposable.add(customerRegisterManager.getAcquisitionTaxFromMcc(query.getMcc())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doAfterTerminate(() -> safeExecute(BaseView::hideLoading))
                    .subscribe(
                            mcc -> safeExecute(view -> view.fillMccFromConsultaReceita(mcc)),
                            throwable -> {
                                Timber.e(throwable);
                                getView().fillMccFromConsultaReceita(null);
                            }
                    ));
        }
    }

    @Override
    public void doSave(CustomerRegister request) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        boolean acquisition = ACQUISITION.equals(request.getCustomerType()) || SUBADQUIRENCIA.equals(request.getCustomerType());
        boolean physical = PHYSICAL.equals(request.getPersonType());

        if (acquisition) {
            errors.addAll(validateAcquisition(request, physical));
        } else if (physical) {
            errors.addAll(validateNonAcquisitionPhysical(request));
        } else {
            errors.addAll(validateNonAcquisitionJuridical(request));
        }

        if (errors.contains(ET_CPF_INSERTED)) {
            showError(getString(R.string.register_already_exists));
        }
        // Não permite fazer Cadastro de Empresa Juridica caso esta esteja Baixada na Receita
        else if (errors.contains(ET_EMPRESA_BAIXADA)) {
            showError("Documento não pode ser cadastrado.\nEmpresa BAIXADA na receita federal!");
        } else if (errors.contains(ET_DATANASCCRITERIO)) {
            showError("Data de Nascimento não atende os critérios internos da empresa!");
        } else if (errors.isEmpty()) {
            saveData(request, false);
        } else {
            getView().setErrors(errors);
        }
    }

    private List<EnumRegisterFields> validateAcquisition(CustomerRegister request, boolean isPhysical) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (StringUtils.isEmpty(request.getFullName())) {
            errors.add(ET_FULL_NAME);
        }
        if (StringUtils.isEmpty(request.getFantasyName())) {
            errors.add(ET_FANTASY_NAME);
        }

        if (!isPhysical && !StringUtils.isCnpjValid(request.getCpfCnpj())) {
            errors.add(ET_CPF);
        } else if (isPhysical && !StringUtils.isCpfValid(request.getCpfCnpj())) {
            errors.add(ET_CPF);
        } else if (documentInvalidated) {
            errors.add(ET_CPF_INSERTED);
        } else if (empresaBaixada) {
            errors.add(ET_EMPRESA_BAIXADA);
        }

        // Validação Data Inicio da Empresa
        if (isPhysical && !DateUtils.isDateValid(Util_IO.dateToStringBr(request.getDataFundacaoPF()), true, LIMIT_YEAR)) {
            errors.add(ET_DATA_FUNDACAO);
        }

        // Validação Data de Nascimento
        if (isPhysical && !DateUtils.isDateValid(Util_IO.dateToStringBr(request.getBirthdate()), true, LIMIT_YEAR)) {
            errors.add(ET_STARTING_DATE);
        } else if (isPhysical && DateUtils.isDateValid(Util_IO.dateToStringBr(request.getBirthdate()), true, LIMIT_YEAR)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataHoje = null;
                dataHoje = dateFormat.parse(dateFormat.format(new Date()));

                long idade = (Utilidades.diferencaDatas(dataHoje, request.getBirthdate(), TimeUnit.DAYS) / 365);

                // Idade >= 90 ou Idade < 16 --> Mensagem "Data não atende os critérios internos da empresa"
                // Idade < 90 e Idade >= 18  --> Liberado o cadastro
                // Idade >= 16 e Idade < 18  -->  Obrigatório anexar Documento de Emancipação
                if (idade >= 90 || idade < 16)
                    errors.add(ET_DATANASCCRITERIO);

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("Redeflex", "Erro validateAcquisition: " + e.getMessage());
            }
        }

        /*if (request.getSegment() == null) {
            errors.add(ET_SEGMENT);
        }
        if (request.getMcc() == null) {
            errors.add(SPN_MCC);
        }
        if (!isPhysical && StringUtils.isEmpty(request.getPartnerName())) {
            errors.add(ET_PARTNER_FULL_NAME);
        }
        if (!isPhysical && !DateUtils.isDateValid(Util_IO.dateToStringBr(request.getPartnerBirthday()), true, LIMIT_YEAR)) {
            errors.add(ET_PARTNER_BIRTHDAY);
        }
        if (!isPhysical && !StringUtils.isCpfValid(request.getPartnerCpf())) {
            errors.add(ET_PARTNER_CPF);
        }*/

        if (isPhysical) {
            if (StringUtils.isEmpty(request.getRgIe())) {
                errors.add(ET_RG);
            }

            if (request.getSexo() == null) {
                errors.add(SPN_SEXO);
            }
        }

        return errors;
    }

    private List<EnumRegisterFields> validateNonAcquisitionPhysical(CustomerRegister request) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (StringUtils.isEmpty(request.getFullName())) {
            errors.add(ET_FULL_NAME);
        }
        if (StringUtils.isEmpty(request.getFantasyName())) {
            errors.add(ET_FANTASY_NAME);
        }
        if (!StringUtils.isCpfValid(request.getCpfCnpj())) {
            errors.add(ET_CPF);
        } else if (documentInvalidated) {
            errors.add(ET_CPF_INSERTED);
        }
        if (StringUtils.isEmpty(request.getRgIe())) {
            errors.add(ET_RG);
        }
        if (!DateUtils.isDateValid(Util_IO.dateToStringBr(request.getBirthdate()), true, LIMIT_YEAR)) {
            errors.add(ET_STARTING_DATE);
        }
        if (request.getSegment() == null) {
            errors.add(ET_SEGMENT);
        }
        return errors;
    }

    private List<EnumRegisterFields> validateNonAcquisitionJuridical(CustomerRegister request) {
        List<EnumRegisterFields> errors = new ArrayList<>();
        if (StringUtils.isEmpty(request.getFullName())) {
            errors.add(ET_FULL_NAME);
        }
        if (StringUtils.isEmpty(request.getFantasyName())) {
            errors.add(ET_FANTASY_NAME);
        }
        if (!StringUtils.isCnpjValid(request.getCpfCnpj())) {
            errors.add(ET_CPF);
        } else if (documentInvalidated) {
            errors.add(ET_CPF_INSERTED);
        } else if (empresaBaixada) {
            errors.add(ET_EMPRESA_BAIXADA);
        }
        if (!DateUtils.isDateValid(Util_IO.dateToStringBr(request.getBirthdate()), true, LIMIT_YEAR)) {
            errors.add(ET_STARTING_DATE);
        }
        if (request.getSegment() == null) {
            errors.add(ET_SEGMENT);
        }
        return errors;
    }

    @Override
    public void saveData(CustomerRegister request, boolean isBack) {
        clearSavedFields(request.getCustomerType(), request.getPersonType());

        boolean acquisition = ACQUISITION.equals(request.getCustomerType()) || SUBADQUIRENCIA.equals(request.getCustomerType());
        boolean physical = PHYSICAL.equals(request.getPersonType());

        CustomerRegister customerRegister;
        if (isBack) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            customerRegister = parentActivity.getCustomerRegisterClone();
        } else {
            customerRegister = this.customerRegister;
        }

        customerRegister.setCustomerType(request.getCustomerType());
        customerRegister.setPersonType(request.getPersonType());
        customerRegister.setFullName(request.getFullName());
        customerRegister.setFantasyName(request.getFantasyName());
        customerRegister.setCpfCnpj(StringUtils.returnOnlyNumbers(request.getCpfCnpj()));
        customerRegister.setSegment(request.getSegment());
        customerRegister.setRgIe(request.getRgIe());

        // Dados complementares de Pessoa Fisica
        customerRegister.setIdProfissao(request.getIdProfissao());
        customerRegister.setIdRenda(request.getIdRenda());
        customerRegister.setIdPatrimonio(request.getIdPatrimonio());
        customerRegister.setSexo(request.getSexo());
        customerRegister.setDataFundacaoPF(request.getDataFundacaoPF());

        if (!acquisition || physical) {
            customerRegister.setBirthdate(request.getBirthdate());
        }

        if (!physical && acquisition) {
            customerRegister.setBirthdate(request.getBirthdate());
            customerRegister.setPartnerName(request.getPartnerName());
            customerRegister.setPartnerBirthday(request.getPartnerBirthday());
            customerRegister.setPartnerCpf(request.getPartnerCpf());

            // Carrega Dados do Sócio Filtrado pelo CNPJ na Receita

            /*CustomerRegisterPartners socio = new CustomerRegisterPartners();
            socio.setNome(request.getPartnerName());
            customerRegister.setPartners(socio);*/
        }

        if (acquisition) {
            customerRegister.setMcc(request.getMcc());
            finalizeFlow(isBack);
            return;
        }

        customerRegister.setObservation(request.getObservation());

        finalizeFlow(isBack);
    }

    @Override
    public void onMccChanged(Integer mcc) {
        if (customerRegister.getMcc() != null && mcc != null && !customerRegister.getMcc().equals(mcc)) {
            customerRegister.setTaxList(null);
        }
    }

    @Override
    public boolean areProspectProcess() {
        return prospecting == null;
    }

    private void finalizeFlow(boolean isBack) {
        if (isBack) {
            getView().onValidationSuccessBack();
        } else {
            getView().onValidationSuccess();
        }
    }

    private void clearPersonalFields() {
        clearPersonalFields(customerRegister);

        RegisterCustomerView parentActivity = getView().getParentActivity();
        parentActivity.setConsultaReceita(null);

        if (StringUtils.isEmpty(customerRegister.getId())) {
            clearPersonalFields(parentActivity.getCustomerRegisterClone());
        }
    }

    private void clearPersonalFields(CustomerRegister customerRegister) {
        customerRegister.setMcc(null);
        customerRegister.setFullName(null);
        customerRegister.setFantasyName(null);
        customerRegister.setCpfCnpj(null);
        customerRegister.setRgIe(null);
        customerRegister.setBirthdate(null);
        customerRegister.setSegment(null);
        customerRegister.setObservation(null);
        customerRegister.setPartnerName(null);
        customerRegister.setPartnerBirthday(null);
        customerRegister.setPartnerCpf(null);
        customerRegister.setAddresses(new ArrayList<>());
        customerRegister.setIdProfissao(null);
        customerRegister.setIdRenda(null);
        customerRegister.setIdPatrimonio(null);
        customerRegister.setSexo(null);
        customerRegister.setDataFundacaoPF(null);

        // Inicializando variavel
        CustomerRegisterPartners socios = new CustomerRegisterPartners();
        customerRegister.setPartners(socios);
    }

    private void clearSavedFields(EnumRegisterCustomerType customerType,
                                  EnumRegisterPersonType personType) {

        boolean equalCustomerType = customerRegister.getCustomerType() != null
                && customerRegister.getCustomerType().equals(customerType);
        boolean equalPersonType = customerRegister.getPersonType() != null
                && customerRegister.getPersonType().equals(personType);

        if ((!equalCustomerType || !equalPersonType) && !ACQUISITION.equals(customerType)) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            clearAddressFields(parentActivity);
            clearAllFinancialFields(parentActivity);
            clearAllCommercialFields(parentActivity);
        }
    }

    private void clearAddressFields(RegisterCustomerView parentActivity) {
        customerRegister.setAddresses(new ArrayList<>());

        if (StringUtils.isEmpty(customerRegister.getId())) {
            parentActivity.getCustomerRegisterClone().setAddresses(new ArrayList<>());
        }
    }

    private void clearAllFinancialFields(RegisterCustomerView parentActivity) {
        clearAllFinancialFields(customerRegister);

        if (StringUtils.isEmpty(customerRegister.getId())) {
            clearAllFinancialFields(parentActivity.getCustomerRegisterClone());
        }
    }

    private void clearAllFinancialFields(CustomerRegister customerRegister) {
        customerRegister.setSgvCode(null);
        customerRegister.setAccountType(null);
        customerRegister.setBank(null);
        customerRegister.setBankAgency(null);
        customerRegister.setBankAgencyDigit(null);
        customerRegister.setBankAccount(null);
        customerRegister.setBankAccountDigit(null);
    }

    private void clearAllCommercialFields(RegisterCustomerView parentActivity) {
        clearAllFinancialFields(customerRegister);

        if (StringUtils.isEmpty(customerRegister.getId())) {
            clearAllCommercialFields(parentActivity.getCustomerRegisterClone());
        }
    }

    private void clearAllCommercialFields(CustomerRegister customerRegister) {
        customerRegister.setForeseenRevenue(null);
        customerRegister.setAnticipation(null);
        customerRegister.setDebitAutomatic(null);
        customerRegister.setPosList(null);
        customerRegister.setRentalMachineDue(null);
        customerRegister.setExemption(null);
    }

    public ICustomSpinnerDialogModel findItem(int idMcc) {
        for (TaxaMdr item : listTaxes) {
            if (item.getMcc() == idMcc) {
                return item;
            }
        }
        return null;
    }

}
