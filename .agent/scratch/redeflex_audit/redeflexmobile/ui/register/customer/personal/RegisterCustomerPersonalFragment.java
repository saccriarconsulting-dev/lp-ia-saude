package com.axys.redeflexmobile.ui.register.customer.personal;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_DATA_FUNDACAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FANTASY_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FULL_NAME;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_RG;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_STARTING_DATE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_SEXO;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;
import static com.axys.redeflexmobile.ui.register.customer.personal.RegisterCustomerPersonalPresenterImpl.LIMIT_YEAR;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterSexo;
import com.axys.redeflexmobile.shared.models.ConsultaPessoaFisica;
import com.axys.redeflexmobile.shared.models.ConsultaReceita;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.customer.personal.dialog.FillCnpjInfoDialog;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Bruno Pimentel on 20/11/18.
 */
@SuppressLint("NonConstantResourceId")
public class RegisterCustomerPersonalFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerPersonalView {

    @Inject RegisterCustomerPersonalPresenter presenter;

    @BindView(R.id.first_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.first_register_spn_customer_type) CustomSpinner spnCustomerType;
    @BindView(R.id.first_register_spn_person_type) CustomSpinner spnPersonType;
    @BindView(R.id.first_register_et_cpf) CustomEditText etCpfCnpj;
    @BindView(R.id.first_register_et_full_name) CustomEditText etFullName;
    @BindView(R.id.first_register_et_fantasy_name) CustomEditText etFantasyName;
    @BindView(R.id.first_register_et_rg) CustomEditText etRgIe;
    @BindView(R.id.first_register_et_starting_date) CustomEditText etStartingDate;
    @BindView(R.id.first_register_spn_segment) CustomSpinner spnSegment;
    @BindView(R.id.first_register_spn_mcc) CustomSpinner spnMcc;
    @BindView(R.id.first_register_cet_observation) CustomEditText etObservation;
    @BindView(R.id.first_register_et_partner_full_name) CustomEditText etPartnerFullName;
    @BindView(R.id.first_register_et_partner_birthday) CustomEditText etPartnerBirthday;
    @BindView(R.id.first_register_et_partner_cpf) CustomEditText etPartnerCpf;
    @BindView(R.id.first_register_spn_profissao) CustomSpinner spnProfissao;
    @BindView(R.id.first_register_spn_renda) CustomSpinner spnRenda;
    @BindView(R.id.first_register_spn_patrimonio) CustomSpinner spnPatrimonio;
    @BindView(R.id.first_register_spn_sexo) CustomSpinner spnSexo;
    @BindView(R.id.first_register_et_datafundacaoPF) CustomEditText etDataFundacaoPF;

    TextWatcher tvDataNAscimento;

