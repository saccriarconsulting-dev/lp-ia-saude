package com.axys.redeflexmobile.ui.redeflex;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSenhaVenda;
import com.axys.redeflexmobile.shared.bd.MyMultiThreadSQLiteOpenHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.SenhaVenda;
import com.axys.redeflexmobile.shared.services.CartaoPontoService;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private EditText txtSenha;
    private Button btnLogar;
    private EditText txtConfirmaSenha;
    private EditText txtCadastrarSenha;
    private Button btnCadastrar;
    private DBSenhaVenda dbSenhaVenda;
    private Colaborador colaborador;
    private ArrayList<SenhaVenda> listSenha;
    private Intent serviceCartaoPonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utilidades.getDataServidorESalvaBanco(LoginActivity.this);
        serviceCartaoPonto = new Intent(this, CartaoPontoService.class);
        this.iniciarValidacoes();

        /*Colaborador usuario = new DBColaborador(this).get();
        if (usuario != null) {
            String usuarioDados = String.format(
                    "%s - %s",
                    usuario.getId(),
                    usuario.getNome()
            );
            Crashlytics.setUserIdentifier(usuarioDados);
        }*/
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            Utilidades.hideKeyboard(view, this);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void iniciarValidacoes() {
        try {
            DBColaborador dbColaborador = new DBColaborador(LoginActivity.this);
            colaborador = dbColaborador.get();

            if (colaborador == null) {
                Alerta alerta = new Alerta(LoginActivity.this,
                        getString(R.string.app_name),
                        getString(R.string.aparelho_sem_vinculo));
                alerta.showError();
                return;
            }

            if (!Util_IO.isNullOrEmpty(colaborador.getNome())) {
                TextView txtApresentacao = findViewById(R.id.txtColaborador);
                txtApresentacao.setText(Util_IO.toTitleCase(colaborador.getNome()
                        .substring(0, colaborador.getNome().indexOf(' '))));
            }

            if (this.validarVersao()) return;
            this.criaObjetos();
            this.criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(LoginActivity.this, ex.getLocalizedMessage(), true);
            Timber.e(ex);
        }
    }

    private boolean validarVersao() {
        if (colaborador.getVersaoApp() == null
                || colaborador.getVersaoApp().equals("-1")
                || colaborador.getVersaoApp().equals(BuildConfig.VERSION_NAME)) {
            return false;
        }

        Alerta alerta = new Alerta(LoginActivity.this,
                getString(R.string.app_name),
                getString(R.string.message_update_app));

        alerta.show((dialog, which) -> {
            Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
            startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
            finish();
        });
        return true;
    }

    private void criaObjetos() {
        btnLogar = findViewById(R.id.btnLogar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtSenha = findViewById(R.id.txtSenhaLogin);
        txtConfirmaSenha = findViewById(R.id.txtConfirmaSenha);
        txtCadastrarSenha = findViewById(R.id.txtCadastrarSenha);
        RelativeLayout rlTelaDesabilitada = findViewById(R.id.tela_desabilitada);

        rlTelaDesabilitada.setVisibility(View.GONE);

        try {
            dbSenhaVenda = new DBSenhaVenda(LoginActivity.this);
            listSenha = dbSenhaVenda.getSenha(false);

            if (listSenha == null || listSenha.isEmpty()) {
                (findViewById(R.id.layoutConfirmaSenha)).setVisibility(View.VISIBLE);
                Alerta msg = new Alerta(LoginActivity.this,
                        getString(R.string.app_name),
                        getString(R.string.message_save_password));
                msg.show();
            } else {
                (findViewById(R.id.layoutSenha)).setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(LoginActivity.this, ex.getMessage(), true);
        }

    }

    private boolean deveControlarCartaoPonto() {
        return !(colaborador != null && colaborador.isCartaoPonto());
    }

    private void criarEventos() {
        txtSenha.setOnEditorActionListener(this);
        txtConfirmaSenha.setOnEditorActionListener(this);
        btnLogar.setOnClickListener(view -> logar());
        btnCadastrar.setOnClickListener(view -> cadastrar());
    }

    private void logar() {
        if (!Validacoes.validacaoDataAparelho(LoginActivity.this)) {
            return;
        }

        try {
            String senhaInformada = txtSenha.getText().toString().trim();
            if (senhaInformada.isEmpty()) {
                txtSenha.requestFocus();
                txtSenha.setError("Senha não informada");
                return;
            }

            String senhaatual = listSenha.get(0).getSenha().trim();
            String senhaparalela = senhaOffLine();
            if (!senhaatual.equals(senhaInformada)) {
                if (!senhaparalela.equals(senhaInformada)) {
                    txtSenha.requestFocus();
                    txtSenha.setError("A senha está inválida, Verifique!");
                    return;
                } else
                    dbSenhaVenda.deleteAll();
            }

            if (deveControlarCartaoPonto()) {
                abrirMainActivity(1);
                return;
            }

            Utilidades.hideKeyboard(txtSenha, this);
            startService(serviceCartaoPonto);
            abrirMainActivity(1);
        } catch (Exception ex) {
            Mensagens.mensagemErro(LoginActivity.this, ex.getMessage(), false);
        }
    }

    private void cadastrar() {
        try {
            String senha = txtCadastrarSenha.getText().toString().trim();
            if (senha.isEmpty()) {
                txtCadastrarSenha.requestFocus();
                txtCadastrarSenha.setError("Senha não informada, Verifique!");
                return;
            }
            if (senha.length() < 4) {
                txtCadastrarSenha.requestFocus();
                txtCadastrarSenha.setError("A senha deve ser maior ou igual à 4 digitos, Verifique!");
                return;
            }

            String confirmacao = txtConfirmaSenha.getText().toString().trim();
            if (confirmacao.isEmpty()) {
                txtConfirmaSenha.requestFocus();
                txtConfirmaSenha.setError("A Confirmação da Senha não foi informada, Verifique!");
                return;
            }
            if (confirmacao.length() < 4) {
                txtConfirmaSenha.requestFocus();
                txtConfirmaSenha.setError("A Confirmação da Senha deve ser maior ou igual à 4 digitos, Verifique!");
                return;
            }
            if (!senha.equals(confirmacao)) {
                Alerta msg = new Alerta(LoginActivity.this,
                        getString(R.string.app_name),
                        "A senha e a confirmação estão divergentes, Verifique!");
                msg.show();
                return;
            }

            SenhaVenda senhaVenda = new SenhaVenda();
            senhaVenda.setSenha(senha);
            DBSenhaVenda dbSenhaVenda = new DBSenhaVenda(LoginActivity.this);
            dbSenhaVenda.addSenha(senhaVenda);

            Utilidades.hideKeyboard(txtCadastrarSenha, this);

            if (deveControlarCartaoPonto()) {
                stopService(serviceCartaoPonto);
                Utilidades.retornaMensagem(LoginActivity.this, "Senha incluída com sucesso!", false);
                abrirMainActivity(0);
                return;
            }

            startService(serviceCartaoPonto);
            Utilidades.retornaMensagem(LoginActivity.this, "Senha incluída com sucesso!", false);
            abrirMainActivity(0);
        } catch (Exception ex) {
            Mensagens.mensagemErro(LoginActivity.this, ex.getMessage(), false);
        }
    }

    private void abrirMainActivity(int tipoOperacao) {
        Bundle bundle = new Bundle();
        bundle.putInt("Login", 1);
        bundle.putInt("mTipoOperacao", tipoOperacao);
        Utilidades.openNewActivity(LoginActivity.this, MainActivity.class, bundle, true);
    }

    private String senhaOffLine() {
        String retorno = "";

        try {
            //imei
            String imei = Utilidades.retornaIMEI(LoginActivity.this);

            //data atual
            Calendar calendar = new GregorianCalendar();
            Date trialTime = new Date();
            calendar.setTime(trialTime);
            String dataatual = Util_IO.dateTimeToString(calendar.getTime(), "yyyyMMdd");

            Integer dataint = Integer.parseInt(dataatual);
            Long imeiint = Long.parseLong(imei);
            Long resultado = dataint * imeiint;

            retorno = String.valueOf(resultado).substring(resultado.toString().length() - 4);
        } catch (Exception ex) {
            Timber.e(ex);
        }

        return retorno;
    }
}
