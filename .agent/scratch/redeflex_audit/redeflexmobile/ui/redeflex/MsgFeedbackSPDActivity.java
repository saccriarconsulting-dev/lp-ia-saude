package com.axys.redeflexmobile.ui.redeflex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MsgFeedbackSPDActivity extends AppCompatActivity {

    public static final int BUTTON_WAIT_DURATION = 1000;

    Button botaoConsultSol;
    FloatingActionButton botaoNovaSol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_feedback_spd);

        botaoConsultSol = findViewById(R.id.btn_consultar_solicitacoes);
        botaoNovaSol = findViewById(R.id.btnAdd_spd);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Solicitar Preço Diferenciado");
        }

        botaoConsultSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MsgFeedbackSPDActivity.this, ConsultarSolicitacoesPrecoDifActivity.class);
                    startActivity(intent);
            }
        });

        botaoNovaSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MsgFeedbackSPDActivity.this, SolicitacaoPrecoDifActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}