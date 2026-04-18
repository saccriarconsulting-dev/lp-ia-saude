package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.axys.redeflexmobile.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PagamentoConcluidoActivity extends AppCompatActivity {
    public static final int BUTTON_WAIT_DURATION = 1200;
    private Toolbar toolbar;

    LinearLayout btnComprovante;
    Button btnSair;
    ImageView imageViewGif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_concluido);

        criarObjetos();
        criarToolbar();
        criarEventos();
    }

    private void criarEventos() {
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void criarObjetos() {
        btnComprovante = findViewById(R.id.pagtoConcluido_btnComprovante);
        btnSair = findViewById(R.id.pagtoConcluido_btnSair);
        imageViewGif = findViewById(R.id.pagtoConcluido_imgConfirmado);

        // Carrega o GIF usando Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.animacao_confirma) // Substitua por seu GIF
                .into(new com.bumptech.glide.request.target.ImageViewTarget<GifDrawable>(imageViewGif) {
                    @Override
                    protected void setResource(GifDrawable resource) {
                        imageViewGif.setImageDrawable(resource);
                        if (resource != null) {
                            resource.setLoopCount(1);
                            resource.start();
                        }
                    }
                });
    }

    private void criarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mudarTitulo("Pagamento Concluído");
    }

    private void mudarTitulo(String texto) {
        setTitle(texto);
        toolbar.setTitle(texto);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(texto);
        }
    }
}