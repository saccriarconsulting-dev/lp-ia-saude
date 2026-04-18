package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_DOUBLE;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CNPJ;
import static com.axys.redeflexmobile.ui.component.customedittext.CustomEditText.ComponentEditTextMask.CPF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.SolicitacaoPidPOSAdapter;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.bd.DBOperadora;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidPos;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.ArrayList;
import java.util.List;

public class Activity_SolPID_DadosPOS extends AppCompatActivity implements SolicitacaoPidPOSAdapter.excluir {
    private ArrayList<SolicitacaoPidPos> listaItens;
    SolicitacaoPidPOSAdapter itemsAdapter;

    CustomSpinner spnModeloPOS, spnTipoConexao, spnOperadora;
    CustomEditText edtValorUnitario, edtMetragemCabo;
    Button btnAddNovaPOS;
    RecyclerView recycler_POS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sol_pid_dados_pos);

        // Carrega o Objeto que for passado
        Bundle data = (Bundle) getIntent().getExtras();
        listaItens = (ArrayList<SolicitacaoPidPos>) data.getSerializable("Lista_POS");
        if (listaItens == null)
            listaItens = new ArrayList<>();

        // Titulo da Barra de Titulo Formulario
        String pTitulo = data.getString("Titulo");

        criarObjetos();
        montarActionBar(pTitulo);
        criarEventos();
        AtualizaRecycler();
    }

    private void AtualizaRecycler() {
        itemsAdapter = new SolicitacaoPidPOSAdapter(listaItens, Activity_SolPID_DadosPOS.this, this);
        recycler_POS.setAdapter(itemsAdapter);
    }

    private void criarEventos() {
        spnModeloPOS.setCallback(item -> {
            iniciarspnTipoConexao(new ArrayList<>(new DBModeloPOS(this).obterModelosPOSConexoes(item.getIdValue())));
            ModeloPOS modeloPOS = new DBModeloPOS(this).getById(item.getIdValue());
            if (modeloPOS != null)
                edtValorUnitario.setText(StringUtils.maskCurrencyDouble(modeloPOS.getValorAluguelPadrao()));
            else
                edtValorUnitario.setText(String.valueOf(EMPTY_DOUBLE));
        });

        btnAddNovaPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnModeloPOS.getSelectedItem() == null) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosPOS.this, Activity_SolPID_DadosPOS.this.getResources().getString(R.string.app_name), "Modelo POS não informado. Verifique!");
                    alerta.show();
                    return;
                }

                if (spnTipoConexao.getSelectedItem() == null) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosPOS.this, Activity_SolPID_DadosPOS.this.getResources().getString(R.string.app_name), "Tipo de Conexão não informado. Verifique!");
                    alerta.show();
                    return;
                }

                if (spnOperadora.getSelectedItem() == null) {
                    Alerta alerta = new Alerta(Activity_SolPID_DadosPOS.this, Activity_SolPID_DadosPOS.this.getResources().getString(R.string.app_name), "Operadora não informada. Verifique!");
                    alerta.show();
                    return;
                }

                SolicitacaoPidPos pidPOS = new SolicitacaoPidPos();
                pidPOS.setTipoMaquinaId(spnModeloPOS.getSelectedItem().getIdValue());
                pidPOS.setTipoConexaoId(spnTipoConexao.getSelectedItem().getIdValue());
                pidPOS.setIdOperadora(spnOperadora.getSelectedItem().getIdValue());
                pidPOS.setQuantidade(1);
                pidPOS.setValorAluguel(edtValorUnitario.getCurrencyDouble());
                if (!Util_IO.isNullOrEmpty(edtMetragemCabo.getText()))
                    pidPOS.setMetragemCabo(Integer.valueOf(edtMetragemCabo.getText()));
                listaItens.add(pidPOS);
                AtualizaRecycler();
            }
        });
    }

    private void criarObjetos() {
        spnModeloPOS = findViewById(R.id.llCadPOS_spnModeloPOS);
        spnTipoConexao = findViewById(R.id.llCadPOS_spnTpConexao);
        spnOperadora = findViewById(R.id.llCadPOS_spnOperadora);

        edtValorUnitario = findViewById(R.id.llCadPOS_edtValorUnit);
        edtMetragemCabo = findViewById(R.id.llCadPOS_edtMetragemCabo);
        btnAddNovaPOS = findViewById(R.id.llCadPOS_btnAddPOS);

        recycler_POS = findViewById(R.id.llCadPOS_recycler_POS);
        recycler_POS.setLayoutManager(new LinearLayoutManager(this));

        iniciarspnModeloPOS(new ArrayList<>(new DBModeloPOS(this).obterModelosPOS()));
        iniciarSpnOperadora(new ArrayList<>(new DBOperadora(Activity_SolPID_DadosPOS.this).buscaOperadoras()));
        edtValorUnitario.setText(String.valueOf(EMPTY_DOUBLE));
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
                returnIntent.putExtra("Dados_POS", listaItens);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarspnModeloPOS(List<ICustomSpinnerDialogModel> list) {
        spnModeloPOS.setList(list);
    }

    private void iniciarspnTipoConexao(List<ICustomSpinnerDialogModel> list) {
        spnTipoConexao.setList(list);
    }

    private void iniciarSpnOperadora(List<ICustomSpinnerDialogModel> list) {
        spnOperadora.setList(list);
    }

    @Override
    public void onExcluirClick(int pos) {
        listaItens.remove(pos);
        AtualizaRecycler();
    }
}