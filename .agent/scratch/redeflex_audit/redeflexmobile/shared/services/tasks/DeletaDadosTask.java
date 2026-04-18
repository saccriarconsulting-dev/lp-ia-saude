package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBCobranca;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBLocalizacaoCliente;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPendencia;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBProjetoTrade;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.bd.DBVisita;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.bd.registerrate.DBProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.bd.registerrate.DBRegistrationSimulationFee;

/**
 * Created by joao.viana on 09/08/2016.
 */
public class DeletaDadosTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public DeletaDadosTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            new DBCobranca(mContext).deletePagos();
            DBRemessa dbRemessa = new DBRemessa(mContext);
            dbRemessa.deleteConfirmados();
            dbRemessa.deleteCancelados();
            DBVenda dbVenda = new DBVenda(mContext);
            dbVenda.delete60dias();
            new DBVisita(mContext).deletaVisitas60dias();
            DBPreco dbPreco = new DBPreco(mContext);
            dbPreco.deletePrecoUtilizado();
            dbPreco.deletarInativos();
            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(mContext);
            dbClienteCadastro.deleteOk();
            new DBOs(mContext).deleteAtendidas();
            new DBProjetoTrade(mContext).deletePrazo();
            new DBLocalizacaoCliente(mContext).deleteOk();
            DBEstoque dbEstoque = new DBEstoque(mContext);
            dbEstoque.deleteAuditagens();
            dbEstoque.deletaAuditagemCliente();
            dbEstoque.deletarPistolagensFinalizadas();
            new DBChamados(mContext).deleteFinalizados();
            dbVenda.deleteVendaNaoFinalizada();
            dbClienteCadastro.deletaAnexoDuplicados();
            new DBSolicitacaoMercadoria(mContext).deleteFinalizados();
            new DBPendencia(mContext).delete60diasPendenciaCliente();
            new DBProspectingClientAcquisition(mContext).delete60DaysOld();
            new DBRegistrationSimulationFee(mContext).deleteAll();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }
}
