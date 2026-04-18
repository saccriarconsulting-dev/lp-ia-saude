package com.axys.redeflexmobile.ui.clientemigracao.register.proposal;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSub;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationCommonImpl;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCCARTAOPRESENTE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCECOMMERCE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAIMEDIATA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAPOSTERIOR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCFATURAMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_DEBIT_AUTOMATIC;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;

/**
 * @author lucasmarciano on 06/04/20
 */
@SuppressLint("NonConstantResourceId")
public class RegisterMigrationProposalFragment extends RegisterMigrationCommonImpl implements
        RegisterMigrationProposalView {

    @Inject RegisterMigrationProposalPresenter presenter;

    @BindView(R.id.migration_cet_provided_billing) CustomEditText etProvidedBilling;
    @BindView(R.id.migration_cs_negotiation_period) CustomSpinner spNegotiationPeriod;
    @BindView(R.id.migration_cs_automatic_anticipation) CustomSpinner spAutomaticAnticipation;
    @BindView(R.id.tv_rav_message) TextView tvRavMessage;

    @BindView(R.id.migracao_comercial_et_FaturamentoBruto) CustomEditText etFaturamentoBruto;
    @BindView(R.id.migracao_comercial_et_PercFaturamento) CustomEditText etPercFaturamento;
    @BindView(R.id.migracao_comercial_et_PercVendaCartao) CustomEditText etPercVendaCartao;
    @BindView(R.id.migracao_comercial_et_PercVendaEcommerce) CustomEditText etPercVendaEcommerce;
    @BindView(R.id.migracao_comercial_switch_EntregaPosterior) Switch swEntregaPosterior;
    @BindView(R.id.migracao_comercial_et_PrazoEntrega) CustomEditText etPrazoEntrega;
    @BindView(R.id.migracao_comercial_et_PercEntregaImediata) CustomEditText etPercEntregaImediata;
    @BindView(R.id.migracao_comercial_et_PercEntregaPosterior) CustomEditText etPercEntregaPosterior;

    TextWatcher tvValorMedio, tvPercFatuamento, tvValorBruto;
    TextWatcher tvEntregaImediata, tvEntregaPosterior;
    TextWatcher tvPercCartao, tvPercEcommerce;

    public static RegisterMigrationProposalFragment newInstance() {
        return new RegisterMigrationProposalFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_migration_register_proposal;
    }

    @Override
    public void initialize() {
        RegisterMigrationSub registerMigrationSub = parentActivity.recoverObjectMigration();

        presenter.initializeData(registerMigrationSub);
        presenter.attachView(this);
        presenter.loadNegotiationPeriod();
        presenter.loadDataAutomaticAnticipation();
        initializeCallbacks(registerMigrationSub);

        // Inicializando objetos
        InicializaEventos();
        etPercFaturamento.setEnabled(false);
        etProvidedBilling.setEnabled(false);

        // Dados de Entrega
        etPrazoEntrega.setText(null);
        setTextSilently(etPercEntregaImediata, tvEntregaImediata, "100");
        setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, "0");
        etPrazoEntrega.setEnabled(false);
        etPercEntregaImediata.setEnabled(false);
        etPercEntregaPosterior.setEnabled(false);

        setTextSilently(etPercVendaCartao, tvPercCartao, null);
        setTextSilently(etPercVendaEcommerce, tvPercEcommerce, null);
    }

    private void InicializaEventos() {
        swEntregaPosterior.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swEntregaPosterior.isChecked()) {
                    etPrazoEntrega.setEnabled(true);
                    etPercEntregaImediata.setEnabled(true);
                    etPercEntregaPosterior.setEnabled(true);
                    etPrazoEntrega.requestFieldFocus();
                } else {
                    etPrazoEntrega.setText(null);
                    setTextSilently(etPercEntregaImediata, tvEntregaImediata, "100");
                    setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, "0");
                    etPrazoEntrega.setEnabled(false);
                    etPercEntregaImediata.setEnabled(false);
                    etPercEntregaPosterior.setEnabled(false);
                }
            }
        });

        tvValorBruto = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable pValor) {
                if (!etFaturamentoBruto.isEmpty() && etFaturamentoBruto.getCurrencyDouble() >= 1) {
                    etPercFaturamento.setEnabled(true);
                    etProvidedBilling.setEnabled(true);
                } else {
                    etPercFaturamento.setEnabled(false);
                    etProvidedBilling.setEnabled(false);
                }
            }
        };
        tvPercFatuamento = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable pValor) {
                if (etPercFaturamento.hasFocusable()) {
                    if (pValor.toString().length() > 0 && !etFaturamentoBruto.isEmpty()) {
                        double vPrevisao = 0.00;
                        double vValorBruto = etFaturamentoBruto.getCurrencyDouble();
                        double vPercFat = Double.valueOf(pValor.toString());
                        vPrevisao = (vValorBruto * vPercFat) / 100;
                        setTextSilently(etProvidedBilling, tvValorMedio, Util_IO.formataValor(vPrevisao));
                    } else {
                        setTextSilently(etPercFaturamento, tvPercFatuamento, null);
                        setTextSilently(etProvidedBilling, tvValorMedio, null);
                    }
                }
            }
        };
        tvValorMedio = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence pValor, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence pValor, int i, int i1, int i2) {
                if (etProvidedBilling.hasFocusable()) {
                    if (pValor.toString().length() > 0 && !etFaturamentoBruto.isEmpty()) {
                        double vPrevisao = Double.valueOf(pValor.toString().replace("R", "").replace("$", "").replace(".", "").replace(",", ".").trim());
                        double vValorBruto = etFaturamentoBruto.getCurrencyDouble();
                        int vPercFat = 0;
                        vPercFat = (int) ((vPrevisao * 100) / vValorBruto);
                        setTextSilently(etPercFaturamento, tvPercFatuamento, String.valueOf(vPercFat));
                    } else {
                        setTextSilently(etProvidedBilling, tvValorMedio, null);
                        setTextSilently(etPercFaturamento, tvPercFatuamento, null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable pValor) {
            }
        };
        // Entrega Imediata e Posterior
        tvEntregaImediata = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Util_IO.isNullOrEmpty(editable.toString())) {
                    if (Integer.parseInt(editable.toString()) < 100)
                        setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, String.valueOf(100 - Integer.parseInt(editable.toString())));
                    else
                        setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, "0");
                } else
                    setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, null);
            }
        };
        tvEntregaPosterior = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Util_IO.isNullOrEmpty(editable.toString())) {
                    if (Integer.parseInt(editable.toString()) < 100)
                        setTextSilently(etPercEntregaImediata, tvEntregaImediata, String.valueOf(100 - Integer.parseInt(editable.toString())));
                    else
                        setTextSilently(etPercEntregaImediata, tvEntregaImediata, "0");
                } else
                    setTextSilently(etPercEntregaImediata, tvEntregaImediata, null);
            }
        };
        // Cartão Presente e Ecommerce
        tvPercCartao = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Util_IO.isNullOrEmpty(editable.toString())) {
                    if (Integer.parseInt(editable.toString()) < 100)
                        setTextSilently(etPercVendaEcommerce, tvPercEcommerce, String.valueOf(100 - Integer.parseInt(editable.toString())));
                    else
                        setTextSilently(etPercVendaEcommerce, tvPercEcommerce, "0");
                } else
                    setTextSilently(etPercVendaEcommerce, tvPercEcommerce, null);
            }
        };
        tvPercEcommerce = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Util_IO.isNullOrEmpty(editable.toString())) {
                    if (Integer.parseInt(editable.toString()) < 100)
                        setTextSilently(etPercVendaCartao, tvPercCartao, String.valueOf(100 - Integer.parseInt(editable.toString())));
                    else
                        setTextSilently(etPercVendaCartao, tvPercCartao, "0");
                } else
                    setTextSilently(etPercVendaCartao, tvPercCartao, null);
            }
        };

        etPercFaturamento.addTextChangedListener(tvPercFatuamento);
        etProvidedBilling.addTextChangedListener(tvValorMedio);
        etFaturamentoBruto.addTextChangedListener(tvValorBruto);
        etPercEntregaImediata.addTextChangedListener(tvEntregaImediata);
        etPercEntregaPosterior.addTextChangedListener(tvEntregaPosterior);
        etPercVendaCartao.addTextChangedListener(tvPercCartao);
        etPercVendaEcommerce.addTextChangedListener(tvPercEcommerce);
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
    public void persistData() {
        clearErrors();
        presenter.doSave(getViewValues());
    }

    @Override
    public void persistCloneData() {
        clearErrors();
        presenter.finalizeFlow(getViewValues(), true);
    }

    @Override
    public void fillSpinnerNegotiationPeriod(List<ICustomSpinnerDialogModel> list) {
        spNegotiationPeriod.setList(list);
    }

    @Override
    public void fillSpinnerAutomaticAnticipation(List<ICustomSpinnerDialogModel> list) {
        spAutomaticAnticipation.setList(list);
    }

    @Override
    public void initializeInterface(RegisterMigrationSub registerMigrationSub,
                                    @Nullable ICustomSpinnerDialogModel negotiationPeriod,
                                    @Nullable ICustomSpinnerDialogModel automaticAnticipation) {
        clearErrors();

        etFaturamentoBruto.setText(StringUtils.maskCurrencyDouble(registerMigrationSub.getFaturamentoMedioPrevisto()));

        if (negotiationPeriod != null) {
            spNegotiationPeriod.doSelect(negotiationPeriod);
            PrazoNegociacao prazoNegociacao = (PrazoNegociacao) negotiationPeriod;
            configureSpinnerAutomaticAnticipation(prazoNegociacao);
        }

        PrazoNegociacao prazoNegociacao = (PrazoNegociacao) negotiationPeriod;
        if (prazoNegociacao != null && prazoNegociacao.isConfiguraAntecipacao()) {
            spAutomaticAnticipation.setEnabled(true);
        }

        if (registerMigrationSub.getFaturamentoBruto() > 0) {
            etFaturamentoBruto.setText(StringUtils.maskCurrencyDouble(
                    registerMigrationSub.getFaturamentoBruto()));
        }

        if (registerMigrationSub.getPercFaturamento() > 0) {
            etPercFaturamento.setText(String.valueOf(registerMigrationSub.getPercFaturamento()));
        }

        if (registerMigrationSub.getPercVendaCartao() > 0) {
            etPercVendaCartao.setText(String.valueOf(registerMigrationSub.getPercVendaCartao()));
        }

        if (registerMigrationSub.getPercFaturamentoEcommerce() > 0) {
            etPercVendaEcommerce.setText(String.valueOf(registerMigrationSub.getPercFaturamentoEcommerce()));
        }

        if (registerMigrationSub.getPrazoEntrega() > 0) {
            etPrazoEntrega.setText(String.valueOf(registerMigrationSub.getPrazoEntrega()));
        }

        if (registerMigrationSub.getPercEntregaImediata() > 0) {
            etPercEntregaImediata.setText(String.valueOf(registerMigrationSub.getPercEntregaImediata()));
        }

        if (registerMigrationSub.getPercEntregaPosterior() > 0) {
            etPercEntregaPosterior.setText(String.valueOf(registerMigrationSub.getPercEntregaPosterior()));
        }

        if (!Util_IO.isNullOrEmpty(registerMigrationSub.getEntregaPosCompra())) {
            if (registerMigrationSub.getEntregaPosCompra().equals("S"))
                swEntregaPosterior.setChecked(true);
            else
                swEntregaPosterior.setChecked(false);
        } else {
            swEntregaPosterior.setChecked(false);
        }
    }

    @Override
    public void onValidationSuccess(RegisterMigrationSub request) {
        if (parentActivity.getTipoMigracao().equals("ADQ")) {
            request.setTaxesList(new ArrayList<RegisterMigrationSubTax>());
            parentActivity.saveObjectMigration(request);
            parentActivity.doComplete();
        } else {
            parentActivity.saveObjectMigration(request);
            parentActivity.stepValidated();
        }
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.stepValidatedBack();
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        if (errors.contains(ET_FORESEEN_REVENUE)) {
            etProvidedBilling.showError();
        }

        if (errors.contains(SPN_NEGOTIATION)) {
            spNegotiationPeriod.showError();
        }
        if (errors.contains(SPN_DEBIT_AUTOMATIC)) {
            spAutomaticAnticipation.showError();
        }
        if (errors.contains(ET_PERCFATURAMENTO)) etPercFaturamento.showErrorPercentual();
        if (errors.contains(ET_PERCCARTAOPRESENTE)) etPercVendaCartao.showErrorPercentual();
        if (errors.contains(ET_PERCECOMMERCE)) etPercVendaEcommerce.showErrorPercentual();
        if (errors.contains(ET_PERCENTREGAIMEDIATA)) etPercEntregaImediata.showErrorPercentual();
        if (errors.contains(ET_PERCENTREGAPOSTERIOR)) etPercEntregaPosterior.showErrorPercentual();
    }

    private void clearErrors() {
        etProvidedBilling.hideError();
        spNegotiationPeriod.hideError();
        spAutomaticAnticipation.hideError();
        etPercFaturamento.hideError();
        etPercVendaCartao.hideError();
        etPercVendaEcommerce.hideError();
        etPercEntregaImediata.hideError();
        etPercEntregaPosterior.hideError();
    }

    private RegisterMigrationSub getViewValues() {
        RegisterMigrationSub registerMigrationSub = new RegisterMigrationSub();
        if (!Util_IO.isNullOrEmpty(etProvidedBilling.getText()))
            registerMigrationSub.setFaturamentoMedioPrevisto(etProvidedBilling.getCurrencyDouble());

        registerMigrationSub.setIdPrazoNegociacao(spNegotiationPeriod.getSelectedItemId());
        if (spAutomaticAnticipation != null && spAutomaticAnticipation.getSelectedItemId() != null) {
            registerMigrationSub.setAntecipacao(spAutomaticAnticipation.getSelectedItemId().equals(EnumRegisterAnticipation.YES.getIdValue()));
        }

        registerMigrationSub.setFaturamentoBruto(etFaturamentoBruto.getCurrencyDouble());

        if (!Util_IO.isNullOrEmpty(etPercFaturamento.getText()))
            registerMigrationSub.setPercFaturamento(Integer.valueOf(etPercFaturamento.getText()));

        if (!Util_IO.isNullOrEmpty(etPercVendaCartao.getText()))
            registerMigrationSub.setPercVendaCartao(Integer.valueOf(etPercVendaCartao.getText()));

        if (!Util_IO.isNullOrEmpty(etPercVendaEcommerce.getText()))
            registerMigrationSub.setPercFaturamentoEcommerce(Integer.valueOf(etPercVendaEcommerce.getText()));

        if (!Util_IO.isNullOrEmpty(etPrazoEntrega.getText()))
            registerMigrationSub.setPrazoEntrega(Integer.valueOf(etPrazoEntrega.getText()));

        if (!Util_IO.isNullOrEmpty(etPercEntregaImediata.getText()))
            registerMigrationSub.setPercEntregaImediata(Integer.valueOf(etPercEntregaImediata.getText()));

        if (!Util_IO.isNullOrEmpty(etPercEntregaPosterior.getText()))
            registerMigrationSub.setPercEntregaPosterior(Integer.valueOf(etPercEntregaPosterior.getText()));

        if (swEntregaPosterior.isChecked())
            registerMigrationSub.setEntregaPosCompra("S");
        else
            registerMigrationSub.setEntregaPosCompra("N");

        return registerMigrationSub;
    }

    private void initializeCallbacks(RegisterMigrationSub registerMigrationSub) {
        spNegotiationPeriod.setCallback(item -> {
            if (registerMigrationSub.getIdPrazoNegociacao() != null && !registerMigrationSub.getIdPrazoNegociacao().equals(item.getIdValue())) {
                registerMigrationSub.setTaxesList(new ArrayList<>());
            }

            PrazoNegociacao prazoNegociacao = (PrazoNegociacao) item;
            if (prazoNegociacao != null) {
                configureSpinnerAutomaticAnticipation(prazoNegociacao);
            }
        });
    }

    private void configureSpinnerAutomaticAnticipation(PrazoNegociacao prazoNegociacao) {
        if (prazoNegociacao.isConfiguraAntecipacao()) {
            spAutomaticAnticipation.setEnabled(true);
            spAutomaticAnticipation.removeSelection();
        } else {
            spAutomaticAnticipation.setEnabled(false);
            spAutomaticAnticipation.doSelect(EnumRegisterAnticipation.YES);
        }
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
