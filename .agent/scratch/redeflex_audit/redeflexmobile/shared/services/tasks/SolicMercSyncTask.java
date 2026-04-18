package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.SituacaoSolicitacao;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoMercadoriaBus;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;

/**
 * Created by Desenvolvimento on 24/04/2016.
 */
public class SolicMercSyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public SolicMercSyncTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            SolicitacaoMercadoriaBus.enviarSolicitacoesPendentes(mContext);
            getSolicitacoesCriadasNoServer();
            getNovasSituacoes();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void getNovasSituacoes() {
        try {
            DBSolicitacaoMercadoria dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(mContext);
            DBColaborador dbColaborador = new DBColaborador(mContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.SITUACAO_SOLICITACAO + "?idVendedor=" + String.valueOf(colaborador.getId());
            SituacaoSolicitacao[] array = Utilidades.getArrayObject(urlfinal, SituacaoSolicitacao[].class);
            if (array != null) {
                for (SituacaoSolicitacao situacaoSolicitacao : array) {
                    if (dbSolicitacaoMercadoria.addSituacao(situacaoSolicitacao.getIdAppMobile(), situacaoSolicitacao.getIdStatus()
                            , Util_IO.dateTimeToString(situacaoSolicitacao.getData(), "yyyy-MM-dd HH:mm:ss"))) {
                        setSyncSituacao(colaborador.getId(), situacaoSolicitacao.getId(), situacaoSolicitacao.getIdAppMobile(), situacaoSolicitacao.getIdStatus());
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setSyncSituacao(int idVendedor, int idSolicitacao, int idAppModule, int idStatus) {
        try {
            String urlfinal = URLs.SITUACAO_SOLICITACAO
                    + "?idVendedor=" + String.valueOf(idVendedor)
                    + "&idSolicitacao=" + String.valueOf(idSolicitacao)
                    + "&idAppModule=" + String.valueOf(idAppModule)
                    + "&idStatus=" + String.valueOf(idStatus);

            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void getSolicitacoesCriadasNoServer() {
        try {
            DBSolicitacaoMercadoria dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(mContext);
            DBColaborador dbColaborador = new DBColaborador(mContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.SOLICITACAO_MERC + "?idVendedor=" + String.valueOf(colaborador.getId());
            SolicitacaoMercadoria[] array = Utilidades.getArrayObject(urlfinal, SolicitacaoMercadoria[].class);
            if (array != null) {
                for (SolicitacaoMercadoria item : array) {
                    long idAppMobile = dbSolicitacaoMercadoria.NovaSolicitacaoCriadaNoServer(item);
                    if (idAppMobile > 0)
                        setSyncSolicitacoesCriadasNoServer(item.getId(), idAppMobile);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setSyncSolicitacoesCriadasNoServer(int idSolicitacao, long idAppModule) {
        try {
            String urlfinal = URLs.SOLICITACAO_MERC
                    + "?idSolicitacao=" + String.valueOf(idSolicitacao)
                    + "&idAppModule=" + String.valueOf(idAppModule);
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}