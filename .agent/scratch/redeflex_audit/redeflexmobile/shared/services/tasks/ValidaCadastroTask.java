package com.axys.redeflexmobile.shared.services.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;

/**
 * Created by Desenvolvimento on 26/04/2016.
 */
public class ValidaCadastroTask extends AsyncTask<String, Void, String> {
    private Context mContext;

    public ValidaCadastroTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (Utilidades.isConectado(mContext)) {
                DBColaborador dbColaborador = new DBColaborador(mContext);
                Colaborador atual = dbColaborador.get();
                Colaborador server = ColaboradorBus.getServer(mContext);
                if (server != null) {
                    if (server.isOk()) {
                        if (atual == null) {
                            if (server.getId() > 0) {
                                dbColaborador.add(server);
                                Utilidades.addRegId(server.getId());
                                return "BAIXAR";
                            } else
                                return "OFF";
                        } else {
                            if (server.getId() > 0) {
                                if (atual.getId() != server.getId()) {
                                    Utilidades.removeRegId(atual.getId());
                                    Utilidades.addRegId(server.getId());
                                    //remove tudo
                                    Utilidades.deletarTudo(mContext);

                                    //baixar tudo novamente
                                    dbColaborador.atualiza(server);
                                    return "BAIXAR";
                                } else {
                                    dbColaborador.atualiza(server);
                                    return "OK";
                                }
                            } else {
                                return "OFF";
                            }
                        }
                    } else if (atual != null) {
                        dbColaborador.delete(atual.getId());
                        Utilidades.deletarTudo(mContext);
                        Utilidades.removeRegId(atual.getId());
                        return "REMOVEU";
                    }
                } else {
                    return "OFF";
                }
                return "OFF";
            } else
                return "OFF";
        } catch (IOException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SimpleDbHelper.INSTANCE.close();
        try {
            if (result != null) {
                switch (result) {
                    case "OK":
                        new PermissaoSyncTask(mContext, 1).execute();
                        new EstoqueSyncTask(mContext, 1).execute();
                        new ClienteSyncTask(mContext, 1).execute();
                        new SolicMercSyncTask(mContext).execute();
                        new RotaSyncTask(mContext, 1).execute();
                        new OsSyncTask(mContext, 1).execute();
                        new MensagemTask(mContext).execute();
                        new CobrancaTask(mContext, 1).execute();
                        new RemessaTask(mContext, 1).execute();
                        new PrecoTask(mContext, 1).execute();
                        new LocalizacaoClienteTask(mContext).execute();
                        new ClienteCadastroTask(mContext, 1).execute();
                        new SyncTask(mContext, 1).execute();
                        new IccidTaks(mContext, 1).execute();
                        new ChamadosTask(mContext, 1).execute();
                        break;
                    case "BAIXAR":
                        //new BaixarTudoTask(mContext, true).execute();
                        break;
                    case "OFF":
                        break;
                    case "REMOVEU":
                        if (!((Activity) mContext).isFinishing()) {
                            Mensagens.colaboradorSemImeiVinculado(mContext);
                        }
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}