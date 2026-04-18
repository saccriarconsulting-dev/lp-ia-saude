package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.CodBarraAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class ItensVendaActivity extends AppCompatActivity implements CodBarraAdapter.ICodBarraAdapterListenner {
    CodBarraAdapter mAdapter;
    ArrayList<ItemVendaCombo> listFinal;
    ExpandableListView expandableICCIDs;
    private int codigoVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens_venda);

        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Itens Venda");
            }

            codigoVenda = 0;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                codigoVenda = bundle.getInt(Config.CodigoVenda);

            if (codigoVenda == 0) {
                Mensagens.mensagemErro(ItensVendaActivity.this, "Venda não iniciada!", true);
                return;
            }

            criarObjetos();
            alimentaDados(codigoVenda);
        } catch (Exception ex) {
            Mensagens.mensagemErro(ItensVendaActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        expandableICCIDs = (ExpandableListView) findViewById(R.id.expandableICCIDs);
    }

    private void alimentaDados(int pCodigoVenda) {
        try {
            listFinal = new DBVenda(ItensVendaActivity.this).getItensComboVendabyIdVenda(pCodigoVenda);
            mAdapter = new CodBarraAdapter(listFinal, ItensVendaActivity.this, this, 1);
            expandableICCIDs.setAdapter(mAdapter);
            expandableICCIDs.setEmptyView(findViewById(R.id.empty));
            for (int i = 0; i < listFinal.size(); i++) {
                if (!listFinal.get(i).getCodigosList().isEmpty()) {
                    expandableICCIDs.expandGroup(i);
                }
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(ItensVendaActivity.this, ex.getMessage(), false);
        }
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

    @Override
    public void removeGroupItem(ArrayList<ItemVendaCombo> lista, int groupPosition) {
        Alerta alerta = new Alerta(ItensVendaActivity.this, getResources().getString(R.string.app_name), "Deseja excluir esse item?");
        alerta.showConfirm((dialog, which) -> {
            try {
                Utilidades.removerItemVenda(ItensVendaActivity.this,
                        lista.get(groupPosition).getIdProduto(),
                        lista.get(groupPosition).getQtde(),
                        lista.get(groupPosition).getId());

                alimentaDados(codigoVenda);
            } catch (Exception ex) {
                Mensagens.mensagemErro(ItensVendaActivity.this, ex.getMessage(), false);
            }
        }, null);
    }

    @Override
    public void removeChildItem(ArrayList<ItemVendaCombo> lista, CodBarra codBarra, int groupPosition, int childPosition) {
        Alerta alerta = new Alerta(ItensVendaActivity.this, getResources().getString(R.string.app_name), "Deseja excluir esse item?");
        alerta.showConfirm((dialog, which) -> {
            DBEstoque dbEstoque = new DBEstoque(ItensVendaActivity.this);
            DBVenda dbVenda = new DBVenda(ItensVendaActivity.this);
            int quantidade = 0, codigoPai = lista.get(groupPosition).getId();
            lista.get(groupPosition).getCodigosList().remove(childPosition);
            for (CodBarra items : lista.get(groupPosition).getCodigosList())
                quantidade += Integer.parseInt(items.retornaQuantidade(UsoCodBarra.GERAL));

            if (quantidade == 0) {
                lista.remove(groupPosition);
                dbVenda.deleteItemByIdItem(codigoPai);
            } else {
                lista.get(groupPosition).setQtde(quantidade);
                dbVenda.updateQtdItemVenda(codigoPai, quantidade);
                dbVenda.deleteItemCodBarraById(codBarra.getIdPistolagem());
            }

            alimentaDados(codigoVenda);
        }, null);
    }
}