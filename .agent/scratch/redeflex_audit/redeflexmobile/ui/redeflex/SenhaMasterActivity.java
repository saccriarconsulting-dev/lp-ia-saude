package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSenhaMasters;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.models.SenhaMasters;
import com.axys.redeflexmobile.shared.services.tasks.SenhaMastersTask;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vitor Herrmann - Capptan on 07/08/2018.
 */

public class SenhaMasterActivity extends AppCompatActivity {

    private static final String nullResponse = "null";

    private LinearLayout llPrincipal;
    private LinearLayout llErro;
    private LinearLayout llCarregando;
    private LinearLayout llContainer;
    private TextView tvData;
    private TextView tvSenha;
    private Button btTentarNovamente;

    private DBColaborador dbColaborador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha_master);

        iniciarViews();
        dbColaborador = new DBColaborador(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Senha Masters");
        }

        try {
            SenhaMasters senhaMasters = recuperaSenhaMasters();

            if (senhaMasters == null) {
                validaSenha();
            } else {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataAtual = dateFormat.parse(dateFormat.format(new Date()));
                Date dataBanco = dateFormat.parse(senhaMasters.getDataUltimaAtualizacao());

                if (Utilidades.diferencaDatas(dataAtual, dataBanco, TimeUnit.DAYS) >= 1) {
                    validaSenha();
                } else {
                    preencheValores(senhaMasters);
                }
            }

        } catch (Exception ex) {
            Alerta alerta = new Alerta(SenhaMasterActivity.this, "Erro", ex.getMessage());
            alerta.showError();
        }
    }

    private void preencheValores(SenhaMasters senhaMasters) {
        llPrincipal.setVisibility(View.VISIBLE);
        llErro.setVisibility(View.GONE);

        tvData.setText(senhaMasters.getDataUltimaAtualizacao());
        tvSenha.setText(senhaMasters.getSenha().replace("\"", ""));
    }

    private void validaSenha() {
        if (Utilidades.isConectado(this)) {
            recuperarSenhaServidor(String.valueOf(dbColaborador.get().getId()));
        } else {
            mostrarLayoutErro();
        }
    }

    private void esconderCarregando() {
        llContainer.setVisibility(View.VISIBLE);
        llCarregando.setVisibility(View.GONE);
    }

    private void mostrarCarregando() {
        llContainer.setVisibility(View.GONE);
        llCarregando.setVisibility(View.VISIBLE);
    }

    private void mostrarLayoutErro() {
        llPrincipal.setVisibility(View.GONE);
        llErro.setVisibility(View.VISIBLE);
    }

    private void recuperarSenhaServidor(String idColaborador) {
        DBSenhaMasters dbSenhaMasters = new DBSenhaMasters(this);
        mostrarCarregando();
        new SenhaMastersTask(senha -> {
            esconderCarregando();

            if (senha.exception == null && senha.models != null && !senha.models.isEmpty() && !senhaEhInvalida(senha)) {
                dbSenhaMasters.addSenhaMasters(senha.models.get(0));
                preencheValores(dbSenhaMasters.getSenhaMasters());
            } else {
                mostrarLayoutErro();
            }

        }).execute(idColaborador);
    }

    private boolean senhaEhInvalida(RequestModelGeneric<String> requestModelGeneric) {
        String senha = requestModelGeneric.models.get(0)
                .replace("\n", "")
                .replaceAll(" ", "")
                .replaceAll("\"\"", "");
        return senha.equals(nullResponse) || senha.equals("");
    }

    private void iniciarViews() {
        llPrincipal = (LinearLayout) findViewById(R.id.senha_masters_layout_principal);
        llErro = (LinearLayout) findViewById(R.id.senha_masters_layout_erro);
        llCarregando = (LinearLayout) findViewById(R.id.senha_masters_ll_carregando);
        llContainer = (LinearLayout) findViewById(R.id.senha_masters_ll_container);
        tvData = (TextView) findViewById(R.id.senha_masters_data);
        tvSenha = (TextView) findViewById(R.id.senha_masters);
        btTentarNovamente = (Button) findViewById(R.id.senha_masters_botao_tentar_novamente);

        btTentarNovamente.setOnClickListener(view -> {
            if (Utilidades.isConectado(this)) {
                recuperarSenhaServidor(String.valueOf(dbColaborador.get().getId()));
            }
        });
    }

    private SenhaMasters recuperaSenhaMasters() {
        try {
            return new DBSenhaMasters(SenhaMasterActivity.this).getSenhaMasters();
        } catch (Exception ex) {
            Alerta alerta = new Alerta(SenhaMasterActivity.this, "Erro", ex.getMessage());
            alerta.show();

            return null;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
