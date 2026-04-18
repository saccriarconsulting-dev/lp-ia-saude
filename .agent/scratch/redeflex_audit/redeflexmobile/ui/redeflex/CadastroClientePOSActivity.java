package com.axys.redeflexmobile.ui.redeflex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.bd.BDCadastroVendedorPOS;
import com.axys.redeflexmobile.shared.bd.DBTipoMaquina;
import com.axys.redeflexmobile.shared.models.CadastroVendedorPOS;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.ui.component.InputText;
import com.axys.redeflexmobile.ui.component.SearchableSpinner;

import java.util.ArrayList;

public class CadastroClientePOSActivity extends AppCompatActivity {

    //region Declaração de Variaveis

    SearchableSpinner ddlTipoPOS;
    InputText txtQtd, txtValorUnitario, txtValorLimite;
    ListView listaPOS;
    Button btnAddPOS;
    StringAdapter mStringAdapter;

    ArrayList<String> arrVendedorPOS;
    CadastroVendedorPOS vendedorPOS;

    ArrayList<String> arrTpPOS;

    BDCadastroVendedorPOS dbPOS;
    DBTipoMaquina dbTipoMaquina;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente_pos);
        dbPOS = new BDCadastroVendedorPOS(CadastroClientePOSActivity.this);
        dbTipoMaquina = new DBTipoMaquina(CadastroClientePOSActivity.this);
        setValorLimite();

        criarObjetos();
        carregarDados();
    }

    public void AdicionarPOS(View view) {

    }

    private void criarObjetos() {
        ddlTipoPOS = (SearchableSpinner) findViewById(R.id.ddlTipoPOS);
        txtQtd = (InputText) findViewById(R.id.txtQtd);
        txtValorUnitario = (InputText) findViewById(R.id.txtValorUnitario);
        txtValorLimite = (InputText) findViewById(R.id.txtValorLimite);
        listaPOS = (ListView) findViewById(R.id.listaPOS);
        btnAddPOS = (Button) findViewById(R.id.btnAddPOS);
    }

    private void setValorLimite() {
        txtValorLimite.setHint(Util_IO.formataValor(vendedorPOS.getTipoMaquina().getValorAluguelPadrao()));
    }

    private void carregarDados() {
        //vendedorPOS = dbPOS.getById();

        arrTpPOS.add("POS sem FIO Touch [IWL]");
        arrTpPOS.add("POS com FIO Touch [IWL]");
        mStringAdapter = new StringAdapter(CadastroClientePOSActivity.this, R.id.ddlTipoPOS, arrTpPOS);
        ddlTipoPOS.setAdapter(mStringAdapter);
    }
}
