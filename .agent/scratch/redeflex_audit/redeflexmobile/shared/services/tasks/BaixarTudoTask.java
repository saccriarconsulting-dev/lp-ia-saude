package com.axys.redeflexmobile.shared.services.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.services.bus.BancosBus;
import com.axys.redeflexmobile.shared.services.bus.CadastroVendedorPOSBus;
import com.axys.redeflexmobile.shared.services.bus.ChamadosBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteCadastroBus;
import com.axys.redeflexmobile.shared.services.bus.CnaeBus;
import com.axys.redeflexmobile.shared.services.bus.CobrancaBus;
import com.axys.redeflexmobile.shared.services.bus.DepartamentoBus;
import com.axys.redeflexmobile.shared.services.bus.EstoqueBus;
import com.axys.redeflexmobile.shared.services.bus.FilialBus;
import com.axys.redeflexmobile.shared.services.bus.FormaPagamentoBus;
import com.axys.redeflexmobile.shared.services.bus.IccidBus;
import com.axys.redeflexmobile.shared.services.bus.LimiteBus;
import com.axys.redeflexmobile.shared.services.bus.MotivoBus;
import com.axys.redeflexmobile.shared.services.bus.PermissaoBus;
import com.axys.redeflexmobile.shared.services.bus.PrecoBus;
import com.axys.redeflexmobile.shared.services.bus.ProjetoTradeBus;
import com.axys.redeflexmobile.shared.services.bus.RelatorioMetaBus;
import com.axys.redeflexmobile.shared.services.bus.RemessaBus;
import com.axys.redeflexmobile.shared.services.bus.RotaBus;
import com.axys.redeflexmobile.shared.services.bus.SegmentoBus;
import com.axys.redeflexmobile.shared.services.bus.SenhaClienteBus;
import com.axys.redeflexmobile.shared.services.bus.SolicitacaoMercadoriaBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaBus;
import com.axys.redeflexmobile.shared.services.bus.TaxasAdquirenciaPFBus;
import com.axys.redeflexmobile.shared.services.bus.TokenClienteBus;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.ui.redeflex.LoginActivity;

public class BaixarTudoTask extends AsyncTask<String, Void, String> {
    private ProgressDialog mDialog;
    private Context mContext;
    private boolean mInbackground;

    public BaixarTudoTask(Context pContext, boolean pInbackground) {
        mContext = pContext;
        mInbackground = pInbackground;
    }

    @Override
    protected String doInBackground(String... params) {
        int mTipoOperacao = 0;
        try {
            PermissaoBus.getPermissoes(mTipoOperacao, mContext);
            EstoqueBus.getProdutos(mTipoOperacao, mContext);
            ClienteBus.getClientes(mTipoOperacao, mContext);
            SenhaClienteBus.get(mTipoOperacao, mContext);
            RotaBus.getRota(mTipoOperacao, mContext);
            MotivoBus.getMotivos(mTipoOperacao, mContext);
            FormaPagamentoBus.getFormaPagamentos(mTipoOperacao, mContext);
            PrecoBus.getPrecoDiferenciado(mTipoOperacao, mContext);
            SolicitacaoMercadoriaBus.getSolicitacoes(mContext, mTipoOperacao);
            CobrancaBus.getCobranca(mTipoOperacao, mContext);
            RemessaBus.getRemessa(mTipoOperacao, mContext);
            SegmentoBus.getSegmento(mTipoOperacao, mContext);
            ProjetoTradeBus.getProjeto(mTipoOperacao, mContext);
            ProjetoTradeBus.getProjetoItens(mTipoOperacao, mContext);
            ClienteCadastroBus.getRetorno(mContext, mTipoOperacao);
            TaxasAdquirenciaBus.getTaxas(mTipoOperacao, mContext);
            TaxasAdquirenciaPFBus.getTaxas(mTipoOperacao, mContext);
            BancosBus.getBancos(mTipoOperacao, mContext);
            FilialBus.getFilial(mTipoOperacao, mContext);
            DepartamentoBus.getDepartamentos(mTipoOperacao, mContext);
            LimiteBus.get(mTipoOperacao, this.mContext);
            ChamadosBus.getChamados(mTipoOperacao, mContext);
            ChamadosBus.getInteracoes(mTipoOperacao, mContext);
            ChamadosBus.getAnexos(mTipoOperacao, mContext);
            RelatorioMetaBus.getRelatorioMeta(mContext, mTipoOperacao);
            CnaeBus.getCnae(mTipoOperacao, mContext);
            EstoqueBus.getEstrutura(mTipoOperacao, mContext);
            IccidBus.getIccid(mTipoOperacao, mContext);
            TokenClienteBus.get(mTipoOperacao, mContext);
            CadastroVendedorPOSBus.getVendedorPOS(mTipoOperacao, mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!mInbackground) {
            mDialog = ProgressDialog.show(mContext, mContext.getResources().getString(R.string.app_name), "Sincronizando Dados...", false, false);
            mDialog.setIcon(R.mipmap.ic_icone_new);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (!mInbackground) {
                if (mDialog != null)
                    mDialog.dismiss();
                Intent intent = new Intent(this.mContext, LoginActivity.class);
                this.mContext.startActivity(intent);
            }
        } catch (Exception ex) {
            if (!mInbackground) {
                if (!((Activity) mContext).isFinishing()) {
                    Mensagens.mensagemErro(mContext, ex.getMessage(), false);
                }
            }
        } finally {
            SimpleDbHelper.INSTANCE.close();
        }
    }
}