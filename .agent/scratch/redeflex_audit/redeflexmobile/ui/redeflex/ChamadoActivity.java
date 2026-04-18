package com.axys.redeflexmobile.ui.redeflex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CallReasonAdapter;
import com.axys.redeflexmobile.shared.adapters.InteracoesAdapter;
import com.axys.redeflexmobile.shared.adapters.NovoAnexoAdapter;
import com.axys.redeflexmobile.shared.bd.DBCallReason;
import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.CallReason;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InteracaoAnexos;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.models.NovoAnexo;
import com.axys.redeflexmobile.shared.services.tasks.ChamadosTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ChamadoActivity extends AppCompatActivity {
    private static final int DELAY_TO_NEXT_ACTION = 1200;
    private final String OTHERS_VALUE = "Outros";

    private Chamado chamado;
    private ArrayList<NovoAnexo> listaAnexos;

    private DBChamados dbChamados;
    private DBCallReason dbCallReason;

    private ExpandableListView expandableListView;
    private Button btnEnviar;
    private EditText txtMensagem;
    private ListView listView;
    private SearchableSpinner ddlMotivo;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<Integer> menuEventos = PublishSubject.create();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_interacao, menu);
        return !(chamado != null && chamado.getStatusID() == 6);
    }

    @Override
    public void onBackPressed() {

    }

    private boolean verificaChamadoCliente() {
        if (chamado.getIdCliente() != null && chamado.getIdCliente() > 0) {
            if (chamado.getStatusID() == 1 && chamado.getDataAgendamento() == null) {
                Alerta alerta = new Alerta(ChamadoActivity.this, getResources().getString(R.string.app_name)
                        , "É necessário atender o chamado");
                alerta.show((dialogInterface, i) -> {

                });
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            if (chamado.getStatusID() == 1 && chamado.getDataAgendamento() != null
                    && Util_IO.dateToStringBr(chamado.getDataAgendamento()).equalsIgnoreCase(Util_IO.dateToStringBr(calendar.getTime()))) {
                Alerta alerta = new Alerta(ChamadoActivity.this, getResources().getString(R.string.app_name)
                        , "É necessário atender o chamado, Verifique!");
                alerta.show();
                return false;
            }
        }

        return true;
    }

    private void fechar() {
        if (verificaChamadoCliente()) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_call_screen);
        }

        try {
            criarObjetos();
            Bundle parametros = getIntent().getExtras();
            if (parametros != null) {
                String idChamado = parametros.getString(Config.CodigoChamado);

                chamado = dbChamados.getChamadoById(idChamado);
                if (chamado != null) {
                    getSupportActionBar().setTitle(getString(R.string.label_call_number, chamado.getChamadoID()));
                    criarEventos();

                    Notificacoes.cancelarNotificacao(ChamadoActivity.this, Integer.parseInt(idChamado));

                    LayoutInflater inflater = getLayoutInflater();
                    ViewGroup header = (ViewGroup) inflater.inflate(R.layout.info_chamados, expandableListView, false);
                    expandableListView.addHeaderView(header);
                    TextView txtAgendamento = header.findViewById(R.id.txtAgendamento);

                    if (chamado.getStatusID() == 6) {
                        btnEnviar.setEnabled(false);
                        txtMensagem.setEnabled(false);
                    } else if (chamado.getDataAgendamento() != null) {
                        txtAgendamento.setVisibility(View.VISIBLE);
                        txtAgendamento.setText(
                                getString(R.string.label_marked_for, Util_IO.dateTimeToStringBr(chamado.getDataAgendamento()))
                        );
                    }

                    ((TextView) header.findViewById(R.id.txtAssunto)).setText(
                            getString(R.string.label_number_position_describe, chamado.getChamadoID(), chamado.getAssunto())
                    );
                    ((TextView) header.findViewById(R.id.txtSolicitante)).setText(
                            getString(R.string.label_requester, chamado.getSolicitante())
                    );
                    ((TextView) header.findViewById(R.id.txtAbertura)).setText(
                            getString(R.string.label_open_in, Util_IO.dateTimeToStringBr(chamado.getDataCadastro()))
                    );
                    ((TextView) header.findViewById(R.id.txtUltima)).setText(
                            getString(R.string.label_last_interaction, Util_IO.dateTimeToStringBr(chamado.getDataAlteracao()))
                    );
                    ((TextView) header.findViewById(R.id.txtSituacao)).setText(
                            getString(R.string.label_situation, chamado.retornaSituacao())
                    );

                    TextView txtCliente = header.findViewById(R.id.txtCliente);
                    if (chamado.getIdCliente() == null || chamado.getIdCliente() == 0) {
                        txtCliente.setVisibility(View.GONE);
                        ddlMotivo.setVisibility(View.GONE);
                    } else {
                        if (chamado.getStatusID() != 6) {
                            txtMensagem.setVisibility(View.GONE);
                            ddlMotivo.setVisibility(View.VISIBLE);

                            List<CallReason> listReasons = dbCallReason.getAllActive();
                            Utilidades.defineSpinner(ddlMotivo);
                            CallReasonAdapter mAdapter = new CallReasonAdapter(ChamadoActivity.this, R.layout.custom_spinner_title_bar, listReasons);
                            ddlMotivo.setAdapter(mAdapter);
                        }

                        Cliente cliente = new DBCliente(ChamadoActivity.this).getById(String.valueOf(chamado.getIdCliente()));
                        if (cliente == null)
                            txtCliente.setVisibility(View.GONE);
                        else
                            txtCliente.setText(getString(R.string.label_client_code_name, cliente.retornaCodigoExibicao(), cliente.getNomeFantasia()));
                    }

                    refreshlista();
                } else
                    Mensagens.mensagemErro(ChamadoActivity.this, "Chamado não encontrado", true);
            } else
                Mensagens.mensagemErro(ChamadoActivity.this, "Chamado não encontrado", true);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ChamadoActivity.this, ex.getMessage(), true);
        }
    }

    private void refreshlista() {
        //Recarrega o chamado do DB para capturar idChamado/status atualizados pela sync
        try {
            if (dbChamados != null
                    && chamado != null
                    && chamado.getIdAppMobile() != null
                    && chamado.getIdAppMobile() > 0) {

                Chamado refreshed = dbChamados.getChamadoById(String.valueOf(chamado.getIdAppMobile()));
                if (refreshed != null) {
                    chamado = refreshed;
                }
            }
        } catch (Exception e) {
            Timber.e(e, "refreshlista: failed to reload Chamado from DB");
            // Não bloqueia UI e segue com o que tiver em memória
        }

        ArrayList<InteracaoAnexos> listInteracoes = null;

        final boolean hasServerId = (chamado != null
                && chamado.getChamadoID() != null
                && chamado.getChamadoID() > 0);

        final Integer idMobile = (chamado != null) ? chamado.getIdAppMobile() : null;

        //Fonte principal e fallback para cobrir janela de migração
        if (hasServerId) {
            listInteracoes = dbChamados.getInteracoesByChamadoId(chamado.getChamadoID());

            // Fallback: se por algum motivo ainda estiver só no mobile
            if ((listInteracoes == null || listInteracoes.isEmpty())
                    && idMobile != null && idMobile > 0) {
                listInteracoes = dbChamados.getInteracoesByIdMobile(idMobile);
            }
        } else if (idMobile != null && idMobile > 0) {
            listInteracoes = dbChamados.getInteracoesByIdMobile(idMobile);

            // Fallback: se a sync já migrou (idChamado preenchido), o mobile pode “zerar”
            if ((listInteracoes == null || listInteracoes.isEmpty())
                    && chamado != null
                    && chamado.getChamadoID() != null
                    && chamado.getChamadoID() > 0) {
                listInteracoes = dbChamados.getInteracoesByChamadoId(chamado.getChamadoID());
            }
        } else {
            listInteracoes = new ArrayList<>();
        }

        if (listInteracoes == null) {
            listInteracoes = new ArrayList<>();
        }


        if (hasServerId
                && idMobile != null && idMobile > 0
                && !listInteracoes.isEmpty()
                && !hasAnyAttachment(listInteracoes)) {

            ArrayList<InteracaoAnexos> mobileList = dbChamados.getInteracoesByIdMobile(idMobile);
            if (mobileList != null && !mobileList.isEmpty() && hasAnyAttachment(mobileList)) {
                listInteracoes = mobileList;
            }
        }

        // Bind e auto-expand quando houver anexos
        InteracoesAdapter mAdapter = new InteracoesAdapter(ChamadoActivity.this, listInteracoes);
        expandableListView.setAdapter(mAdapter);

        for (int i = 0; i < listInteracoes.size(); i++) {
            if (listInteracoes.get(i).getListaAnexos() != null
                    && !listInteracoes.get(i).getListaAnexos().isEmpty()) {
                expandableListView.expandGroup(i);
            }
        }
    }


    private boolean hasAnyAttachment(ArrayList<InteracaoAnexos> listInteracoes) {
        for (int i = 0; i < listInteracoes.size(); i++) {
            if (listInteracoes.get(i) != null
                    && listInteracoes.get(i).getListaAnexos() != null
                    && !listInteracoes.get(i).getListaAnexos().isEmpty()) {
                return true;
            }
        }
        return false;
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

    private void criarObjetos() {
        dbChamados = new DBChamados(ChamadoActivity.this);
        dbCallReason = new DBCallReason(ChamadoActivity.this);

        expandableListView = findViewById(R.id.listaInteracoes);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtMensagem = findViewById(R.id.txtMensagem);
        listView = findViewById(R.id.listaAnexos);
        ddlMotivo = findViewById(R.id.spinnerMensagem);
    }

    private void criarEventos() {
        Disposable disposable = RxView.clicks(btnEnviar)
                .throttleFirst(DELAY_TO_NEXT_ACTION, TimeUnit.MILLISECONDS)
                .subscribe(view -> {
                    String descricao = txtMensagem.getText().toString().trim();
                    if (chamado.getIdCliente() != null
                            && chamado.getIdCliente() > 0
                            && !((CallReason) ddlMotivo.getSelectedItem()).getDescription().equalsIgnoreCase(OTHERS_VALUE)) {
                        descricao = ((CallReason) ddlMotivo.getSelectedItem()).getDescription();
                    }
                    Integer idReason = null;
                    if (ddlMotivo.getVisibility() == View.VISIBLE) {
                        idReason = ((CallReason) ddlMotivo.getSelectedItem()).getId();
                    }

                    if (Util_IO.isNullOrEmpty(descricao)) {
                        Utilidades.retornaMensagem(ChamadoActivity.this, getString(R.string.message_not_description), false);
                        if (chamado.getIdCliente() == null || chamado.getIdCliente() == 0)
                            txtMensagem.requestFocus();
                        return;
                    }

                    final ProgressDialog mDialog = new ProgressDialog(ChamadoActivity.this);
                    mDialog.setMessage(getString(R.string.label_wait_with_points));
                    mDialog.setIndeterminate(true);
                    mDialog.setCancelable(false);
                    mDialog.show();
                    final String mensagem = descricao;
                    final Integer idCallReason = idReason;
                    new Thread(() -> runOnUiThread(() -> {
                        String result;
                        try {
                            Interacoes interacoes = new Interacoes();
                            interacoes.setChamadoID(chamado.getChamadoID());
                            interacoes.setDescricao(Util_IO.trataString(mensagem));
                            interacoes.setIdCallReason(idCallReason);
                            interacoes.setDataCadastro(new Date());
                            Colaborador colaborador = new DBColaborador(ChamadoActivity.this).get();
                            interacoes.setUsuario(colaborador.getNome());
                            interacoes.setIdUsuario(colaborador.getId());
                            interacoes.setIdAppMobile(chamado.getIdAppMobile());
                            dbChamados.addInteracoesMobile(interacoes, listaAnexos);

                            int status = 4;
                            if (chamado.getResponsavelID() != null && chamado.getResponsavelID() == colaborador.getId())
                                status = 1;

                            chamado.setStatusID(status);
                            dbChamados.updateChamado(chamado.getIdAppMobile(), status);
                            result = "Interação incluída com sucesso";
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            result = "Erro: " + ex.getMessage();
                        }
                        mDialog.dismiss();
                        Utilidades.retornaMensagem(ChamadoActivity.this, result, false);
                        if (!result.contains("Erro")) {
                            new ChamadosTask(ChamadoActivity.this, 1).execute();
                            refreshlista();
                            txtMensagem.setText("");
                            listaAnexos = new ArrayList<>();
                            atualizarListaAnenos();
                        }
                    })).start();
                }, Timber::e);
        disposables.add(disposable);

        Disposable disposableMenu = menuEventos.throttleFirst(DELAY_TO_NEXT_ACTION, TimeUnit.MILLISECONDS)
                .subscribe(menuId -> {
                    if (menuId == android.R.id.home) {
                        fechar();
                    } else if (menuId == R.id.galeria_interacao) {
                        loadImagefromGallery();
                    } else if (menuId == R.id.limpar_anexo) {
                        listaAnexos = new ArrayList<>();
                        atualizarListaAnenos();
                    }
                }, Timber::e);
        disposables.add(disposableMenu);

        if (chamado.getIdCliente() != null && chamado.getIdCliente() > 0) {
            ddlMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String value = ((CallReason) ddlMotivo.getSelectedItem()).getDescription();
                    if (!value.equalsIgnoreCase(OTHERS_VALUE))
                        txtMensagem.setVisibility(View.GONE);
                    else
                        txtMensagem.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void atualizarListaAnenos() {
        NovoAnexoAdapter anexoAdapter = new NovoAnexoAdapter(ChamadoActivity.this, listaAnexos);
        listView.setAdapter(anexoAdapter);
    }

    private void loadImagefromGallery() {
        try {
            if (listaAnexos != null && listaAnexos.size() == 10) {
                Utilidades.retornaMensagem(ChamadoActivity.this, getString(R.string.message_no_more_attache), false);
                return;
            }
            Utilidades.loadImagefromGallery(ChamadoActivity.this);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ChamadoActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RequestCode.GaleriaImagem && resultCode == RESULT_OK && null != data) {
                File fotoOriginal = Utilidades.getImagemFromGallery(data, ChamadoActivity.this);
                if (fotoOriginal != null) {
                    if (listaAnexos == null)
                        listaAnexos = new ArrayList<>();
                    NovoAnexo novoAnexo = new NovoAnexo();
                    novoAnexo.setLocalArquivo(fotoOriginal.getPath());
                    novoAnexo.setNomeArquivo(fotoOriginal.getName());
                    listaAnexos.add(novoAnexo);
                    atualizarListaAnenos();
                } else
                    Utilidades.retornaMensagem(ChamadoActivity.this, getString(R.string.message_no_selected_image), false);
            } else
                Utilidades.retornaMensagem(ChamadoActivity.this, getString(R.string.message_no_selected_image), false);
        } catch (Exception ex) {
            Utilidades.retornaMensagem(ChamadoActivity.this, "Erro: " + ex.toString(), false);
        }
    }
}
