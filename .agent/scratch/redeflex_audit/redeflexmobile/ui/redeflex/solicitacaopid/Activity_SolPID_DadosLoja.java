package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolPD_ClienteAdapter;
import com.axys.redeflexmobile.shared.adapters.SolicitacaoPidRedeAdapter;
import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRede;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;
import com.axys.redeflexmobile.ui.redeflex.SolicitacaoPrecoDifActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Activity_SolPID_DadosLoja extends AppCompatActivity implements SolicitacaoPidRedeAdapter.excluir {
    CustomEditText edtFaturamentoPrevisto;
    CustomEditText edtCpfCnpj, edtPercentual;
    CustomSpinner spnMCC, spnTipoPessoa;
    TextView tvValorLoja;
    Button btnAddNovaLoja;
    RecyclerView recycler_RedeLojas;
    private ArrayList<SolicitacaoPidRede> listaItens;
    SolicitacaoPidRedeAdapter itemsAdapter;
    List<TaxaMdr> taxas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sol_pid_dados_loja);

        // Carrega o Objeto que for passado
        Bundle data = (Bundle) getIntent().getExtras();
        listaItens = (ArrayList<SolicitacaoPidRede>) data.getSerializable("Lista_Redes");
        if (listaItens == null)
            listaItens = new ArrayList<>();

        // Titulo da Barra de Titulo Formulario
        String pTitulo = data.getString("Titulo");

        criarObjetos();
        montarActionBar(pTitulo);
        criarEventos();
        AtualizaRecycler();
    }

    private void criarEventos() {
        spnTipoPessoa.setCallback(item -> {
            if (item.getIdValue() == PHYSICAL.getIdValue()) {
                edtCpfCnpj.setLabel("CPF");
                edtCpfCnpj.setMask(CPF);
                edtCpfCnpj.clearValue();
            } else {
                edtCpfCnpj.setLabel("CNPJ");
                edtCpfCnpj.setMask(CNPJ);
                edtCpfCnpj.clearValue();
            }

            taxas = new DBTaxaMdr(Activity_SolPID_DadosLoja.this).getAllMccByPersonType(item.getIdValue());
            iniciarMcc(new ArrayList<>(taxas));
        });

        btnAddNovaLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validações
                if (Util_IO.isNullOrEmpty(edtFaturamentoPrevisto.getText().toString()) || edtFaturamentoPrevisto.getCurrencyDouble() <= 0) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "Faturamento Total de Rede não informado. Verifique!");
                    alerta.show();
                    return;
                }

                if (spnTipoPessoa.getSelectedItem().getIdValue() == 0) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "Tipo de Pessoa não informado. Verifique!");
                    alerta.show();
                    return;
                }
                else
                {
                    if (spnTipoPessoa.getSelectedItemId() == 1)
                    {
                        if (!Validacoes.cpfValido(edtCpfCnpj.getText()))
                        {
                            Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "CPF informado inválido. Verifique!");
                            alerta.show();
                            return;
                        }
                    }
                    else
                    {
                        if (!Validacoes.cnpjValido(edtCpfCnpj.getText()))
                        {
                            Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "Cnpj informado inválido. Verifique!");
                            alerta.show();
                            return;
                        }
                    }
                }

                if (Util_IO.isNullOrEmpty(edtPercentual.getText().toString()) || edtPercentual.getCurrencyDouble() <= 0) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "% TPV não informado. Verifique!");
                    alerta.show();
                    return;
                }

                if (spnMCC.getSelectedItemId() == null || spnMCC.getSelectedItemId() == 0) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosLoja.this, Activity_SolPID_DadosLoja.this.getResources().getString(R.string.app_name), "Informe o MCC da Loja!");
                    alerta.show();
                    return;
                }

                SolicitacaoPidRede pidRede = new SolicitacaoPidRede();
                pidRede.setCpfCnpj(StringUtils.returnOnlyNumbers(edtCpfCnpj.getText()));
                pidRede.setTpvPorcentagem(edtPercentual.getCurrencyDouble());
                pidRede.setMcc(spnMCC.getSelectedItemId().toString());

                double vVavlorTPV = 0.00;
                vVavlorTPV = (edtFaturamentoPrevisto.getCurrencyDouble() * edtPercentual.getCurrencyDouble()) / 100;
                pidRede.setTpvTotal(vVavlorTPV);

                listaItens.add(pidRede);
                AtualizaRecycler();

                // Limpa campos
                edtPercentual.clearValue();
                edtCpfCnpj.clearValue();
                spnMCC.removeSelection();
            }
        });

        edtPercentual.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Defina o formato para moeda brasileira
                NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

                if (!Util_IO.isNullOrEmpty(edtPercentual.getText().toString())) {
                    if (!Util_IO.isNullOrEmpty(edtFaturamentoPrevisto.getText().toString())) {
                        double vVavlorTPV = 0.00;

                        vVavlorTPV = (edtFaturamentoPrevisto.getCurrencyDouble() * edtPercentual.getCurrencyDouble()) / 100;
                        String valorFormatado = formatoMoeda.format(vVavlorTPV);
                        tvValorLoja.setText("VALOR: " + valorFormatado);
                    } else {
                        String valorFormatado = formatoMoeda.format(0.00);
                        tvValorLoja.setText("VALOR: " + valorFormatado);
                    }
                } else {
                    String valorFormatado = formatoMoeda.format(0.00);
                    tvValorLoja.setText("VALOR: " + valorFormatado);
                }
            }
        });
    }

    private void criarObjetos() {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        edtFaturamentoPrevisto = findViewById(R.id.llCadLoja_edtFaturamentoPrevisto);
        edtCpfCnpj = findViewById(R.id.llCadLoja_edtCpfCNPJ);
        edtPercentual = findViewById(R.id.llCadLoja_edtTPV);

        tvValorLoja = findViewById(R.id.llCadLoja_tvValor);
        String valorFormatado = formatoMoeda.format(0.00);
        tvValorLoja.setText("VALOR: " + valorFormatado);

        spnMCC = findViewById(R.id.llCadLoja_spnMCC);
        btnAddNovaLoja = findViewById(R.id.llCadLoja_btnAddCliente);

        spnTipoPessoa = findViewById(R.id.llCadLoja_spnTipoPessoa);
        iniciarTipoPessoa(new ArrayList<>(EnumRegisterPersonType.getList()));

        recycler_RedeLojas = (RecyclerView) findViewById(R.id.llCadLoja_recycler_lojas);
        recycler_RedeLojas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void montarActionBar(String pTitulo) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pTitulo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Dados_loja", listaItens);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarTipoPessoa(List<ICustomSpinnerDialogModel> list) {
        spnTipoPessoa.setList(list);
    }

    private void iniciarMcc(List<ICustomSpinnerDialogModel> list) {
        spnMCC.removeSelection();
        spnMCC.setList(list);
    }

    private void AtualizaRecycler() {
        itemsAdapter = new SolicitacaoPidRedeAdapter(listaItens, Activity_SolPID_DadosLoja.this, this);
        recycler_RedeLojas.setAdapter(itemsAdapter);

        if (listaItens != null && listaItens.size()>0)
            edtFaturamentoPrevisto.setEnabled(false);
        else
            edtFaturamentoPrevisto.setEnabled(true);
    }

    @Override
    public void onExcluirClick(int pos) {
        listaItens.remove(pos);
        AtualizaRecycler();
    }
}