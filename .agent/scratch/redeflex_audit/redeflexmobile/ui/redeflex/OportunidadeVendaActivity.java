package com.axys.redeflexmobile.ui.redeflex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.EnumSituacaoCliente;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

public class OportunidadeVendaActivity extends AppCompatActivity {

    ImageButton btnRupturaClaro,btnRupturaVivo,btnRupturaOi,btnRupturaTim;
    ImageButton btnPreRupturaClaro,btnPreRupturaVivo,btnPreRupturaOi,btnPreRupturaTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidade_venda);

        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Oportunidades de Vendas");
            }
            criarObjetos();
            criarEventos();
        } catch (Exception ex) {
            Mensagens.mensagemErro(OportunidadeVendaActivity.this, ex.getMessage(), true);
        }
    }

    private void criarEventos() {
        btnRupturaClaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.Ruptura.getValue());
                bundle.putInt("OPERADORA", 2);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnRupturaVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.Ruptura.getValue());
                bundle.putInt("OPERADORA", 3);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnRupturaOi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.Ruptura.getValue());
                bundle.putInt("OPERADORA", 1);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnRupturaTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.Ruptura.getValue());
                bundle.putInt("OPERADORA", 4);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnPreRupturaClaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.PreRuptura.getValue());
                bundle.putInt("OPERADORA", 2);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnPreRupturaVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.PreRuptura.getValue());
                bundle.putInt("OPERADORA", 3);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnPreRupturaOi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.PreRuptura.getValue());
                bundle.putInt("OPERADORA", 1);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });

        btnPreRupturaTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("SITUACAO", EnumSituacaoCliente.PreRuptura.getValue());
                bundle.putInt("OPERADORA", 4);
                Utilidades.openNewActivity(OportunidadeVendaActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
            }
        });
    }

    private void criarObjetos() {
        btnRupturaClaro = (ImageButton)findViewById(R.id.oportunidade_btnRupturaClaro);
        btnRupturaVivo = (ImageButton)findViewById(R.id.oportunidade_btnRupturaVivo);
        btnRupturaOi = (ImageButton)findViewById(R.id.oportunidade_btnRupturaOi);
        btnRupturaTim = (ImageButton)findViewById(R.id.oportunidade_btnRupturaTim);
        btnPreRupturaClaro = (ImageButton)findViewById(R.id.oportunidade_btnPreRupturaClaro);
        btnPreRupturaVivo = (ImageButton)findViewById(R.id.oportunidade_btnPreRupturaVivo);
        btnPreRupturaOi = (ImageButton)findViewById(R.id.oportunidade_btnPreRupturaOi);
        btnPreRupturaTim = (ImageButton)findViewById(R.id.oportunidade_btnPreRupturaTim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//                                try {
//        Bundle bundle = new Bundle();
//        bundle.putString("SITUACAO", EnumSituacaoCliente.Ruptura.getValue());
//        bundle.putInt("OPERADORA", 3);
//        Utilidades.openNewActivity(MainActivity.this, Lista_OportunidadeVendaActivity.class, bundle, false);
//    }
//                                catch (Exception ex) {
//        Utilidades.retornaMensagem(MainActivity.this, "Erro: " + ex.getMessage(), true);
//    }

}