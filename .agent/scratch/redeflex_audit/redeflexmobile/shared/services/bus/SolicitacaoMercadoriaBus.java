package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.SituacaoSolicitacao;
import com.axys.redeflexmobile.shared.models.SolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 28/04/2016.
 */
public class SolicitacaoMercadoriaBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getSolicitacoes(Context pContext, int pTipoOperacao) {
        try {
            DBSolicitacaoMercadoria dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.SOLICITACAO_MERC + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&isReSync=" + String.valueOf(pTipoOperacao);
            SolicitacaoMercadoria[] array = Utilidades.getArrayObject(urlfinal, SolicitacaoMercadoria[].class);
            if (array != null) {
                for (SolicitacaoMercadoria solicitacaoMercadoria : array) {
                    long idAppMobile = dbSolicitacaoMercadoria.AddSolicitacaoByReSync(solicitacaoMercadoria);
                    if (idAppMobile > 0) {
                        setIdAppModuleSolicitacao(solicitacaoMercadoria.getId(), idAppMobile);
                        getSituacoesByIdServer(solicitacaoMercadoria.getId(), idAppMobile, pContext);
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
    private static void setIdAppModuleSolicitacao(int pIdSolicitacao, long pIdAppModule) {
        try {
            String urlfinal = URLs.SOLICITACAO_MERC + "?idSolicitacao=" + String.valueOf(pIdSolicitacao) + "&idAppModule=" + String.valueOf(pIdAppModule) + "&isReSync=1";
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void getSituacoesByIdServer(int pIdSolicitacao, long pIdAppModule, Context pContext) {
        try {
            DBSolicitacaoMercadoria dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(pContext);
            String urlfinal = URLs.SITUACAO_SOLICITACAO + "?idSolicitacao=" + String.valueOf(pIdSolicitacao) + "&isReSync=1";
            SituacaoSolicitacao[] array = Utilidades.getArrayObject(urlfinal, SituacaoSolicitacao[].class);
            if (array != null) {
                for (SituacaoSolicitacao situacaoSolicitacao : array) {
                    dbSolicitacaoMercadoria.addSituacao(pIdAppModule, situacaoSolicitacao.getIdStatus(), Util_IO.dateTimeToString(situacaoSolicitacao.getData(), Config.FormatDateTimeStringBanco));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void enviarSolicitacoesPendentes(Context pContext) {
        DBSolicitacaoMercadoria dbSolicitacaoMercadoria = new DBSolicitacaoMercadoria(pContext);
        ArrayList<SolicitacaoMercadoria> list = dbSolicitacaoMercadoria.getPendentes();

        for (SolicitacaoMercadoria pSolicitacaoMercadoria : list) {
            try {
                URL url = new URL(URLs.SOLICITACAO_MERC);
                JSONObject main = new JSONObject();
                try {
                    main.put("id", pSolicitacaoMercadoria.getIdServer());
                    main.put("idAppMobile", pSolicitacaoMercadoria.getId());
                    main.put("idVendedor", String.valueOf(pSolicitacaoMercadoria.getIdVendedor()));
                    main.put("dataCriacao", Util_IO.dateTimeToString(pSolicitacaoMercadoria.getDataCriacao(), Config.FormatDateTimeStringBanco));
                    JSONArray itensjson = new JSONArray();
                    JSONObject item;
                    for (int i = 0; i < pSolicitacaoMercadoria.getItens().size(); i++) {
                        item = new JSONObject();
                        item.put("id", pSolicitacaoMercadoria.getItens().get(i).getId());
                        item.put("qtde", pSolicitacaoMercadoria.getItens().get(i).getQtde());
                        item.put("estoqueSugerido", pSolicitacaoMercadoria.getItens().get(i).getEstoqueSugerido());
                        item.put("estoqueMax", pSolicitacaoMercadoria.getItens().get(i).getEstoqueMax());
                        item.put("diasEstoque", pSolicitacaoMercadoria.getItens().get(i).getDiasEstoque());
                        item.put("estoqueAtual", pSolicitacaoMercadoria.getItens().get(i).getEstoqueAtual());
                        item.put("mediaDiariaVnd", pSolicitacaoMercadoria.getItens().get(i).getMediaDiariaVnd());
                        itensjson.put(item);
                    }
                    main.put("Itens", itensjson);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.postRegistros(url, main.toString());
                if (response != null && !response.equals("-1"))
                    dbSolicitacaoMercadoria.updateIdServer(response, pSolicitacaoMercadoria.getId());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}