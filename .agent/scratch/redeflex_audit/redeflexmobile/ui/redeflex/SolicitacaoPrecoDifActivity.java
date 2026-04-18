package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolPD_ClienteAdapter;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.customedittext.CustomEditText;
import com.axys.redeflexmobile.ui.component.customspinner.CustomSpinner;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolicitacaoPrecoDifActivity extends AppCompatActivity implements ItemSolPD_ClienteAdapter.excluir {
    Button btnSalvar, btnCancelar;
    CustomEditText edtQuantidade;
    CustomEditText edtPrecoVenda;
    CustomEditText edtPrecoDif;
    CustomEditText edtDesconto;
    private static CustomEditText edtDataInicial;
    private static CustomEditText edtDataFinal;
    ImageButton btnCalendario;
    Button btnAddCliente;
    RecyclerView recycler_clientes;
    Cliente mCliente;
    private ArrayList<Produto> listaProdutos;
    private ArrayList<Cliente> listaCliente;
    private Produto prodSelecionado;
    private ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> listaItens;
    CustomSpinner spnCliente, spnProduto;

    ItemSolPD_ClienteAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_preco_dif);

        Bundle bundle = getIntent().getExtras();
        mCliente = (Cliente) bundle.getSerializable("Cliente");

        criarObjetos();
        montarActionBar();
        criarEventos();
        listaItens = new ArrayList<>();
        AtualizaRecycler();
    }

    private void criarEventos() {
        btnCancelar.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(SolicitacaoPrecoDifActivity.this)
                    .setTitle(getString(R.string.app_name))
                    .setIcon(R.mipmap.ic_icone_new)
                    .setMessage("Deseja realmente cancelar a solicitação?")
                    .setPositiveButton(R.string.sim, (dialogInterface, i) -> {
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    })
                    .setNegativeButton(R.string.nao, (dialogInterface, i) -> {
                    })
                    .create();
            dialog.show();
        });

        btnSalvar.setOnClickListener(view -> {
            if (listaItens.size() <= 0) {
                Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Deve ser informado Cliente. Verifique!");
                alerta.show();
                return;
            }

            DBColaborador dbColaborador = new DBColaborador(this);
            Colaborador colaborador = dbColaborador.get();

            // Grava o cabeçalho
            SolicitacaoPrecoDiferenciado solicitacao = new SolicitacaoPrecoDiferenciado();
            solicitacao.setIdVendedor(colaborador.getId());
            solicitacao.setNomeSolicitante(colaborador.getNome());
            solicitacao.setDataSolicitacao(new Date());
            solicitacao.setDataCriacao(new Date());
            solicitacao.setTipoId(1);
            solicitacao.setDataInicial(Util_IO.stringToDate(edtDataInicial.getText().toString()));
            solicitacao.setDataFinal(Util_IO.stringToDate(edtDataFinal.getText().toString()));
            try {
                long pCodSolicitacao = new BDSolicitacaoPrecoDiferenciado(SolicitacaoPrecoDifActivity.this).addSolicitacao(solicitacao);

                // Grava os Itens
                for (SolicitacaoPrecoDiferenciadoDetalhe item : listaItens) {
                    item.setIdSolicitacao((int) pCodSolicitacao);
                    item.setAplicada("N");
                    new BDSolicitacaoPrecoDiferenciadoDetalhe(SolicitacaoPrecoDifActivity.this).addSolicitacaoDetalhes(item);
                }

                Utilidades.retornaMensagem(SolicitacaoPrecoDifActivity.this, "Solicitação realizada com sucesso!", true);
                setResult(Activity.RESULT_OK);
                finish();
            } catch (Exception ex) {
                Mensagens.mensagemErro(SolicitacaoPrecoDifActivity.this, ex.getMessage(), false);
                return;
            }
        });

        spnProduto.setCallback(item -> {
            prodSelecionado = new DBEstoque(SolicitacaoPrecoDifActivity.this).getProdutoById(Util_IO.padLeft(String.valueOf(spnProduto.getSelectedItemId()), 5, "0"));

            // Defina o formato para moeda brasileira
            NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String valorFormatado = formatoMoeda.format(prodSelecionado.getPrecovenda());
            edtPrecoVenda.setText(valorFormatado);
            edtPrecoDif.setText("0");
            edtDesconto.setText("0");
            edtQuantidade.setText("");

            if (prodSelecionado != null) {
                edtQuantidade.setEnabled(true);
                edtPrecoDif.setEnabled(true);
            } else {
                edtQuantidade.setEnabled(false);
                edtPrecoDif.setEnabled(false);
            }
        });

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        btnAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnCliente.getSelectedItemId() == null || spnCliente.getSelectedItemId() == 0) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Selecione um Cliente!");
                    alerta.show();
                    return;
                }

                // Valida dados do Produto Preenchido
                if (prodSelecionado == null) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Informe o Produto!");
                    alerta.show();
                    return;
                }

                // Valida Quantidade
                if (Util_IO.isNullOrEmpty(edtQuantidade.getText().toString())) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Quantidade não informada, Verifique");
                    alerta.show();
                    return;
                } else if (Integer.parseInt(edtQuantidade.getText().toString()) == 0) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Quantidade não informada, Verifique");
                    alerta.show();
                    return;
                }

                // Valida Preço Diferenciado
                if (Util_IO.isNullOrEmpty(edtPrecoDif.getText().toString())) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Preço Diferenciado não informado, Verifique");
                    alerta.show();
                    return;
                } else if (edtPrecoDif.getCurrencyDouble() == 0) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Preço Diferenciado não informado, Verifique");
                    alerta.show();
                    return;
                }

                // Valida Periodo Inicial e Final
                try {
                    String periodo = edtDataInicial.getText().toString();
                    if (periodo.isEmpty()) {
                        Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Data inicial não informada, Verifique");
                        alerta.show();
                        return;
                    }

                    Date dInicial = Util_IO.getDateByInput(periodo);
                    periodo = edtDataFinal.getText().toString();
                    if (periodo.isEmpty()) {
                        Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Data final não informada, Verifique");
                        alerta.show();
                        return;
                    }

                    Date dFinal = Util_IO.getDateByInput(periodo);
                    if (dFinal.before(dInicial)) {
                        Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Data final não pode ser menor que inicial, Verifique");
                        alerta.show();
                        return;
                    }
                } catch (Exception ex) {
                    Mensagens.mensagemErro(SolicitacaoPrecoDifActivity.this, ex.getMessage(), false);
                }

                // Filtrar a lista e atribuir a uma nova lista ou fazer alguma operação
                int idCliente = spnCliente.getSelectedItemId();
                int idProduto = spnProduto.getSelectedItemId();

                List<SolicitacaoPrecoDiferenciadoDetalhe> newList = listaItens.stream()
                        .filter(objeto -> objeto.getIdCliente() == idCliente && Integer.valueOf(objeto.getItemCode()) == idProduto)
                        .collect(Collectors.toList());

                if (newList != null && newList.size() > 0) {
                    Alerta alerta = new Alerta(SolicitacaoPrecoDifActivity.this, SolicitacaoPrecoDifActivity.this.getResources().getString(R.string.app_name), "Cliente/Produto já informado. Verifique!");
                    alerta.show();
                    return;
                } else {
                    DBColaborador dbColaborador = new DBColaborador(SolicitacaoPrecoDifActivity.this);
                    Colaborador colaborador = dbColaborador.get();

                    // Carrega os dados do Cliente
                    Cliente cliSelect = new DBCliente(SolicitacaoPrecoDifActivity.this).getById(String.valueOf(spnCliente.getSelectedItemId()));

                    SolicitacaoPrecoDiferenciadoDetalhe item = new SolicitacaoPrecoDiferenciadoDetalhe();
                    item.setIdCliente(Integer.parseInt(cliSelect.getId()));
                    item.setCodigoClienteSGV(cliSelect.getCodigoSGV());
                    item.setItemCode(prodSelecionado.getId());
                    item.setPrecoDiferenciado(edtPrecoDif.getCurrencyDouble());
                    item.setPrecoVenda(prodSelecionado.getPrecovenda());
                    item.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString()));
                    item.setTipoPagamentoId(1);
                    item.setStatusId(0);
                    item.setIdVendedor(colaborador.getId());
                    listaItens.add(item);
                    AtualizaRecycler();
                }
            }
        });

        edtPrecoDif.addTextChangedListener(new TextWatcher() {
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

                if (!Util_IO.isNullOrEmpty(edtPrecoDif.getText().toString())) {
                    if (!Util_IO.isNullOrEmpty(edtPrecoVenda.getText().toString())) {
                        double vDesconto = 0.00;
                        if (edtPrecoVenda.getCurrencyDouble() > edtPrecoDif.getCurrencyDouble()) {
                            // Calcula o Valor de Desconto
                            vDesconto = edtPrecoVenda.getCurrencyDouble() - edtPrecoDif.getCurrencyDouble();
                        }

                        String valorFormatado = formatoMoeda.format(vDesconto);
                        edtDesconto.setText(valorFormatado);
                    } else {
                        String valorFormatado = formatoMoeda.format(0.00);
                        edtDesconto.setText(valorFormatado);
                    }
                } else {
                    String valorFormatado = formatoMoeda.format(0.00);
                    edtDesconto.setText(valorFormatado);
                }
            }
        });
    }

    private void criarObjetos() {
        btnCancelar = findViewById(R.id.solPD_btnCancelar);
        btnSalvar = findViewById(R.id.solPD_btnSalvar);
        btnAddCliente = findViewById(R.id.solPD_btnAddCliente);

        // Monta Spinner Produto
        listaProdutos = new DBEstoque(this).getProdutosComEstoque();
        spnProduto = findViewById(R.id.solPD_spnProduto);
        fillProduto(new ArrayList<>(listaProdutos));

        // Monta Spinner Cliente
        listaCliente = new DBCliente(this).getClientes("");
        spnCliente = findViewById(R.id.solPD_spnCliente);
        fillCliente(new ArrayList<>(listaCliente));

        edtQuantidade = findViewById(R.id.solPD_edtQuantidade);
        edtPrecoVenda = findViewById(R.id.solPD_edtPrecoVenda);
        edtPrecoDif = findViewById(R.id.solPD_edtPrecoDif);
        edtDesconto = findViewById(R.id.solPD_edtDesconto);

        edtDataInicial = findViewById(R.id.solPD_edtDataInicial);
        edtDataFinal = findViewById(R.id.solPD_edtDataFinal);
        btnCalendario = findViewById(R.id.solPD_btnCalendario);

        recycler_clientes = (RecyclerView) findViewById(R.id.solPD_recycler_clientes);
        recycler_clientes.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o Valor das Datas
        edtDataInicial.setText(getDataAtual());
        edtDataFinal.setText(UltimoDiadoMes(getDataAtual()));

        // Inicia sem poder informar quantidade e preço diferenciado até que o produto seja selecionado
        edtQuantidade.setEnabled(false);
        edtPrecoDif.setEnabled(false);
    }

    public void fillProduto(List<ICustomSpinnerDialogModel> list) {
        spnProduto.setList(list);
    }

    public List<ICustomSpinnerDialogModel> distinctClients(List<ICustomSpinnerDialogModel> list) {
        Set<String> seen = new HashSet<>();
        return list.stream()
                .filter(item -> seen.add(item.getDescriptionValue()))
                .collect(Collectors.toList());
    }

    public void fillCliente(List<ICustomSpinnerDialogModel> list) {
        spnCliente.setList(distinctClients(list));
        if (mCliente != null)
            spnCliente.doSelect(mCliente);
    }

    private void montarActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitar Preço Diferenciado");
    }

    protected void onResume() {
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    protected void onPause() {
        SimpleDbHelper.INSTANCE.close();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private String getDataAtual() {
        SimpleDateFormat dateF = new SimpleDateFormat(("dd/MM/yyyy"), Locale.getDefault());
        return dateF.format(Calendar.getInstance().getTime());
    }

    public static String UltimoDiadoMes(String dateString) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date data = formato.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);

            // Definindo o dia do mês como o último dia do mês
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            return formato.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void AtualizaRecycler() {
        itemsAdapter = new ItemSolPD_ClienteAdapter(listaItens, SolicitacaoPrecoDifActivity.this, this);
        recycler_clientes.setAdapter(itemsAdapter);

        if (listaItens.size() > 0) {
            btnCalendario.setEnabled(false);
        } else {
            btnCalendario.setEnabled(true);
        }
    }

    @Override
    public void onExcluirClick(int pos) {
        listaItens.remove(pos);
        AtualizaRecycler();
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = df.parse(day + "-" + (month + 1) + "-" + year);
                edtDataInicial.setText(Util_IO.dateTimeToString(date, "dd/MM/yyyy"));
                edtDataFinal.setText(UltimoDiadoMes(Util_IO.dateTimeToString(date, "dd/MM/yyyy")));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
}

