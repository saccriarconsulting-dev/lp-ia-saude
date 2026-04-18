package com.axys.redeflexmobile.ui.redeflex.solicitacaopid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.RotaAdapter;
import com.axys.redeflexmobile.shared.adapters.SolicitacaoPidAdapter;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.DBRota;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;
import com.axys.redeflexmobile.ui.redeflex.RotaDiariaActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Activity_ListaSolicitacaoPID extends AppCompatActivity {
    LinearLayout Empty;
    RecyclerView mRecyclerSolPid;
    SolicitacaoPidAdapter adapterSolPid;
    View viewLoading;

    FloatingActionButton btnNovaSolicitacao;
    EditText edtFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_solicitacao_pid);

        criarObjetos();
        montarActionBar();
        criarEventos();

        try {
            new LoadDataTask().execute();
        } catch (Exception ex) {
            Alerta alerta = new Alerta(Activity_ListaSolicitacaoPID.this, Activity_ListaSolicitacaoPID.this.getResources().getString(R.string.app_name), "Erro ao Carregar Dados:\n" + ex.getLocalizedMessage());
            alerta.show();
        }
    }

    private void montarActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fila de Solicitações - PID");
    }

    private void criarEventos() {
        btnNovaSolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Activity_ListaSolicitacaoPID.this, Activity_SolPID_DadosCliente.class);
                //intent.putExtra("Solicitacao", new SolicitacaoPid());
                //startActivityForResult(intent, 0);
                showPopupMenu(v);
            }
        });

        edtFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CarregarDados();
            }
        });
    }

    private void CarregarDados() {
        try {

            ArrayList<SolicitacaoPid> listaDados = new BDSolicitacaoPid(Activity_ListaSolicitacaoPID.this).getSolicitacaoPidFiltro(edtFiltro.getText().toString());
            adapterSolPid = new SolicitacaoPidAdapter(listaDados, Activity_ListaSolicitacaoPID.this);
            mRecyclerSolPid.setAdapter(adapterSolPid);
            if (listaDados.size() == 0)
                Empty.setVisibility(View.VISIBLE);
            else
                Empty.setVisibility(View.GONE);
        } catch (Exception ex) {
            Mensagens.mensagemErro(Activity_ListaSolicitacaoPID.this, ex.getMessage(), false);
        }
    }

    private void criarObjetos() {
        Empty = findViewById(R.id.empty);
        mRecyclerSolPid = (RecyclerView) findViewById(R.id.listaPid_rv_itens);
        mRecyclerSolPid.setLayoutManager(new LinearLayoutManager(this));
        btnNovaSolicitacao = findViewById(R.id.listaPid_btn_novasolicitacao);
        edtFiltro = findViewById(R.id.listaPid_edt_filtro);
    }

    public void onResume() {
        super.onResume();
        if (adapterSolPid != null)
            adapterSolPid.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            try {
                new LoadDataTask().execute();
            } catch (Exception ex) {
                Alerta alerta = new Alerta(Activity_ListaSolicitacaoPID.this, Activity_ListaSolicitacaoPID.this.getResources().getString(R.string.app_name), "Erro ao Carregar Dados:\n" + ex.getLocalizedMessage());
                alerta.show();
            }
        }
    }

    private class LoadDataTask extends AsyncTask<String, Void, ArrayList<SolicitacaoPid>> {
        private ProgressDialog load = new ProgressDialog(Activity_ListaSolicitacaoPID.this);

        @Override
        protected void onPreExecute() {
            load.setMessage("Carregando Solicitações PID!");
            load.show();
        }

        @Override
        protected ArrayList<SolicitacaoPid> doInBackground(String... params) {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            ArrayList<SolicitacaoPid> lista_Cadastros = new BDSolicitacaoPid(Activity_ListaSolicitacaoPID.this).getAll();
            return lista_Cadastros;
        }

        @Override
        protected void onPostExecute(ArrayList<SolicitacaoPid> items) {
            super.onPostExecute(items);
            load.dismiss();
            adapterSolPid = new SolicitacaoPidAdapter(items, Activity_ListaSolicitacaoPID.this);
            mRecyclerSolPid.setAdapter(adapterSolPid);
            if (items.size() == 0)
                Empty.setVisibility(View.VISIBLE);
            else
                Empty.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_solicitacaopid, popupMenu.getMenu());

        // Forçar ícones a aparecerem no menu overflow
        if (popupMenu.getMenu() instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) popupMenu.getMenu();
            m.setOptionalIconsVisible(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_item_novasolicitacao) {
                    Intent intent = new Intent(Activity_ListaSolicitacaoPID.this, Activity_SolPID_DadosCliente.class);
                    intent.putExtra("Solicitacao", new SolicitacaoPid());
                    startActivityForResult(intent, 0);
                    return true;
                } else if (item.getItemId() == R.id.menu_item_simuladorpid) {
                    // Inicia a activity principal (MainActivity) novamente
                    Intent intent = new Intent(Activity_ListaSolicitacaoPID.this, Activity_SimuladorPid.class);
                    startActivityForResult(intent, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }
}