    public static RegisterCustomerPersonalFragment newInstance() {
        return new RegisterCustomerPersonalFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_personal;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        prepareEvents();
        prepareFieldsToRecruiter();

        presenter.initializeData();
        scrollView.post(() -> {
            if (scrollView != null) {
                scrollView.scrollTo(EMPTY_INT, EMPTY_INT);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void fillClientType(List<ICustomSpinnerDialogModel> list) {
        spnCustomerType.setList(list);
    }

    @Override
    public void fillPersonType(List<ICustomSpinnerDialogModel> list) {
        spnPersonType.setList(list);
    }

    @Override
    public void fillMccList(List<ICustomSpinnerDialogModel> list) {
        spnMcc.setList(list);
    }

    @Override
    public void fillSegmentList(List<ICustomSpinnerDialogModel> list) {
        spnSegment.setList(list);
    }

    @Override
    public void fillMccFromConsultaReceita(ICustomSpinnerDialogModel mcc) {
        if (mcc != null) {
            spnMcc.doSelect(mcc);
            if (spnPersonType.getSelectedItem().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                spnMcc.setEnabled(false);
            else
                spnMcc.setEnabled(true);
        }
        else
            spnMcc.setEnabled(true);
    }

    @Override
    public void initializeSpinnersSelection(CustomerRegister customerRegister) {
        spnCustomerType.doSelect(customerRegister.getCustomerType() != null
                ? customerRegister.getCustomerType()
                : EnumRegisterCustomerType.ACQUISITION);

        spnPersonType.doSelect(customerRegister.getPersonType() != null
                ? customerRegister.getPersonType()
                : EnumRegisterPersonType.PHYSICAL);

        presenter.clearAndChangeFields(spnCustomerType.getSelectedItem(),
                spnPersonType.getSelectedItem(), false);
    }

    @Override
    public void onQueryCpfSuccess(ConsultaPessoaFisica consultaPessoaFisica) {
        if (StringUtils.isNotEmpty(consultaPessoaFisica.getRetornoDados().getNome())) {
            etFullName.setText(consultaPessoaFisica.getRetornoDados().getNome());
            etFantasyName.setText(consultaPessoaFisica.getRetornoDados().getNome());
        }

        if (StringUtils.isNotEmpty(consultaPessoaFisica.getRetornoDados().getDataNascimento()) != null){
            setTextSilently(etStartingDate, tvDataNAscimento, consultaPessoaFisica.getRetornoDados().getDataNascimento());
        }

        spnSexo.doSelect(consultaPessoaFisica.getRetornoDados().getSexo() != null ? EnumRegisterSexo.getEnumByCharValue(consultaPessoaFisica.getRetornoDados().getSexo()) : null);
    }

    @Override
    public void onQueryCnpjSuccess(ConsultaReceita consultaReceita) {
        if (StringUtils.isNotEmpty(consultaReceita.getRazaoSocial())) {
            etFullName.setText(consultaReceita.getRazaoSocial());
        }
        if (StringUtils.isNotEmpty(consultaReceita.getFantasia())) {
            etFantasyName.setText(consultaReceita.getFantasia());
        }
        if (consultaReceita.getInicioAtividade() != null) {
            setTextSilently(etStartingDate, tvDataNAscimento, Util_IO.dateToStringBr(consultaReceita.getInicioAtividade()));
        }
        if (StringUtils.isNotEmpty(consultaReceita.getNomeSocio())) {
            etPartnerFullName.setText(consultaReceita.getNomeSocio());
        }
    }

    @Override
    public void onQueryCnpjResponseNoDialog(ConsultaReceita consultaReceita) {
        if (StringUtils.isNotEmpty(consultaReceita.getFantasia())) {
            etFantasyName.setText(consultaReceita.getFantasia());
        }
        if (consultaReceita.getInicioAtividade() != null) {
            setTextSilently(etStartingDate, tvDataNAscimento, Util_IO.dateToStringBr(consultaReceita.getInicioAtividade()));
        }
    }

    @Override
    public void fillInterfaceWithProspectData(ProspectingClientAcquisition prospecting,
                                              ICustomSpinnerDialogModel personType,
                                              boolean isPhysical,
                                              @Nullable ICustomSpinnerDialogModel mcc) {
        spnPersonType.doSelect(personType);
        if (isPhysical) {
            etCpfCnpj.setMask(CPF);
            etFullName.setText(prospecting.getCompleteName());
        } else {
            etCpfCnpj.setMask(CNPJ);
            etPartnerFullName.setText(prospecting.getCompleteName());
        }
        etFantasyName.setText(prospecting.getFantasyName());
        etCpfCnpj.setText(prospecting.getCpfCnpjNumber());
        if (mcc != null) {
            spnMcc.doSelect(mcc);
        }
    }

    @Override
    public void showDialogToFillTheJuridicalInformation(ProspectingClientAcquisition prospecting) {
        if (DeviceUtils.isConnectionInternet(requireContext())) {
            FillCnpjInfoDialog.newInstance(
                    this::checkModalResponse,
                    prospecting.getCpfCnpjNumber()
            ).show(getFragmentManager(), null);
        }
    }

    @Override
    public void fillProfissoes(List<ICustomSpinnerDialogModel> profissoes) {
        spnProfissao.setList(profissoes);
    }

    @Override
    public void fillRenda(List<ICustomSpinnerDialogModel> renda) {
        spnRenda.setList(renda);
    }

    @Override
    public void fillPatrimonio(List<ICustomSpinnerDialogModel> patrimonio) {
        spnPatrimonio.setList(patrimonio);
    }

    @Override
    public void fillSexo(List<ICustomSpinnerDialogModel> list) {
        spnSexo.setList(list);
    }

    @Override
    public void initializeFieldValues(CustomerRegister customerRegister) {
        clearErrors();
        if (customerRegister.getFullName() != null)
            etFullName.setText(customerRegister.getFullName());
        if (customerRegister.getFantasyName() != null)
            etFantasyName.setText(customerRegister.getFantasyName());
        if (customerRegister.getCpfCnpj() != null)
            etCpfCnpj.setText(customerRegister.getCpfCnpj());
        if (customerRegister.getPartnerName() != null)
            etPartnerFullName.setText(customerRegister.getPartnerName());
        if (customerRegister.getRgIe() != null)
            etRgIe.setText(customerRegister.getRgIe());
        if (customerRegister.getBirthdate() != null)
            setTextSilently(etStartingDate, tvDataNAscimento, Util_IO.dateToStringBr(customerRegister.getBirthdate()));
            //etStartingDate.setText(Util_IO.dateToStringBr(customerRegister.getBirthdate()));
        if (customerRegister.getSegment() != null)
            spnSegment.doSelectWithCallback(customerRegister.getSegment());
        if (customerRegister.getMcc() != null)
            spnMcc.doSelectWithCallback(customerRegister.getMcc());
        if (customerRegister.getObservation() != null)
            etObservation.setText(customerRegister.getObservation());
        if (customerRegister.getPartnerBirthday() != null)
            etPartnerBirthday.setText(Util_IO.dateToStringBr(customerRegister.getPartnerBirthday()));
        if (customerRegister.getPartnerCpf() != null)
            etPartnerCpf.setText(customerRegister.getPartnerCpf());
        if (customerRegister.getIdProfissao() != null)
            spnProfissao.doSelectWithCallback(customerRegister.getIdProfissao());
        if (customerRegister.getIdRenda() != null)
            spnRenda.doSelectWithCallback(customerRegister.getIdRenda());
        if (customerRegister.getIdPatrimonio() != null)
            spnPatrimonio.doSelectWithCallback(customerRegister.getIdPatrimonio());
        if (customerRegister.getSexo() != null)
        {
            spnSexo.doSelect(customerRegister.getSexo() != null
                    ? customerRegister.getSexo()
                    : null);
        }
        if (customerRegister.getDataFundacaoPF() != null)
            etDataFundacaoPF.setText(Util_IO.dateToStringBr(customerRegister.getDataFundacaoPF()));
    }

    @Override
    public void persistData() {
        clearErrors();
        presenter.doSave(getStepRequest());
    }

    @Override
    public void persistCloneData() {
        presenter.saveData(getStepRequest(), true);
    }

    @Override
    public void onValidationSuccess() {
        parentActivity.stepValidated();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.cancelFlow();
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.contains(ET_FULL_NAME)) {
            etFullName.showError();
        }
        if (errors.contains(ET_FANTASY_NAME)) {
            etFantasyName.showError();
        }
        if (errors.contains(ET_CPF)) {
            etCpfCnpj.showError();
        }
        if (errors.contains(ET_RG)) {
            etRgIe.showError();
        }
        if (errors.contains(ET_STARTING_DATE)) {
            etStartingDate.showError();
        }
        if (errors.contains(ET_DATA_FUNDACAO)) {
            etDataFundacaoPF.showError();
        }
        if (errors.contains(SPN_SEXO)) {
            spnSexo.showError();
        }
        moveFocusToFirstError(errors.get(EMPTY_INT));
    }

    @Override
    public void makeAcquisitionPhysicalLayout() {
        etFullName.setLabel(getString(R.string.customer_register_personal_full_name));
        etCpfCnpj.setLabel(getString(R.string.customer_register_personal_cpf));
        etCpfCnpj.setMask(CPF);
        if (presenter.areProspectProcess())
            etCpfCnpj.clearValue();
        etStartingDate.setLabel(getString(R.string.customer_register_personal_starting_date_physical));
        etStartingDate.showVisibility();

        etPartnerFullName.hideVisibility();
        etPartnerBirthday.hideVisibility();
        etPartnerCpf.hideVisibility();

        spnProfissao.hideVisibility();
        spnRenda.hideVisibility();
        spnPatrimonio.hideVisibility();
        spnSexo.showVisibility();
        etDataFundacaoPF.showVisibility();

        makeAcquisitionDefaults();

        spnSegment.hideVisibility();
        spnMcc.hideVisibility();
        parentActivity.inicializaAdquirenciaPF();
    }

    @Override
    public void makeAcquisitionJuridicalLayout() {
        etFullName.setLabel(getString(R.string.customer_register_personal_social_name));
        etCpfCnpj.setLabel(getString(R.string.customer_register_personal_cnpj));
        etCpfCnpj.setMask(CNPJ);
        if (presenter.areProspectProcess())
            etCpfCnpj.clearValue();
        etStartingDate.setLabel("Data da Fundação da Empresa");

        etPartnerFullName.hideVisibility();
        etPartnerBirthday.hideVisibility();
        etPartnerCpf.hideVisibility();
        etStartingDate.showVisibility();

        //makeAcquisitionDefaults();
        etStartingDate.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etFullName.showVisibility();
        etFantasyName.showVisibility();
        spnSegment.hideVisibility();
        spnMcc.hideVisibility();
        etRgIe.hideVisibility();
        etObservation.hideVisibility();

        spnProfissao.hideVisibility();
        spnRenda.hideVisibility();
        spnPatrimonio.hideVisibility();
        spnSexo.hideVisibility();
        etDataFundacaoPF.hideVisibility();
        parentActivity.inicializaAdquirenciaPJ();
    }

    private void makeAcquisitionDefaults() {
        etStartingDate.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etFullName.showVisibility();
        etFantasyName.showVisibility();
        spnSegment.showVisibility();
        spnMcc.showVisibility();
        etRgIe.showVisibility();
        etObservation.hideVisibility();
    }

    @Override
    public void makeNonAcquisitionPhysicalLayout() {
        etFullName.showVisibility();
        etFullName.setLabel(getString(R.string.customer_register_personal_full_name));
        etCpfCnpj.setLabel(getString(R.string.customer_register_personal_cpf));
        etCpfCnpj.setMask(CPF);
        if (presenter.areProspectProcess())
            etCpfCnpj.clearValue();
        etCpfCnpj.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        etFantasyName.showVisibility();
        etRgIe.showVisibility();
        etStartingDate.setLabel(getString(R.string.customer_register_personal_starting_date_physical));
        etStartingDate.showVisibility();
        spnSegment.showVisibility();
        spnMcc.hideVisibility();
        etObservation.showVisibility();
        etObservation.setImeOptions(EditorInfo.IME_ACTION_DONE);

        etPartnerFullName.hideVisibility();
        etPartnerBirthday.hideVisibility();
        etPartnerCpf.hideVisibility();
        spnSexo.showVisibility();
    }

    @Override
    public void makeNonAcquisitionJuridicalLayout() {
        etCpfCnpj.setLabel(getString(R.string.customer_register_personal_cnpj));
        etCpfCnpj.setMask(CNPJ);
        etCpfCnpj.showVisibility();
        etCpfCnpj.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        etFullName.showVisibility();
        etFantasyName.showVisibility();
        etRgIe.hideVisibility();
        etStartingDate.setLabel("Data da Fundação da Empresa");
        etStartingDate.showVisibility();
        spnSegment.showVisibility();
        spnMcc.hideVisibility();
        etObservation.showVisibility();
        etObservation.setImeOptions(EditorInfo.IME_ACTION_DONE);

        etPartnerFullName.hideVisibility();
        etPartnerBirthday.hideVisibility();
        etPartnerCpf.hideVisibility();

        spnProfissao.hideVisibility();
        spnRenda.hideVisibility();
        spnPatrimonio.hideVisibility();
        spnSexo.hideVisibility();
        etDataFundacaoPF.hideVisibility();
    }

    public boolean haveInteraction() {
        return StringUtils.isNotEmpty(etFullName.getText())
                || StringUtils.isNotEmpty(etFantasyName.getText())
                || StringUtils.isNotEmpty(etCpfCnpj.getText())
                || StringUtils.isNotEmpty(etRgIe.getText())
                || StringUtils.isNotEmpty(etStartingDate.getText())
                || spnSegment.getSelectedItem() != null
                || spnMcc.getSelectedItem() != null
                || StringUtils.isNotEmpty(etObservation.getText())
                || StringUtils.isNotEmpty(etPartnerFullName.getText())
                || StringUtils.isNotEmpty(etPartnerBirthday.getText())
                || StringUtils.isNotEmpty(etPartnerCpf.getText());
    }

    private void prepareEvents() {
        spnCustomerType.setCallback(this::customerTypeCallback);
        spnPersonType.setCallback(item -> {
            etCpfCnpj.clearValue();
            presenter.loadMccList(item.getIdValue());
            presenter.clearAndChangeFields(spnCustomerType.getSelectedItem(),
                    spnPersonType.getSelectedItem(), true);
        });

        spnMcc.setCallback(item -> presenter.onMccChanged(item.getIdValue()));
        etCpfCnpj.setAfterTextListener(this::cnpjListener);

        tvDataNAscimento = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                datanascListener(editable.toString());
            }
        };
        etStartingDate.addTextChangedListener(tvDataNAscimento);

    }

    private void prepareFieldsToRecruiter() {
        boolean isEnabled = !parentActivity.isBlockRegisterFields();
        spnCustomerType.setEnabled(isEnabled);
        spnPersonType.setEnabled(isEnabled);
        etCpfCnpj.setEnabled(isEnabled);
    }

    private CustomerRegister getStepRequest() {
        CustomerRegister request = new CustomerRegister();
        request.setCustomerType(spnCustomerType.getSelectedItemId() != null
                ? EnumRegisterCustomerType.getEnumByValue(spnCustomerType.getSelectedItemId())
                : null);
        request.setPersonType(spnPersonType.getSelectedItemId() != null
                ? EnumRegisterPersonType.getEnumByIdValue(spnPersonType.getSelectedItemId())
                : null);
        request.setMcc(spnMcc.getSelectedItemId());
        request.setFullName(etFullName.getText());
        request.setFantasyName(etFantasyName.getText());
        request.setCpfCnpj(etCpfCnpj.getText());
        request.setRgIe(etRgIe.getText());
        if (DateUtils.isDateValid(etStartingDate.getText(), true, LIMIT_YEAR)) {
            request.setBirthdate(Util_IO.stringToDateBr(etStartingDate.getText()));
        }
        if (DateUtils.isDateValid(etPartnerBirthday.getText(), true, LIMIT_YEAR)) {
            request.setPartnerBirthday(Util_IO.stringToDateBr(etPartnerBirthday.getText()));
        }
        request.setPartnerName(etPartnerFullName.getText());
        request.setPartnerCpf(etPartnerCpf.getText());
        request.setSegment(spnSegment.getSelectedItemId());
        request.setObservation(etObservation.getText());
        request.setIdProfissao(spnProfissao.getSelectedItemId());
        request.setIdRenda(spnRenda.getSelectedItemId());
        request.setIdPatrimonio(spnPatrimonio.getSelectedItemId());
        request.setSexo(spnSexo.getSelectedItemId() != null
                ? EnumRegisterSexo.getEnumByIdValue(spnSexo.getSelectedItemId())
                : null);
        if (DateUtils.isDateValid(etDataFundacaoPF.getText(), true, LIMIT_YEAR)) {
            request.setDataFundacaoPF(Util_IO.stringToDateBr(etDataFundacaoPF.getText()));
        }
        return request;
    }

    private void datanascListener(String text)
    {
        Log.d("Roni", "datanascListener: 1");
        if (etStartingDate.isNotFocused()) {
            return;
        }
        text = StringUtils.returnOnlyNumbers(text);
        if (StringUtils.returnOnlyNumbers(etCpfCnpj.getText()).length() == StringUtils.CPF_LENGTH
                && StringUtils.isCpfValid(StringUtils.returnOnlyNumbers(etCpfCnpj.getText()))
                && text.length() >=8) {
            presenter.verifyRegisterCPF(StringUtils.returnOnlyNumbers(etCpfCnpj.getText().toString()), etStartingDate.getText(), spnPersonType.getSelectedItem(), spnCustomerType.getSelectedItem());
        }

    }

    private void cnpjListener(String text) {
        if (etCpfCnpj.isNotFocused()) {
            return;
        }

        text = StringUtils.returnOnlyNumbers(text);
        if (text.length() == StringUtils.CPF_LENGTH && StringUtils.isCpfValid(text) && ((EnumRegisterPersonType) spnPersonType.getSelectedItem()).getCharValue().equals("F")) {
            presenter.verifyRegisterCPF(text, etStartingDate.getText(), spnPersonType.getSelectedItem(), spnCustomerType.getSelectedItem());
        } else if (text.length() == StringUtils.CNPJ_LENGTH && StringUtils.isCnpjValid(text)) {
            presenter.verifyRegister(text, spnPersonType.getSelectedItem(), spnCustomerType.getSelectedItem());
        }
    }

    private void customerTypeCallback(ICustomSpinnerDialogModel item) {
        if (EnumRegisterCustomerType.ACQUISITION.getIdValue().equals(item.getIdValue()) || EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue().equals(item.getIdValue())) {
            parentActivity.inicializaAdquirenciaPJ();
        } else {
            parentActivity.initializeNonAcquisitionFlow();
        }
        presenter.clearAndChangeFields(spnCustomerType.getSelectedItem(),
                spnPersonType.getSelectedItem(), true);
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        switch (error) {
            case SPN_MCC:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnMcc.getY() - margin);
                break;
            case ET_FULL_NAME:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etFullName.getY() - margin);
                break;
            case ET_FANTASY_NAME:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etFantasyName.getY() - margin);
                break;
            case ET_CPF:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etCpfCnpj.getY() - margin);
                break;
            case ET_RG:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etRgIe.getY() - margin);
                break;
            case ET_STARTING_DATE:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etStartingDate.getY() - margin);
                break;
            case ET_SEGMENT:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnSegment.getY() - margin);
                break;
            case ET_PARTNER_FULL_NAME:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etPartnerFullName.getY() - margin);
                break;
            case ET_PARTNER_CPF:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etPartnerCpf.getY() - margin);
                break;
            case ET_PARTNER_BIRTHDAY:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etPartnerBirthday.getY() - margin);
                break;
            case ET_DATA_FUNDACAO:
                scrollView.smoothScrollTo(EMPTY_INT, (int) etDataFundacaoPF.getY() - margin);
                break;
            case SPN_SEXO:
                scrollView.smoothScrollTo(EMPTY_INT, (int) spnSexo.getY() - margin);
                break;
            default:
                break;
        }
    }

    private void clearErrors() {
        spnMcc.hideError();
        etFullName.hideError();
        etFantasyName.hideError();
        etCpfCnpj.hideError();
        etRgIe.hideError();
        etStartingDate.hideError();
        spnSegment.hideError();
        etPartnerCpf.hideError();
        etPartnerBirthday.hideError();
        etPartnerFullName.hideError();
        spnProfissao.hideError();
        spnRenda.hideError();
        spnPatrimonio.hideError();
        spnSexo.hideError();
        etDataFundacaoPF.hideError();
    }

    private void checkModalResponse(boolean response, String cnpj) {
        presenter.queryCnpj(cnpj, response);
    }

    private static void setTextSilently(CustomEditText editText, TextWatcher textWatcher, CharSequence text) {
        editText.removeTextChangedListener(textWatcher); //removing watcher temporarily
        if (text != null)
            editText.setText(text.toString()); //setting text
        else
            editText.setText(null);
        editText.addTextChangedListener(textWatcher); //readding watcher
    }
}
