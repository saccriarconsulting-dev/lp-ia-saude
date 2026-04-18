package com.axys.redeflexmobile.ui.register.customer.commercial;

import static android.view.View.GONE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ANTICIPATION_TAX;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_FORESEEN_REVENUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCCARTAOPRESENTE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCECOMMERCE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAIMEDIATA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCENTREGAPOSTERIOR;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.ET_PERCFATURAMENTO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_ANTICIPATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_EXEMPTION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_NEGOTIATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_POS_MODEL;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.SPN_RENTAL_DUE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields.TAX_DEBIT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAnticipation;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterFields;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterTax;
import com.axys.redeflexmobile.shared.models.customerregister.MachineType;
import com.axys.redeflexmobile.shared.models.customerregister.PrazoNegociacao;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.ButtonAction;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;
import com.axys.redeflexmobile.ui.register.customer.commercial.RegisterCustomerCommercialPosViewHolder.RegisterCustomerCommercialPosViewHolderListener;
import com.axys.redeflexmobile.ui.register.customer.commercial.pos.RegisterCustomerCommercialPosFragment;
import com.axys.redeflexmobile.ui.register.customer.commercial.tax.RegisterCustomerCommercialTaxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Bruno Pimentel on 22/11/18.
 */
@SuppressLint("NonConstantResourceId")
public class RegisterCustomerCommercialFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerCommercialView,
        RegisterCustomerCommercialPosViewHolderListener,
        CustomSpinner.ICustomSpinnerCallback {

    private final int TEMPO_TIMER = 1000;

    @Inject RegisterCustomerCommercialPresenter presenter;
    @Inject RegisterCustomerCommercialPosAdapter posAdapter;

    @BindView(R.id.fourth_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.fourth_register_et_foreseen_revenue) CustomEditText etForeseenRevenue;
    @BindView(R.id.fourth_register_spn_negotiation) CustomSpinner spnNegotiation;
    @BindView(R.id.fourth_register_spn_anticipation) CustomSpinner spnAnticipation;
    //@BindView(R.id.fourth_register_spn_debit_automatic) CustomSpinner spnDebitAutomatic;
    @BindView(R.id.customer_register_commercial_tax_cet_anticipation) CustomEditText cetAnticipation;
    @BindView(R.id.fourth_register_ll_add_tax) LinearLayout llAddTax;
    @BindView(R.id.fourth_register_rv_pos_list) RecyclerView rvPOS;
    @BindView(R.id.fourth_register_ll_add_pos) LinearLayout llAddPos;
    @BindView(R.id.fourth_register_spn_rental_due) CustomSpinner spnRentalDue;
    @BindView(R.id.fourth_register_spn_exemption) CustomSpinner spnExemption;
    @BindView(R.id.fourth_register_tv_total) TextView tvTotal;
    @BindView(R.id.fourth_register_et_observation) CustomEditText etObservation;
    @BindView(R.id.fourth_register_ll_tax_error) LinearLayout llTaxError;
    @BindView(R.id.fourth_register_iv_tax_error) ImageView ivTaxError;
    @BindView(R.id.fourth_register_tv_tax_error) TextView tvTaxError;
    @BindView(R.id.fourth_register_ll_pos_error) LinearLayout llPosError;
    @BindView(R.id.fourth_register_tv_title_number) TextView txtNumeroTitulo;

    @BindView(R.id.customer_register_commercial_et_FaturamentoEstabelecimento) CustomEditText etFaturamentoBruto;
    @BindView(R.id.customer_register_commercial_PercFaturamento) CustomEditText etPercFaturamento;
    @BindView(R.id.customer_register_commercial_PercVendaCartao) CustomEditText etPercVendaCartao;
    @BindView(R.id.customer_register_commercial_PercVendaEcommerce) CustomEditText etPercVendaEcommerce;
    @BindView(R.id.customer_register_commercial_switch_EntregaPosterior) Switch swEntregaPosterior;
    @BindView(R.id.customer_register_commercial_PrazoEntrega) CustomEditText etPrazoEntrega;
    @BindView(R.id.customer_register_commercial_PercEntregaImediata) CustomEditText etPercEntregaImediata;
    @BindView(R.id.customer_register_commercial_PercEntregaPosterior) CustomEditText etPercEntregaPosterior;

    TextWatcher tvValorMedio, tvPercFatuamento, tvValorBruto;
    TextWatcher tvEntregaImediata, tvEntregaPosterior;
    TextWatcher tvPercCartao, tvPercEcommerce;

    private CompositeDisposable compositeDisposable;
    private final PublishSubject<ButtonAction> buttonSubject = PublishSubject.create();

    public static RegisterCustomerCommercialFragment newInstance() {
        return new RegisterCustomerCommercialFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_commercial;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        rvPOS.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPOS.setHasFixedSize(true);
        rvPOS.setNestedScrollingEnabled(false);
        rvPOS.setAdapter(posAdapter);

        compositeDisposable = new CompositeDisposable();
        llAddPos.setOnClickListener(view -> buttonSubject.onNext(() -> onPosSelected(null, null)));
        llAddTax.setOnClickListener(view -> buttonSubject.onNext(this::openTaxWindow));

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
                if (!etFaturamentoBruto.isEmpty() && etFaturamentoBruto.getCurrencyDouble() >= 1)
                {
                    etPercFaturamento.setEnabled(true);
                    etForeseenRevenue.setEnabled(true);
                }
                else
                {
                    etPercFaturamento.setEnabled(false);
                    etForeseenRevenue.setEnabled(false);
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
                        setTextSilently(etForeseenRevenue, tvValorMedio, Util_IO.formataValor(vPrevisao));
                    } else {
                        setTextSilently(etPercFaturamento, tvPercFatuamento, null);
                        setTextSilently(etForeseenRevenue, tvValorMedio, null);
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
                if (etForeseenRevenue.hasFocusable()) {
                    if (pValor.toString().length() > 0 && !etFaturamentoBruto.isEmpty()) {
                        double vPrevisao = Double.valueOf(pValor.toString().replace("R", "").replace("$", "").replace(".", "").replace(",", ".").trim());
                        double vValorBruto = etFaturamentoBruto.getCurrencyDouble();
                        int vPercFat = 0;
                        vPercFat = (int) ((vPrevisao * 100) / vValorBruto);
                        setTextSilently(etPercFaturamento, tvPercFatuamento, String.valueOf(vPercFat));
                    } else {
                        setTextSilently(etForeseenRevenue, tvValorMedio, null);
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
                }
                else
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
                }
                else
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
                }
                else
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
                }
                else
                    setTextSilently(etPercVendaCartao, tvPercCartao, null);
            }
        };

        etPercFaturamento.addTextChangedListener(tvPercFatuamento);
        etForeseenRevenue.addTextChangedListener(tvValorMedio);
        etFaturamentoBruto.addTextChangedListener(tvValorBruto);
        etPercEntregaImediata.addTextChangedListener(tvEntregaImediata);
        etPercEntregaPosterior.addTextChangedListener(tvEntregaPosterior);
        etPercVendaCartao.addTextChangedListener(tvPercCartao);
        etPercVendaEcommerce.addTextChangedListener(tvPercEcommerce);

        etPercFaturamento.setEnabled(false);
        etForeseenRevenue.setEnabled(false);

        // Dados de Entrega
        etPrazoEntrega.setText(null);
        setTextSilently(etPercEntregaImediata, tvEntregaImediata, "100");
        setTextSilently(etPercEntregaPosterior, tvEntregaPosterior, "0");
        etPrazoEntrega.setEnabled(false);
        etPercEntregaImediata.setEnabled(false);
        etPercEntregaPosterior.setEnabled(false);

        setTextSilently(etPercVendaCartao, tvPercCartao, null);
        setTextSilently(etPercVendaEcommerce, tvPercEcommerce, null);

        // Solicitado para Retirar e definir Prazo Fixo de 30Dias
        spnNegotiation.setVisibility(GONE);
        spnRentalDue.setVisibility(GONE);
        spnExemption.setVisibility(GONE);

        presenter.initializeData();
        scrollView.post(() -> scrollView.scrollTo(EMPTY_INT, EMPTY_INT));
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable.add(
                buttonSubject.throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(ButtonAction::action, throwable -> {
                        })
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        presenter.clearDispose();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void fillInterfaceWithProspectData(ProspectingClientAcquisition prospecting) {
        if (prospecting != null) {
            setTextSilently(etForeseenRevenue, tvValorMedio, Util_IO.formatDoubleToDecimalNonDivider(prospecting.getEstimatedAverageBilling()));
        }
    }

    @Override
    public void fillNegotiation(List<ICustomSpinnerDialogModel> negotiationTermList,
                                ProspectingClientAcquisition prospect) {
        spnNegotiation.setList(negotiationTermList);
        if (prospect != null) {
            ICustomSpinnerDialogModel filteredItem = filterCustomSpinner(negotiationTermList, prospect.getIdTradingTerm());
            spnNegotiation.doSelect(filteredItem);
        }
    }

    @Override
    public void fillAnticipation(List<ICustomSpinnerDialogModel> anticipationList,
                                 ProspectingClientAcquisition prospect) {
        spnAnticipation.setList(anticipationList);
        spnAnticipation.setCallback(this);
        if (prospect != null) {
            spnAnticipation.doSelect(prospect.isAnticipation() ?
                    EnumRegisterAnticipation.YES : EnumRegisterAnticipation.NO);
        }
    }

    @Override
    public void fillAnticipationValue(List<CustomerRegisterTax> anticipationListValue) {
        cetAnticipation.setText(anticipationListValue.isEmpty() ? "" : "" + anticipationListValue.get(0).getAnticipation());
    }

    @Override
    public void fillPosList(List<MachineType> posList) {
        posAdapter.setList(posList);
    }

    @Override
    public void fillDueList(List<ICustomSpinnerDialogModel> dueList) {
        spnRentalDue.setList(dueList);
    }

    @Override
    public void fillExemption(List<ICustomSpinnerDialogModel> exemptionList) {
        spnExemption.setList(exemptionList);
        spnExemption.doSelectWithCallback(0);
    }

    @Override
    public void fillTotalValue(String value) {
        if (tvTotal != null) {
            tvTotal.setText(value);
            return;
        }
        new Handler().postDelayed(() -> fillTotalValue(value), TEMPO_TIMER);
    }

    @Override
    public void initializeFieldValues(CustomerRegister customerRegister) {
        if (customerRegister.getForeseenRevenue() != null) {
            setTextSilently(etForeseenRevenue, tvValorMedio, StringUtils.maskCurrencyDouble(customerRegister.getForeseenRevenue()));
        }

        if (customerRegister.getNegotiationTermId() != null) {
            spnNegotiation.doSelectWithCallback(customerRegister.getNegotiationTermId());
            prepareAnticipationField(spnNegotiation.getSelectedItem());
        } else {
            spnNegotiation.doSelectWithCallback(2);
            prepareAnticipationField(spnNegotiation.getSelectedItem());
        }

        if (customerRegister.getAnticipation() != null) {
            spnAnticipation.doSelectWithCallback(Util_IO.booleanToNumber(customerRegister.getAnticipation()));
        }

        if (customerRegister.getAnticipationValue() != null) {
            cetAnticipation.setText(StringUtils.maskCurrencyDouble(
                    customerRegister.getAnticipationValue()));
        }

        if (customerRegister.getRentalMachineDue() != null) {
            spnRentalDue.doSelectWithCallback(customerRegister.getRentalMachineDue());
        }

        if (customerRegister.getExemption() != null) {
            spnExemption.doSelectWithCallback(customerRegister.getExemption());
        }

        if (!Util_IO.isEmptyOrNullList(customerRegister.getTaxList())) {
            showTaxInfoLayout(false);
        }

        etObservation.setText(customerRegister.getObservation() != null
                ? customerRegister.getObservation()
                : EMPTY_STRING);

        if (customerRegister.getFaturamentoBruto() != null) {
            etFaturamentoBruto.setText(StringUtils.maskCurrencyDouble(
                    customerRegister.getFaturamentoBruto()));
        }

        if (customerRegister.getPercFaturamento() != null) {
            etPercFaturamento.setText(String.valueOf(customerRegister.getPercFaturamento()));
        }

        if (customerRegister.getPercVendaCartao() != null) {
            etPercVendaCartao.setText(String.valueOf(customerRegister.getPercVendaCartao()));
        }

        if (customerRegister.getPercVendaEcommerce() != null) {
            etPercVendaEcommerce.setText(String.valueOf(customerRegister.getPercVendaEcommerce()));
        }

        if (customerRegister.getPrazoEntrega() != null) {
            etPrazoEntrega.setText(String.valueOf(customerRegister.getPrazoEntrega()));
        }

        if (customerRegister.getPercEntregaImediata() != null) {
            etPercEntregaImediata.setText(String.valueOf(customerRegister.getPercEntregaImediata()));
        }

        if (customerRegister.getPercEntregaPosterior() != null) {
            etPercEntregaPosterior.setText(String.valueOf(customerRegister.getPercEntregaPosterior()));
        }

        if (!Util_IO.isNullOrEmpty(customerRegister.getEntregaPosCompra())) {
            if (customerRegister.getEntregaPosCompra().equals("S"))
                swEntregaPosterior.setChecked(true);
            else
                swEntregaPosterior.setChecked(false);
        } else {
            swEntregaPosterior.setChecked(false);
        }
        initializeCallbacks(customerRegister);
    }

    @Override
    public void persistData() {
        clearErrors();
        presenter.doSave(getCustomerRegisterFieldsValues());
    }

    @Override
    public void persistCloneData() {
        presenter.saveData(getCustomerRegisterFieldsValues(), true);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onPosSelected(MachineType machineType, Integer position) {
        presenter.cacheValues(getCustomerRegisterFieldsValues());
        parentActivity.openFragmentWithoutBottomBar(RegisterCustomerCommercialPosFragment
                .newInstance(machineType, position));
    }

    @Override
    public void onPosRemove(int position) {
        presenter.removePos(position);
    }

    @Override
    public void onValidationSuccess() {
        parentActivity.stepValidated();
    }

    @Override
    public void onValidationSuccessBack() {
        parentActivity.stepValidatedBack();
    }

    @Override
    public void setNumeroTitulo(String pNumero) {
        txtNumeroTitulo.setText(pNumero);
    }

    @Override
    public void setErrors(List<EnumRegisterFields> errors) {
        if (errors == null || errors.isEmpty()) return;
        if (errors.contains(ET_FORESEEN_REVENUE)) etForeseenRevenue.showError();
        if (errors.contains(SPN_NEGOTIATION)) spnNegotiation.showError();
        if (errors.contains(SPN_ANTICIPATION)) spnAnticipation.showError();
        if (errors.contains(ANTICIPATION_TAX)) cetAnticipation.showError();
        if (errors.contains(SPN_POS_MODEL)) llPosError.setVisibility(View.VISIBLE);
        if (errors.contains(SPN_RENTAL_DUE)) spnRentalDue.showError();
        if (errors.contains(SPN_EXEMPTION)) spnExemption.showError();
        if (errors.contains(TAX_DEBIT)) showTaxInfoLayout(true);
        if (errors.contains(ET_PERCFATURAMENTO)) etPercFaturamento.showErrorPercentual();
        if (errors.contains(ET_PERCCARTAOPRESENTE)) etPercVendaCartao.showErrorPercentual();
        if (errors.contains(ET_PERCECOMMERCE)) etPercVendaEcommerce.showErrorPercentual();
        if (errors.contains(ET_PERCENTREGAIMEDIATA)) etPercEntregaImediata.showErrorPercentual();
        if (errors.contains(ET_PERCENTREGAPOSTERIOR)) etPercEntregaPosterior.showErrorPercentual();
        moveFocusToFirstError(errors.get(EMPTY_INT));
    }

    @Override
    public void afterTextChanged(Editable s) {
        CustomerRegister customerRegister = presenter.getLocalCustomerRegister();
        if (customerRegister.getForeseenRevenue() != null
                && !etForeseenRevenue.getCurrencyDouble().equals(customerRegister.getForeseenRevenue())) {
            customerRegister.setTaxList(new ArrayList<>());
            llTaxError.setVisibility(View.GONE);
        }
    }

    private void initializeCallbacks(CustomerRegister customerRegister) {
        etForeseenRevenue.addTextChangedListener(this);

        spnNegotiation.setCallback(item -> {
            if (customerRegister.getNegotiationTermId() != null && !customerRegister.getNegotiationTermId().equals(item.getIdValue())) {
                customerRegister.setTaxList(new ArrayList<>());
                llTaxError.setVisibility(View.GONE);
            }
            prepareAnticipationField(item);
        });
    }

    private void prepareAnticipationField(ICustomSpinnerDialogModel item) {
        PrazoNegociacao prazoNegociacao = (PrazoNegociacao) item;
        if (prazoNegociacao == null) {
            return;
        }

        if (prazoNegociacao.isConfiguraAntecipacao()) {
            spnAnticipation.setEnabled(true);
            spnAnticipation.removeSelection();
        } else {
            spnAnticipation.setEnabled(false);
            spnAnticipation.doSelect(EnumRegisterAnticipation.YES);
        }

        updateCetAnticipationView();
    }

    private void showTaxInfoLayout(boolean hasError) {
        ivTaxError.setImageResource(hasError
                ? R.drawable.ic_error_outline_red_wrapped
                : R.drawable.ic_check_circle_grey_wraped);
        tvTaxError.setText(hasError
                ? R.string.customer_register_commercial_tax_selected_error
                : R.string.customer_register_commercial_tax_selected_success);
        tvTaxError.setTextColor(ContextCompat.getColor(requireContext(), hasError
                ? R.color.colorAccent
                : R.color.textoCinzaMedio));
        llTaxError.setVisibility(View.VISIBLE);
    }

    private void moveFocusToFirstError(EnumRegisterFields error) {
        int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
        if (error == ET_FORESEEN_REVENUE) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etForeseenRevenue.getY() - margin);
        } else if (error == SPN_NEGOTIATION) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) spnNegotiation.getY() - margin);
        } else if (error == SPN_ANTICIPATION) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) spnAnticipation.getY() - margin);
        } else if (error == ANTICIPATION_TAX) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) cetAnticipation.getY() - margin);
        } /*else if (error == SPN_DEBIT_AUTOMATIC) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) spnDebitAutomatic.getY() - margin);
        }*/ else if (error == SPN_POS_MODEL) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) rvPOS.getY() - margin);
        } else if (error == SPN_RENTAL_DUE) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) spnRentalDue.getY() - margin);
        } else if (error == SPN_EXEMPTION) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) spnExemption.getY() - margin);
        } else if (error == ET_PERCFATURAMENTO) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etPercFaturamento.getY() - margin);
        } else if (error == ET_PERCCARTAOPRESENTE) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etPercVendaCartao.getY() - margin);
        } else if (error == ET_PERCECOMMERCE) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etPercVendaEcommerce.getY() - margin);
        } else if (error == ET_PERCENTREGAIMEDIATA) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etPercEntregaImediata.getY() - margin);
        } else if (error == ET_PERCENTREGAPOSTERIOR) {
            scrollView.smoothScrollTo(EMPTY_INT, (int) etPercEntregaPosterior.getY() - margin);
        }
    }

    private void clearErrors() {
        etForeseenRevenue.hideError();
        spnAnticipation.hideError();
        cetAnticipation.hideError();
        //spnDebitAutomatic.hideError();
        llPosError.setVisibility(View.GONE);
        spnRentalDue.hideError();
        spnExemption.hideError();
        presenter.setErrorList(null);
    }

    private CustomerRegister getCustomerRegisterFieldsValues() {
        CustomerRegister customerRegister = new CustomerRegister(presenter.getLocalCustomerRegister());
        try {
            customerRegister.setForeseenRevenue(etForeseenRevenue.getCurrencyDouble());
            customerRegister.setNegotiationTermId(spnNegotiation.getSelectedItemId());
            customerRegister.setRentalMachineDue(spnRentalDue.getSelectedItemDescription());
            customerRegister.setExemption(spnExemption.getSelectedItemId());
            customerRegister.setObservation(etObservation.getText());
            customerRegister.setAnticipation(spnAnticipation.getSelectedItemId() != null
                    ? Util_IO.numberToBoolean(spnAnticipation.getSelectedItemId())
                    : null);
            customerRegister.setAnticipationValue(cetAnticipation.getCurrencyDouble());
            customerRegister.setDebitAutomatic(false);

            customerRegister.setFaturamentoBruto(etFaturamentoBruto.getCurrencyDouble());

            if (!Util_IO.isNullOrEmpty(etPercFaturamento.getText()))
                customerRegister.setPercFaturamento(Integer.valueOf(etPercFaturamento.getText()));

            if (!Util_IO.isNullOrEmpty(etPercVendaCartao.getText()))
                customerRegister.setPercVendaCartao(Integer.valueOf(etPercVendaCartao.getText()));

            if (!Util_IO.isNullOrEmpty(etPercVendaEcommerce.getText()))
                customerRegister.setPercVendaEcommerce(Integer.valueOf(etPercVendaEcommerce.getText()));

            if (!Util_IO.isNullOrEmpty(etPrazoEntrega.getText()))
                customerRegister.setPrazoEntrega(Integer.valueOf(etPrazoEntrega.getText()));

            if (!Util_IO.isNullOrEmpty(etPercEntregaImediata.getText()))
                customerRegister.setPercEntregaImediata(Integer.valueOf(etPercEntregaImediata.getText()));

            if (!Util_IO.isNullOrEmpty(etPercEntregaPosterior.getText()))
                customerRegister.setPercEntregaPosterior(Integer.valueOf(etPercEntregaPosterior.getText()));

            if (swEntregaPosterior.isChecked())
                customerRegister.setEntregaPosCompra("S");
            else
                customerRegister.setEntregaPosCompra("N");
        } catch (Exception ex) {
            Log.d("Roni", "getCustomerRegisterFieldsValues: " + ex.getMessage());
        }
        return customerRegister;
    }

    public void openTaxWindow() {
        CustomerRegister customerRegisterCached = getCustomerRegisterFieldsValues();
        if (!validateFieldsToOpenTax(customerRegisterCached)) {
            return;
        }

        presenter.cacheValues(customerRegisterCached);
        parentActivity.openFragmentWithoutBottomBar(RegisterCustomerCommercialTaxFragment.newInstance());
    }

    private boolean validateFieldsToOpenTax(CustomerRegister customerRegister) {
        View.OnClickListener callback = null;
        if (NumberUtils.isEmptyDouble(customerRegister.getForeseenRevenue()) && Util_IO.isEmptyOrNullList(customerRegister.getTaxList())) {
            callback = v -> moveFocusToFirstError(ET_FORESEEN_REVENUE);
        } else if (spnNegotiation.getSelectedItem() == null && Util_IO.isEmptyOrNullList(customerRegister.getTaxList())) {
            callback = v -> moveFocusToFirstError(SPN_NEGOTIATION);
        } else if (spnAnticipation.getSelectedItem() == null && Util_IO.isEmptyOrNullList(customerRegister.getTaxList())) {
            callback = v -> moveFocusToFirstError(SPN_ANTICIPATION);
        } else if (spnAnticipation.getSelectedItem() != null
                && Util_IO.numberToBoolean(spnAnticipation.getSelectedItemId())
                && (cetAnticipation.getCurrencyDouble() == null || cetAnticipation.getCurrencyDouble() == 0)) {
            callback = v -> moveFocusToFirstError(ANTICIPATION_TAX);
        }

        if (callback == null) return true;

        showOneButtonDialog(EMPTY_STRING,
                getString(R.string.customer_register_commercial_error_open_tax),
                callback);
        return false;
    }

    private @Nullable
    ICustomSpinnerDialogModel filterCustomSpinner(
            List<ICustomSpinnerDialogModel> list,
            int idListItem) {
        for (ICustomSpinnerDialogModel item : list) {
            if (item.getIdValue() == idListItem)
                return item;
        }
        return null;
    }

    @Override
    public void onNext(ICustomSpinnerDialogModel item) {
        updateCetAnticipationView();
    }

    private void updateCetAnticipationView() {
        boolean withAnticipation = spnAnticipation.getSelectedItemId() != null && Util_IO.numberToBoolean(spnAnticipation.getSelectedItemId());
        if (withAnticipation) {
            cetAnticipation.setVisibility(View.VISIBLE);
        } else {
            cetAnticipation.setVisibility(View.GONE);
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
