package com.axys.redeflexmobile.ui.redeflex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.NovoAnexoAdapter;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBDepartamentos;
import com.axys.redeflexmobile.shared.bd.DBFilial;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Departamento;
import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.models.NovoAnexo;
import com.axys.redeflexmobile.shared.services.tasks.ChamadosTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.UtilAdapter;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.InputText;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class NovoChamadoActivity extends AppCompatActivity {
    private static final int DELAY_TO_NEXT_ACTION = 1200;

    SearchableSpinner ddlFilial, ddlDepartamento;
    ArrayList<Filial> listaFiliais;
    ArrayList<Departamento> listaDepartamentos;
    EditText txtDescricao;
    Filial filial;
    Departamento departamento;
    InputText txtAssunto;
    ArrayList<NovoAnexo> listaAnexos;
    ListView listaItens;
    ProgressDialog mDialog;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<Integer> menuEventos = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_chamado);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Novo Chamado");
        }

        try {
            criarObjetos();
            alimentarDados();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoChamadoActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        ddlFilial = (SearchableSpinner) findViewById(R.id.ddlFilial);
        Utilidades.defineSpinner(ddlFilial);
        ddlDepartamento = (SearchableSpinner) findViewById(R.id.ddlDepartamento);
        Utilidades.defineSpinner(ddlDepartamento);
        listaFiliais = new DBFilial(NovoChamadoActivity.this).getFiliais();
        listaDepartamentos = new DBDepartamentos(NovoChamadoActivity.this).getDepartamentos();
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtAssunto = (InputText) findViewById(R.id.txtAssunto);
        listaItens = (ListView) findViewById(R.id.listaAnexos);
    }

    private void alimentarDados() {
        ddlFilial.setAdapter(UtilAdapter.adapterFilial(this, listaFiliais));
        ddlDepartamento.setAdapter(UtilAdapter.adapterDepartamento(this, listaDepartamentos));
    }

    private void criarEventos() {
        txtDescricao.addTextChangedListener(new TextWatcher() {
            TextView txtExibir = (TextView) findViewById(R.id.txtExibirDescricao);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0)
                    txtExibir.setVisibility(View.GONE);
                else
                    txtExibir.setVisibility(View.VISIBLE);
            }
        });

        ddlDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departamento = listaDepartamentos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddlFilial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filial = listaFiliais.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Disposable disposable = menuEventos.throttleFirst(DELAY_TO_NEXT_ACTION, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(menuId -> {
                    if (menuId == android.R.id.home) {
                        Alerta alerta = new Alerta(NovoChamadoActivity.this, getResources().getString(R.string.app_name), "Deseja realmente sair? As informações serão perdidas!");
                        alerta.showConfirm((dialog, which) -> {
                            finish();
                        }, null);
                    } else if (menuId == R.id.menu_salvar_chamado) {
                        salvar();
                    } else if (menuId == R.id.menu_galeria_chamado) {
                        loadImagefromGallery();
                    } else if (menuId == R.id.menu_limpar_anexo) {
                        listaAnexos = new ArrayList<>();
                        atualizaLista();
                    }
                }, Timber::e);
        disposables.add(disposable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chamados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuEventos.onNext(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void salvar() {
        try {
            String filial_valor = "", departamento_valor = "", assunto = "", descricao = "";

            if (filial != null)
                filial_valor = String.valueOf(filial.getId());

            if (departamento != null)
                departamento_valor = String.valueOf(departamento.getId());

            assunto = txtAssunto.getText().trim();
            descricao = txtDescricao.getText().toString().trim();

            if (Util_IO.isNullOrEmpty(filial_valor)) {
                Utilidades.retornaMensagem(NovoChamadoActivity.this, "Filial não selecionada, Verifique", true);
                return;
            }

            if (Util_IO.isNullOrEmpty(departamento_valor)) {
                Utilidades.retornaMensagem(NovoChamadoActivity.this, "Departamento não selecionado, Verifique", true);
                return;
            }

            if (Util_IO.isNullOrEmpty(assunto)) {
                txtAssunto.requestFocus();
                txtAssunto.setError("Atenção, Necessário Preenchimento do Assunto");
                return;
            }

            if (Util_IO.isNullOrEmpty(descricao)) {
                txtDescricao.requestFocus();
                txtDescricao.setError("Atenção, Necessário Preenchimento da Descrição");
                return;
            }

            mDialog = new ProgressDialog(NovoChamadoActivity.this);
            mDialog.setMessage("Aguarde...");
            mDialog.setIndeterminate(true);
            mDialog.setCancelable(false);
            mDialog.show();

            final int pFilial = Integer.valueOf(filial_valor);
            final int pDepartamento = Integer.valueOf(departamento_valor);
            final String pAssunto = Util_IO.trataString(assunto);
            final String pDescricao = Util_IO.trataString(descricao);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Colaborador colaborador = new DBColaborador(NovoChamadoActivity.this).get();
                            Chamado chamado = new Chamado();
                            chamado.setDataCadastro(new Date());
                            chamado.setFilialID(pFilial);
                            chamado.setDepartamentoID(pDepartamento);
                            chamado.setSolicitanteID(colaborador.getId());
                            chamado.setSolicitante(colaborador.getNome());
                            chamado.setAssunto(pAssunto);
                            chamado.setStatusID(1);
                            new DBChamados(NovoChamadoActivity.this).addChamadoMobile(chamado, pDescricao, listaAnexos);
                            mDialog.dismiss();
                            new ChamadosTask(NovoChamadoActivity.this, 1).execute();
                            Utilidades.retornaMensagem(NovoChamadoActivity.this, "Chamado incluído com sucesso", false);
                            finish();
                        }
                    });
                }
            }).start();
        } catch (Exception ex) {
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
            Mensagens.mensagemErro(NovoChamadoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        if (!Utilidades.verificarHorarioComercial(NovoChamadoActivity.this, true)) {
            Mensagens.horarioComercial(NovoChamadoActivity.this);
        }
        SimpleDbHelper.INSTANCE.open(getApplicationContext());
        super.onResume();
    }

    private void loadImagefromGallery() {
        try {
            if (listaAnexos != null && listaAnexos.size() == 10) {
                Alerta alerta = new Alerta(NovoChamadoActivity.this, getResources().getString(R.string.app_name), "Não é possivel incluir mais anexos");
                alerta.show();
                return;
            }
            Utilidades.loadImagefromGallery(NovoChamadoActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(NovoChamadoActivity.this, ex.getMessage(), false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RequestCode.GaleriaImagem && resultCode == RESULT_OK && null != data) {
                File file = Utilidades.getImagemFromGallery(data, NovoChamadoActivity.this);
                if (file != null) {
                    if (listaAnexos == null)
                        listaAnexos = new ArrayList<>();
                    NovoAnexo novoAnexo = new NovoAnexo();
                    novoAnexo.setLocalArquivo(file.getPath());
                    novoAnexo.setNomeArquivo(file.getName());
                    listaAnexos.add(novoAnexo);
                    atualizaLista();
                } else
                    Utilidades.retornaMensagem(NovoChamadoActivity.this, "Imagem não selecionada", false);
            } else
                Utilidades.retornaMensagem(NovoChamadoActivity.this, "Imagem não selecionada", false);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(NovoChamadoActivity.this, "Erro: " + ex.toString(), true);
        }
    }

    private void atualizaLista() {
        NovoAnexoAdapter anexoAdapter = new NovoAnexoAdapter(this, listaAnexos);
        listaItens.setAdapter(anexoAdapter);
    }
}
