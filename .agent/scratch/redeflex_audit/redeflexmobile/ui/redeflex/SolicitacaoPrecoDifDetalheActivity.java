package com.axys.redeflexmobile.ui.redeflex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolPD_ClienteAdapter;
import com.axys.redeflexmobile.shared.adapters.ItemSolPD_DetalheAdapter;
import com.axys.redeflexmobile.shared.adapters.ListaSolicitacoesPDAdapter;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;

import java.util.ArrayList;

public class SolicitacaoPrecoDifDetalheActivity extends AppCompatActivity {
    private SolicitacaoPrecoDiferenciado mSolicitacao;
    private RecyclerView recyclerView_Detalhes;
    ItemSolPD_DetalheAdapter itemsAdapter;

    TextView tv_IdSolicitacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_preco_dif_detalhe);

        Bundle bundle = getIntent().getExtras();
        mSolicitacao = (SolicitacaoPrecoDiferenciado) bundle.getSerializable("SolicitacaoPD");

        criarObjetos();
        montarActionBar();
        criarEventos();

        // Atualiza dados da Tela
        if (mSolicitacao.getIdServerSolicitacao() != 0)
            tv_IdSolicitacao.setText("Solicitação ID: " + mSolicitacao.getIdServerSolicitacao());
        else
            tv_IdSolicitacao.setText("Solicitação ID: " + mSolicitacao.getId());
        AtualizaRecycler();
    }

    private void criarEventos() {
    }

    private void montarActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalhes Solicitação PD");
    }

    private void criarObjetos() {
        recyclerView_Detalhes = findViewById(R.id.solicitacaoDetalhe_recyclerDetalhes);
        recyclerView_Detalhes.setLayoutManager(new LinearLayoutManager(this));
        tv_IdSolicitacao = findViewById(R.id.solicitacaoDetalhe_tvSolicitacao);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void AtualizaRecycler() {
        ArrayList<SolicitacaoPrecoDiferenciadoDetalhe> listaItens =
                new BDSolicitacaoPrecoDiferenciadoDetalhe(SolicitacaoPrecoDifDetalheActivity.this).getSolicitacaoDetalhes("and IdSolicitacao = " + mSolicitacao.getId(), null);
        itemsAdapter = new ItemSolPD_DetalheAdapter(mSolicitacao, listaItens, SolicitacaoPrecoDifDetalheActivity.this);
        recyclerView_Detalhes.setAdapter(itemsAdapter);
    }
}