package com.axys.redeflexmobile.shared.services.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.services.bus.AudOpeBus;
import com.axys.redeflexmobile.shared.services.bus.BancosBus;
import com.axys.redeflexmobile.shared.services.bus.CadastroVendedorPOSBus;
import com.axys.redeflexmobile.shared.services.bus.CanalSuporteBus;
import com.axys.redeflexmobile.shared.services.bus.ChamadosBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteBus;
import com.axys.redeflexmobile.shared.services.bus.ClienteCadastroBus;
import com.axys.redeflexmobile.shared.services.bus.CnaeBus;
import com.axys.redeflexmobile.shared.services.bus.CobrancaBus;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.services.bus.ComandosBus;
import com.axys.redeflexmobile.shared.services.bus.DepartamentoBus;
import com.axys.redeflexmobile.shared.services.bus.EstoqueBus;
import com.axys.redeflexmobile.shared.services.bus.FilialBus;
import com.axys.redeflexmobile.shared.services.bus.FormaPagamentoBus;
import com.axys.redeflexmobile.shared.services.bus.IccidBus;
import com.axys.redeflexmobile.shared.services.bus.LimiteBus;
import com.axys.redeflexmobile.shared.services.bus.LocalizacaoClienteBus;
import com.axys.redeflexmobile.shared.services.bus.MensagemBus;
import com.axys.redeflexmobile.shared.services.bus.MotivoBus;
import com.axys.redeflexmobile.shared.services.bus.OsBus;
import com.axys.redeflexmobile.shared.services.bus.PendenciasBus;
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
import com.axys.redeflexmobile.shared.services.bus.TelemetriaBus;
import com.axys.redeflexmobile.shared.services.bus.TokenClienteBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBoletoBus;
import com.axys.redeflexmobile.shared.services.bus.VendaBus;
import com.axys.redeflexmobile.shared.services.bus.VendaSenhaBus;
import com.axys.redeflexmobile.shared.services.bus.VendedorBus;
import com.axys.redeflexmobile.shared.services.bus.VisitaBus;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.MainActivity;

/**
 * Created by joao.viana on 27/09/2017.
 */

public class BaixarParcialTask extends AsyncTask<String, Void, String> {
    private ProgressDialog mDialog;
    private Context mContext;

    public BaixarParcialTask(Context pContext) {
        mContext = pContext;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            int mTipoOperacao = 1;
            ComandosBus.getComandos(mContext);
            PermissaoBus.getPermissoes(mTipoOperacao, mContext);
            ClienteBus.getClientes(mTipoOperacao, mContext);
            EstoqueBus.getProdutos(mTipoOperacao, mContext);
            TokenClienteBus.get(mTipoOperacao, mContext);
            SenhaClienteBus.get(mTipoOperacao, mContext);
            RotaBus.getRota(mTipoOperacao, mContext);
            LimiteBus.get(mTipoOperacao, mContext);
            RelatorioMetaBus.getRelatorioMeta(mContext, mTipoOperacao);
            MotivoBus.getMotivos(mTipoOperacao, mContext);
            CobrancaBus.getCobranca(mTipoOperacao, mContext);
            VendaBoletoBus.getBoletosGerados(mContext);
            FilialBus.getFilial(mTipoOperacao, mContext);
            RemessaBus.getRemessa(mTipoOperacao, mContext);
            PrecoBus.getPrecoDiferenciado(mTipoOperacao, mContext);
            SegmentoBus.getSegmento(mTipoOperacao, mContext);
            FormaPagamentoBus.getFormaPagamentos(mTipoOperacao, mContext);
            ClienteCadastroBus.getRetorno(mContext, mTipoOperacao);
            ClienteCadastroBus.getRetornoDoc(mContext);
            ProjetoTradeBus.getProjeto(mTipoOperacao, mContext);
            ProjetoTradeBus.getProjetoItens(mTipoOperacao, mContext);
            RemessaBus.getAnexo(mTipoOperacao, mContext);
            CnaeBus.getCnae(mTipoOperacao, mContext);
            BancosBus.getBancos(mTipoOperacao, mContext);
            IccidBus.getIccid(mTipoOperacao, mContext);
            OsBus.getOS(mContext, mTipoOperacao);
            TaxasAdquirenciaBus.getTaxas(mTipoOperacao, mContext);
            TaxasAdquirenciaPFBus.getTaxas(mTipoOperacao, mContext);
            ChamadosBus.getChamados(mTipoOperacao, mContext);
            ChamadosBus.getInteracoes(mTipoOperacao, mContext);
            ChamadosBus.getAnexos(mTipoOperacao, mContext);
            DepartamentoBus.getDepartamentos(mTipoOperacao, mContext);
            OsBus.getOS(mContext, mTipoOperacao);
            EstoqueBus.getEstrutura(mTipoOperacao, mContext);
            AudOpeBus.get(mTipoOperacao, mContext);
            CadastroVendedorPOSBus.getVendedorPOS(mTipoOperacao, mContext);
            VendedorBus.getVendedores(mContext);

            ClienteBus.enviarAtualizacao(mContext);
            VisitaBus.enviarPendentes(mContext);
            VendaBus.enviarVendasMobile(mContext);
            VendaBus.enviarMobileCodigoBarras(mContext);
            VendaSenhaBus.syncSenha(mContext);
            RemessaBus.enviarConfirmacaoRemessa(mContext);
            PrecoBus.atualizaIdVendaServer(mContext);
            ClienteCadastroBus.enviarCadastroCliente(mContext);
            ClienteCadastroBus.enviarAnexos(mContext);
            LocalizacaoClienteBus.enviarLocalizacao(mContext);
            RemessaBus.enviarAnexo(mContext);
            ChamadosBus.enviarChamados(mContext);
            EstoqueBus.enviarAuditoria(mContext);
            EstoqueBus.enviarAuditoriaCliente(mContext);
            ChamadosBus.enviarInteracoes(mContext);
            ChamadosBus.enviarAnexos(mContext);
            TelemetriaBus.enviarLocal(mContext);
            MensagemBus.setSyncVisualizacao(mContext);
            SolicitacaoMercadoriaBus.enviarSolicitacoesPendentes(mContext);
            CanalSuporteBus.enviarSolicitacao(mContext);
            PendenciasBus.enviarPendencias(mContext);

            // Colaborador
            Colaborador colaborador = ColaboradorBus.getServer(mContext);
            if (colaborador != null && colaborador.isOk() && colaborador.getId() > 0)
                new DBColaborador(mContext).atualiza(colaborador);
            return "Dados sincronizados com sucesso!";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Erro na sincronização";
        }
    }

    protected void onPreExecute() {
        try {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Sincronizando Dados...");
            mDialog.setIndeterminate(true);
            mDialog.setIcon(R.drawable.ic_action_rf);
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception ex) {
            if (!((Activity) mContext).isFinishing()) {
                Mensagens.mensagemErro(mContext, ex.getMessage(), false);
            }
        }
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                Alerta msg = new Alerta(mContext, "Informação", result);
                msg.show();
                if (mContext instanceof MainActivity) {
                    Utilidades.listarPendencias(mContext);
                    Utilidades.listarRotas(mContext);
                }
            }
        } catch (Exception ex) {
            if (!((Activity) mContext).isFinishing()) {
                Mensagens.mensagemErro(mContext, ex.getMessage(), false);
            }
        } finally {
            SimpleDbHelper.INSTANCE.close();
        }
    }
}
