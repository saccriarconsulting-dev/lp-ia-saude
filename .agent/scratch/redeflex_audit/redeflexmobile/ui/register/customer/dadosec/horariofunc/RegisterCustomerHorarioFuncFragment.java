package com.axys.redeflexmobile.ui.register.customer.dadosec.horariofunc;

import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerCommonImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RegisterCustomerHorarioFuncFragment extends RegisterCustomerCommonImpl implements
        RegisterCustomerHorarioFuncView,
        RegisterCustomerActivity.RegisterCustomerActivityListener {

    @Inject
    RegisterCustomerHorarioFuncPresenter presenter;
    private List<CustomerRegisterHorarioFunc> horarioFunc;

    @BindView(R.id.horariofunc_register_scroll_view) ScrollView scrollView;
    @BindView(R.id.horariofunc_register_spn_horario) CustomSpinner spnTipoHorario;
    @BindView(R.id.horariofunc_register_bt_cancelar) Button btCancelar;
    @BindView(R.id.horariofunc_register_bt_confirmar) Button btConfirmar;

    // Controles
    @BindView(R.id.domingo_txtTitulo) TextView txtTituloDomingo;
    @BindView(R.id.domingo_switch) Switch swTituloDomingo;
    @BindView(R.id.segunda_txtTitulo) TextView txtTituloSegunda;
    @BindView(R.id.segunda_switch) Switch swTituloSegunda;
    @BindView(R.id.terca_txtTitulo) TextView txtTituloTerca;
    @BindView(R.id.terca_switch) Switch swTituloTerca;
    @BindView(R.id.quarta_txtTitulo) TextView txtTituloQuarta;
    @BindView(R.id.quarta_switch) Switch swTituloQuarta;
    @BindView(R.id.quinta_txtTitulo) TextView txtTituloQuinta;
    @BindView(R.id.quinta_switch) Switch swTituloQuinta;
    @BindView(R.id.sexta_txtTitulo) TextView txtTituloSexta;
    @BindView(R.id.sexta_switch) Switch swTituloSexta;
    @BindView(R.id.sabado_txtTitulo) TextView txtTituloSabado;
    @BindView(R.id.sabado_switch) Switch swTituloSabado;

    @BindView(R.id.ll_domingo) LinearLayout llDomingo;
    @BindView(R.id.ll_segunda) LinearLayout llSegunda;
    @BindView(R.id.ll_terca) LinearLayout llTerca;
    @BindView(R.id.ll_quarta) LinearLayout llQuarta;
    @BindView(R.id.ll_quinta) LinearLayout llQuinta;
    @BindView(R.id.ll_sexta) LinearLayout llSexta;
    @BindView(R.id.ll_sabado) LinearLayout llSabado;

    @BindView(R.id.domingo_txtHIni) TextView etDomingoIni;
    @BindView(R.id.domingo_txtHFim) TextView etDomingoFim;

    @BindView(R.id.segunda_txtHIni) TextView etSegundaIni;
    @BindView(R.id.segunda_txtHFim) TextView etSegundaFim;
    @BindView(R.id.terca_txtHIni) TextView etTercaIni;
    @BindView(R.id.terca_txtHFim) TextView etTercaFim;
    @BindView(R.id.quarta_txtHIni) TextView etQuartaIni;
    @BindView(R.id.quarta_txtHFim) TextView etQuartaFim;
    @BindView(R.id.quinta_txtHIni) TextView etQuintaIni;
    @BindView(R.id.quinta_txtHFim) TextView etQuintaFim;
    @BindView(R.id.sexta_txtHIni) TextView etSextaIni;
    @BindView(R.id.sexta_txtHFim) TextView etSextaFim;
    @BindView(R.id.sabado_txtHIni) TextView etSabadoIni;
    @BindView(R.id.sabado_txtHFim) TextView etSabadoFim;

    private static int idSelecionadoComponet;

    @Override
    public int getContentRes() {
        return R.layout.fragment_customer_register_horariofunc;
    }

    @Override
    public void initialize() {
        presenter.attachView(this);
        parentActivity.setKeyboardListenerActivated(false);
        btCancelar.setOnClickListener(view -> onCancelarClick());
        btConfirmar.setOnClickListener(view -> onConfirmarClick());
        spnTipoHorario.setCallback(item -> onTipoHorarioChanged(item.getIdValue()));

        swTituloDomingo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaDomingo();
            }
        });
        swTituloSegunda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaSegunda();
            }
        });
        swTituloTerca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaTerca();
            }
        });
        swTituloQuarta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaQuarta();
            }
        });
        swTituloQuinta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaQuinta();
            }
        });
        swTituloSexta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaSexta();
            }
        });
        swTituloSabado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onAtualizaSabado();
            }
        });

        presenter.initializeData(horarioFunc);

        // Inicializa os dados
        spnTipoHorario.doSelectWithCallback(EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue());

        etDomingoIni.setOnClickListener(v -> showTimePickerDialog(etDomingoIni));
        etDomingoFim.setOnClickListener(v -> showTimePickerDialog(etDomingoFim));
        etSegundaIni.setOnClickListener(v -> showTimePickerDialog(etSegundaIni));
        etSegundaFim.setOnClickListener(v -> showTimePickerDialog(etSegundaFim));
        etTercaIni.setOnClickListener(v -> showTimePickerDialog(etTercaIni));
        etTercaFim.setOnClickListener(v -> showTimePickerDialog(etTercaFim));
        etQuartaIni.setOnClickListener(v -> showTimePickerDialog(etQuartaIni));
        etQuartaFim.setOnClickListener(v -> showTimePickerDialog(etQuartaFim));
        etQuintaIni.setOnClickListener(v -> showTimePickerDialog(etQuintaIni));
        etQuintaFim.setOnClickListener(v -> showTimePickerDialog(etQuintaFim));
        etSextaIni.setOnClickListener(v -> showTimePickerDialog(etSextaIni));
        etSextaFim.setOnClickListener(v -> showTimePickerDialog(etSextaFim));
        etSabadoIni.setOnClickListener(v -> showTimePickerDialog(etSabadoIni));
        etSabadoFim.setOnClickListener(v -> showTimePickerDialog(etSabadoFim));
    }

    private void showTimePickerDialog(TextView tvHora) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener horaListner = (view, horas, minutos) -> {
            calendar.set(Calendar.HOUR_OF_DAY, horas);
            calendar.set(Calendar.MINUTE, minutos);

            String vHoras = "", vMinutos = "";
            vHoras = Util_IO.padLeft(String.valueOf(horas),2,"0");
            vMinutos = String.valueOf(minutos);
            if (String.valueOf(vMinutos).length()<=1)
            {
                vMinutos = String.valueOf(minutos) + "0";
            }

            tvHora.setText(vHoras + ":" + vMinutos);
        };

        new TimePickerDialog(getView().getContext(), R.style.AppTheme, horaListner, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity())).show();
    }

    private void onAtualizaDomingo() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            txtTituloDomingo.setVisibility(View.VISIBLE);
            if (swTituloDomingo.isChecked())
            {
                txtTituloDomingo.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloDomingo.setText("Fechado");
            }
        }
        else
        {
            if (swTituloDomingo.isChecked()) {
                txtTituloDomingo.setVisibility(View.GONE);
                llDomingo.setVisibility(View.VISIBLE);
                etDomingoIni.setText("08:00");
                etDomingoFim.setText("18:00");
            }
            else {
                llDomingo.setVisibility(View.GONE);
                txtTituloDomingo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaSegunda() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloSegunda.isChecked())
            {
                txtTituloSegunda.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloSegunda.setText("Fechado");
            }
        }
        else
        {
            if (swTituloSegunda.isChecked()) {
                txtTituloSegunda.setVisibility(View.GONE);
                llSegunda.setVisibility(View.VISIBLE);
                etSegundaIni.setText("08:00");
                etSegundaFim.setText("18:00");
            }
            else {
                llSegunda.setVisibility(View.GONE);
                txtTituloSegunda.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaTerca() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloTerca.isChecked())
            {
                txtTituloTerca.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloTerca.setText("Fechado");
            }
        }
        else
        {
            if (swTituloTerca.isChecked()) {
                txtTituloTerca.setVisibility(View.GONE);
                llTerca.setVisibility(View.VISIBLE);
                etTercaIni.setText("08:00");
                etTercaFim.setText("18:00");
            }
            else {
                llTerca.setVisibility(View.GONE);
                txtTituloTerca.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaQuarta() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloQuarta.isChecked())
            {
                txtTituloQuarta.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloQuarta.setText("Fechado");
            }
        }
        else
        {
            if (swTituloQuarta.isChecked()) {
                txtTituloQuarta.setVisibility(View.GONE);
                llQuarta.setVisibility(View.VISIBLE);
                etQuartaIni.setText("08:00");
                etQuartaFim.setText("18:00");
            }
            else {
                llQuarta.setVisibility(View.GONE);
                txtTituloQuarta.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaQuinta() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloQuinta.isChecked())
            {
                txtTituloQuinta.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloQuinta.setText("Fechado");
            }
        }
        else
        {
            if (swTituloQuinta.isChecked()) {
                txtTituloQuinta.setVisibility(View.GONE);
                llQuinta.setVisibility(View.VISIBLE);
                etQuintaIni.setText("08:00");
                etQuintaFim.setText("18:00");
            }
            else {
                llQuinta.setVisibility(View.GONE);
                txtTituloQuinta.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaSexta() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloSexta.isChecked())
            {
                txtTituloSexta.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloSexta.setText("Fechado");
            }
        }
        else
        {
            if (swTituloSexta.isChecked()) {
                txtTituloSexta.setVisibility(View.GONE);
                llSexta.setVisibility(View.VISIBLE);
                etSextaIni.setText("08:00");
                etSextaFim.setText("18:00");
            }
            else {
                llSexta.setVisibility(View.GONE);
                txtTituloSexta.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onAtualizaSabado() {
        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            if (swTituloSabado.isChecked())
            {
                txtTituloSabado.setText("Aberto 24 horas");
            }
            else
            {
                txtTituloSabado.setText("Fechado");
            }
        }
        else
        {
            if (swTituloSabado.isChecked()) {
                txtTituloSabado.setVisibility(View.GONE);
                llSabado.setVisibility(View.VISIBLE);
                etSabadoIni.setText("08:00");
                etSabadoFim.setText("18:00");
            }
            else {
                llSabado.setVisibility(View.GONE);
                txtTituloSabado.setVisibility(View.VISIBLE);
            }
        }
    }

    // Ajusta Componentes da Tela
    private void onTipoHorarioChanged(Integer idValue) {
        llDomingo.setVisibility(View.GONE);
        llSegunda.setVisibility(View.GONE);
        llTerca.setVisibility(View.GONE);
        llQuarta.setVisibility(View.GONE);
        llQuinta.setVisibility(View.GONE);
        llSexta.setVisibility(View.GONE);
        llSabado.setVisibility(View.GONE);

        if (idValue == 1)
        {
            String vTitulo = "Aberto 24 horas";
            txtTituloDomingo.setText(vTitulo);
            txtTituloSegunda.setText(vTitulo);
            txtTituloTerca.setText(vTitulo);
            txtTituloQuarta.setText(vTitulo);
            txtTituloQuinta.setText(vTitulo);
            txtTituloSexta.setText(vTitulo);
            txtTituloSabado.setText(vTitulo);
            swTituloDomingo.setChecked(true);
            swTituloSegunda.setChecked(true);
            swTituloTerca.setChecked(true);
            swTituloQuarta.setChecked(true);
            swTituloQuinta.setChecked(true);
            swTituloSexta.setChecked(true);
            swTituloSabado.setChecked(true);
        }
        else
        {
            String vTitulo = "Fechado";
            txtTituloDomingo.setText(vTitulo);
            txtTituloSegunda.setText(vTitulo);
            txtTituloTerca.setText(vTitulo);
            txtTituloQuarta.setText(vTitulo);
            txtTituloQuinta.setText(vTitulo);
            txtTituloSexta.setText(vTitulo);
            txtTituloSabado.setText(vTitulo);
            swTituloDomingo.setChecked(false);
            swTituloSegunda.setChecked(false);
            swTituloTerca.setChecked(false);
            swTituloQuarta.setChecked(false);
            swTituloQuinta.setChecked(false);
            swTituloSexta.setChecked(false);
            swTituloSabado.setChecked(false);
        }
    }

    private void onConfirmarClick() {
        List<CustomerRegisterHorarioFunc> listaHorarios = new ArrayList<CustomerRegisterHorarioFunc>();
        CustomerRegisterHorarioFunc registerHorarioFunc = new CustomerRegisterHorarioFunc();

        if (spnTipoHorario.getSelectedItem().getIdValue() == EnumRegisterHorarioFunc.SEMPREABERTA.getIdValue())
        {
            // Domingo
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(7);
            if (swTituloDomingo.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Segunda
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(1);
            if (swTituloSegunda.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Terça
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(2);
            if (swTituloTerca.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Quarta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(3);
            if (swTituloQuarta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Quinta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(4);
            if (swTituloQuinta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Sexta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(5);
            if (swTituloSexta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Sabado
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(6);
            if (swTituloSabado.isChecked())
            {
                registerHorarioFunc.setHorarioInicio("00:01");
                registerHorarioFunc.setHorarioFim("23:59");
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);
        }
        else
        {
            // Domingo
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(7);
            if (swTituloDomingo.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etDomingoIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etDomingoFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Segunda
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(1);
            if (swTituloSegunda.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etSegundaIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etSegundaFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Terça
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(2);
            if (swTituloTerca.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etTercaIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etTercaFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Quarta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(3);
            if (swTituloQuarta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etQuartaIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etQuartaFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Quinta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(4);
            if (swTituloQuinta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etQuintaIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etQuintaFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Sexta
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(5);
            if (swTituloSexta.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etSextaIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etSextaFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);

            // Sabado
            registerHorarioFunc = new CustomerRegisterHorarioFunc();
            registerHorarioFunc.setDiaAtendimentoId(6);
            if (swTituloSabado.isChecked())
            {
                registerHorarioFunc.setHorarioInicio(etSabadoIni.getText().toString());
                registerHorarioFunc.setHorarioFim(etSabadoFim.getText().toString());
                registerHorarioFunc.setAberto(0); // Aberto
            }
            else
            {
                registerHorarioFunc.setHorarioInicio("00:00");
                registerHorarioFunc.setHorarioFim("00:00");
                registerHorarioFunc.setAberto(1); // Fechado
            }
            listaHorarios.add(registerHorarioFunc);
        }

        presenter.doSave(listaHorarios);
    }

    private void onCancelarClick() {
        onBackPressed();
        return;
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
    public void onBackPressed() {
        parentActivity.setKeyboardListenerActivated(true);
        parentActivity.closeFragmentWithoutBottomBarFromHorarioFunc();
    }

    public static RegisterCustomerHorarioFuncFragment newInstance() {
        RegisterCustomerHorarioFuncFragment fragment = new RegisterCustomerHorarioFuncFragment();
        //fragment.setAddress(new CustomerRegisterAddress(address));
        //fragment.setOperationType(OPERATION_UPDATE);
        return fragment;
    }

    @Override
    public void carregaLista_Horario(List<ICustomSpinnerDialogModel> list) {
        spnTipoHorario.setList(list);
    }
}
