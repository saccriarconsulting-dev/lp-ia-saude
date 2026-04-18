package com.axys.redeflexmobile.shared.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.AudOpeBus;
import com.axys.redeflexmobile.shared.services.bus.BancosBus;
import com.axys.redeflexmobile.shared.services.bus.RegisterMigrationSubBus;
import com.axys.redeflexmobile.shared.services.bus.CanalSuporteBus;
import com.axys.redeflexmobile.shared.services.bus.CnaeBus;
import com.axys.redeflexmobile.shared.services.bus.DepartamentoBus;
import com.axys.redeflexmobile.shared.services.bus.DevolucaoBus;
import com.axys.redeflexmobile.shared.services.bus.FilialBus;
import com.axys.redeflexmobile.shared.services.bus.FormaPagamentoBus;
import com.axys.redeflexmobile.shared.services.bus.LimiteBus;
import com.axys.redeflexmobile.shared.services.bus.MotivoBus;
import com.axys.redeflexmobile.shared.services.bus.MotiveMigrationSubBus;
import com.axys.redeflexmobile.shared.services.bus.PendenciasBus;
import com.axys.redeflexmobile.shared.services.bus.RelatorioMetaBus;
import com.axys.redeflexmobile.shared.services.bus.SegmentoBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaPFBus;
import com.axys.redeflexmobile.shared.services.bus.VendaSenhaBus;

/**
 * Created by joao.viana on 27/04/2017.
 */

public class SyncTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private int mTipoOperacao = 1;

    public SyncTask(Context pContext, int pTipoOperacao) {
        mContext = pContext;
        mTipoOperacao = pTipoOperacao;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            VendaSenhaBus.syncSenha(mContext);
            VendaSenhaBus.getSenha(mTipoOperacao, mContext);
            FormaPagamentoBus.getFormaPagamentos(this.mTipoOperacao, this.mContext);
            LimiteBus.get(this.mTipoOperacao, this.mContext);
            BancosBus.getBancos(this.mTipoOperacao, this.mContext);
            SegmentoBus.getSegmento(this.mTipoOperacao, this.mContext);
            RelatorioMetaBus.getRelatorioMeta(this.mContext, this.mTipoOperacao);
            DepartamentoBus.getDepartamentos(this.mTipoOperacao, this.mContext);
            FilialBus.getFilial(this.mTipoOperacao, this.mContext);
            MotivoBus.getMotivos(this.mTipoOperacao, this.mContext);
            CnaeBus.getCnae(this.mTipoOperacao, this.mContext);
            TaxasAdquirenciaBus.getTaxas(this.mTipoOperacao, this.mContext);
            TaxasAdquirenciaPFBus.getTaxas(this.mTipoOperacao, this.mContext);
            CanalSuporteBus.enviarSolicitacao(this.mContext);
            DevolucaoBus.enviarDevolucao(this.mContext);
            PendenciasBus.enviarPendencias(this.mContext);
            AudOpeBus.get(this.mTipoOperacao, this.mContext);
            RegisterMigrationSubBus.send(this.mContext);
            MotiveMigrationSubBus.get(mTipoOperacao, this.mContext);
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
