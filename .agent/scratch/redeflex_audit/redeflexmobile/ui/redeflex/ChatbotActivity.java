package com.axys.redeflexmobile.ui.redeflex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

public class ChatbotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setDatabaseEnabled(true);
        //webView.getSettings().setAppCacheEnabled(true);

        if (Utilidades.isConectado(ChatbotActivity.this))
        {
            try {
                webView.loadUrl(BuildConfig.CHATBOT_URL + "?ci=" + new DBColaborador(ChatbotActivity.this).get().getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona");

                // Definir o WebChromeClient para monitorar o console JavaScript
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        if (consoleMessage.message().contains("status = DISCONNECTED")) {
                            finish();
                        }
                        return super.onConsoleMessage(consoleMessage);
                    }
                });
            }
            catch (Exception ex)
            {
                Mensagens.mensagemErro(ChatbotActivity.this, "Erro ao conectar com o ChatBot.\nEntrar em contato com o Suporte", false);
            }
        }
        else
            Mensagens.mensagemErro(ChatbotActivity.this, "Verifique sua conexão", false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}