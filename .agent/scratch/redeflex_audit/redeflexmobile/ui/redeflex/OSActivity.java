package com.axys.redeflexmobile.ui.redeflex;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.OS;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OSActivity extends AppCompatActivity {
    static Button btnSelecionarData, btnSelecionarHora;
    OS ordemServico;
    EditText txtObsVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int iCodigo;
        Bundle parametros = getIntent().getExtras();
        if (parametros != null) {
            criarObjetos();

            iCodigo = parametros.getInt(Config.CodigoItemOS);
            if (iCodigo > 0) {
                Notificacoes.cancelarNotificacao(OSActivity.this, iCodigo);

                final DBOs dbOs = new DBOs(OSActivity.this);
                ordemServico = dbOs.get(iCodigo);

                if (ordemServico != null) {
                    Cliente cliente = new DBCliente(OSActivity.this).getById(String.valueOf(ordemServico.getIdCliente()));
                    if (cliente != null) {
                        final String numberPhone = Util_IO.isNullOrEmpty(Util_IO.formataTelefone(cliente.getDddCelular(), cliente.getCelular())) ?
                                Util_IO.formataTelefone(cliente.getDddTelefone(), cliente.getTelefone()) : Util_IO.formataTelefone(cliente.getDddCelular(), cliente.getCelular());
                        ((TextView) findViewById(R.id.txtTelefone)).setText(numberPhone);
                        ((TextView) findViewById(R.id.txtCliente)).setText(cliente.getNomeFantasia());
                        ((ImageButton) findViewById(R.id.btnLigar)).setOnClickListener((view) -> {
                            Utilidades.openTelaDiscagem(OSActivity.this, numberPhone);
                        });
                    } else
                        ((TextView) findViewById(R.id.txtCliente)).setText(ordemServico.getNomeCliente());
                    ((TextView) findViewById(R.id.txtDataOs)).setText(Util_IO.dateTimeToString(ordemServico.getData(), Config.FormatDateStringBr));
                    ((TextView) findViewById(R.id.txtServico)).setText(ordemServico.getDescricaoTipo());
                    ((TextView) findViewById(R.id.txtObs)).setText(ordemServico.getObs());

                    btnSelecionarData.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            showDatePickerDialog(v);
                        }
                    });

                    btnSelecionarHora.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            showTimePickerDialog(v);
                        }
                    });

                    Button btn = (Button) findViewById(R.id.btnAgendarOs);

                    if (ordemServico.getDataAgendamento() != null) {
                        getSupportActionBar().setTitle("Atender OS: " + String.valueOf(ordemServico.getId()));
                        ((TextView) findViewById(R.id.txtAgendamento)).setText("Atendimento agendado para:");
                        btnSelecionarData.setText(Util_IO.dateTimeToString(ordemServico.getDataAgendamento(), Config.FormatDateStringBr));
                        btnSelecionarData.setEnabled(false);

                        btnSelecionarHora.setText(Util_IO.timeToString(ordemServico.getDataAgendamento()));
                        btnSelecionarHora.setEnabled(false);

                        txtObsVendedor.setVisibility(View.VISIBLE);

                        btn.setText("Registrar atendimento");
                        btn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String observacao = txtObsVendedor.getText().toString().trim();

                                if (Util_IO.isNullOrEmpty(observacao)) {
                                    Alerta alerta = new Alerta(OSActivity.this, getResources().getString(R.string.app_name), "Favor descrever como foi o atendimento");
                                    alerta.show();
                                    return;
                                }

                                dbOs.setOsAtendida(ordemServico.getId());
                                dbOs.incluirObservacao(Util_IO.retiraCaracterEspecial(observacao), ordemServico.getId());
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    } else {
                        getSupportActionBar().setTitle("Agendar OS: " + String.valueOf(ordemServico.getId()));
                        ((TextView) findViewById(R.id.txtAgendamento)).setText("Informe data/hora que você irá atender:");
                        btnSelecionarData.setEnabled(true);
                        btnSelecionarData.setText(Util_IO.dateTimeToString(new Date(), "dd/MM/yyyy"));

                        btnSelecionarHora.setEnabled(true);
                        btnSelecionarHora.setText(Util_IO.timeToString(new Date()));

                        btn.setText("Agendar");
                        btn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Date date = Util_IO.stringToDate(btnSelecionarData.getText().toString() + " " + btnSelecionarHora.getText().toString(), "dd/MM/yyyy HH:mm");

                                if (date.before(new Date())) {
                                    Alerta alerta = new Alerta(OSActivity.this, getResources().getString(R.string.app_name), "Selecione uma data superior ou igual a hoje.");
                                    alerta.show();
                                } else {
                                    if (ordemServico.getIdClasse() > 0 && date.after(ordemServico.getDataLimiteAtend()) && ordemServico.getDataLimiteAtend().after(new Date())) {
                                        Alerta alerta = new Alerta(OSActivity.this, getResources().getString(R.string.app_name)
                                                , "Selecione uma data para atendimento até " + Util_IO.dateTimeToString(ordemServico.getDataLimiteAtend(), "dd/MM/yyyy HH:mm"));
                                        alerta.show();
                                    } else {
                                        dbOs.setOsAgenda(ordemServico.getId(), date);
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                }
                            }
                        });
                    }
                    if (ordemServico.getDataVisualizacao() == null)
                        dbOs.setOsVisualizacao(ordemServico.getId());
                } else {
                    Mensagens.ordemServicoNaoEncontrada(OSActivity.this);
                }
            } else {
                Mensagens.ordemServicoNaoEncontrada(OSActivity.this);
            }
        } else {
            Mensagens.ordemServicoNaoEncontrada(OSActivity.this);
        }
    }

    private void criarObjetos() {
        btnSelecionarData = (Button) findViewById(R.id.btnSelecionarData);
        txtObsVendedor = (EditText) findViewById(R.id.txtObsVendedor);
        btnSelecionarHora = (Button) findViewById(R.id.btnSelecionarHora);
    }

    @Override
    protected void onResume() {
        if (!Utilidades.verificarHorarioComercial(OSActivity.this, true)) {
            Mensagens.horarioComercial(OSActivity.this);
        }
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    public void showTimePickerDialog(View v) {
        try {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            finish();
        }
    }

    public void showDatePickerDialog(View v) {
        try {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            finish();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), R.style.DialogTheme, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            btnSelecionarHora.setText(hourOfDay + ":" + minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = df.parse(day + "-" + (month + 1) + "-" + year);
                btnSelecionarData.setText(Util_IO.dateTimeToString(date, "dd/MM/yyyy"));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    @SuppressWarnings("TryWithIdenticalCatches")
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    break;
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}