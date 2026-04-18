package com.axys.redeflexmobile.shared.services.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RetBoleto;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bus.CobrancaBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBoletoBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBus;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Cobranca2Activity;

import java.io.IOException;

/**
 * Created by joao.viana on 28/07/2016.
 */
public class GerarBoletoTask extends AsyncTask<String, Void, String> {
    private ProgressDialog mDialog;
    private Context mContext;

    public GerarBoletoTask(Context pContext) {
        this.mContext = pContext;
    }

    protected String doInBackground(String... params) {
        try {
            VendaBus.enviarVendasMobile(this.mContext);
            VendaBus.enviarMobileCodigoBarras(this.mContext);
            String retorno = gerar();
            CobrancaBus.getCobranca(1, this.mContext);
            VendaBoletoBus.getBoletosGerados(this.mContext);
            return retorno;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = ProgressDialog.show(mContext, mContext.getResources().getString(R.string.app_name), "Aguarde, Gerando Boleto(s)!!", false, false);
        mDialog.setIcon(R.mipmap.ic_icone_new);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            mDialog.dismiss();
            if (!Util_IO.isNullOrEmpty(result))
                Utilidades.retornaMensagem(this.mContext, result, true);
            Utilidades.openNewActivity(mContext, Cobranca2Activity.class, null, true);
        } catch (Exception ex) {
            Mensagens.mensagemErro(mContext, ex.getMessage(), false);
        } finally {
            SimpleDbHelper.INSTANCE.close();
        }
    }

    private String gerar() {
        try {
            DBColaborador dbColaborador = new DBColaborador(mContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.BOLETO + "/" + String.valueOf(colaborador.getId());
            RetBoleto ret = Utilidades.getObject(urlfinal, RetBoleto.class);
            if (ret.isErro())
                throw new Exception(ret.getMensagem());
            else
                return ret.getMensagem();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Erro ao gerar boleto: " + ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Erro ao gerar boleto: " + ex.getMessage();
        }
    }
}