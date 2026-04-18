package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPid;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidPOS;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidProduto;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidRede;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPidTaxaMDR;
import com.axys.redeflexmobile.shared.models.SolicitacaoPid;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidAnexo;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidRetorno;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidStatus;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidStatusRetorno;
import com.axys.redeflexmobile.shared.models.SolicitacaoPidTaxaRAV;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SolicitacaoPidBus extends BaseBus {
    public static void enviarSolicitacaoPid(Context pContext) {
        BDSolicitacaoPid bdSolicitacaoPid = new BDSolicitacaoPid(pContext);
        ArrayList<SolicitacaoPid> list = bdSolicitacaoPid.getSolicitacaoPid(" and Sincronizado = 0", null);
        ArrayList<Integer> serverIds = new ArrayList<Integer>();
        for (SolicitacaoPid pSolicitacao : list) {
            pSolicitacao.setListaPOS(new BDSolicitacaoPidPOS(pContext).getSolicitacaoPidPOS("and IdSolicitacaoPid = " + pSolicitacao.getId(),null));
            pSolicitacao.setListaRedes(new BDSolicitacaoPidRede(pContext).getSolicitacaoPidRede("and IdSolicitacaoPid = " + pSolicitacao.getId(),null));
            pSolicitacao.setListaTaxas(new BDSolicitacaoPidTaxaMDR(pContext).getSolicitacaoPidTaxaMDR("and IdSolicitacaoPid = " + pSolicitacao.getId(),null));
            pSolicitacao.setProduto(new BDSolicitacaoPidProduto(pContext).getSolicitacaoPidProduto("and IdSolicitacaoPid = " + pSolicitacao.getId(),null));

            // Carregando Taxas Rav
            SolicitacaoPidTaxaRAV solicitacaoPidTaxaRAV = new SolicitacaoPidTaxaRAV();
            if (pSolicitacao.getListaTaxas() != null && pSolicitacao.getListaTaxas().size()>0) {
                if(pSolicitacao.getListaTaxas().get(0).getTaxaRavEventualConcorrente().isPresent())
                    solicitacaoPidTaxaRAV.setConcorrenciaRavEventual(pSolicitacao.getListaTaxas().get(0).getTaxaRavEventualConcorrente().get());
                if(pSolicitacao.getListaTaxas().get(0).getTaxaRavAutomaticaConcorrente().isPresent())
                    solicitacaoPidTaxaRAV.setConcorrenciaRavAutomatica(pSolicitacao.getListaTaxas().get(0).getTaxaRavAutomaticaConcorrente().get());

                if(pSolicitacao.getListaTaxas().get(0).getTaxaRavEventual().isPresent())
                    solicitacaoPidTaxaRAV.setNovaPropostaRavEventual(pSolicitacao.getListaTaxas().get(0).getTaxaRavEventual().get());
                if(pSolicitacao.getListaTaxas().get(0).getTaxaRavAutomatica().isPresent())
                    solicitacaoPidTaxaRAV.setNovaPropostaRavAutomatica(pSolicitacao.getListaTaxas().get(0).getTaxaRavAutomatica().get());
            }
            pSolicitacao.setTaxaRAV(solicitacaoPidTaxaRAV);

            // Carrega Anexos
            ArrayList<SolicitacaoPidAnexo> listaAnexosEnvio = new ArrayList<>();
            ArrayList<SolicitacaoPidAnexo> listaAnexos = new BDSolicitacaoPidAnexo(pContext).getSolicitacaoPidAnexo("and IdSolicitacaoPid = " + pSolicitacao.getId(),null);
            for (SolicitacaoPidAnexo itemAnexo: listaAnexos) {
                Bitmap bImagem;
                bImagem = BitmapFactory.decodeFile(itemAnexo.getAnexo());

                if (bImagem != null) {
                    try {
                        itemAnexo.setImagem(Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                        itemAnexo.setNomeArquivo(new File(itemAnexo.getAnexo()).getName());
                        itemAnexo.setTipo(itemAnexo.getTipoArquivo());
                        itemAnexo.setTipoArquivo("imagem/jpg");
                        listaAnexosEnvio.add(itemAnexo);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            pSolicitacao.setAnexos(listaAnexosEnvio);

            // Envio da Solicitação
            try {
                URL url = new URL(URLs.URL_SOLICITACAOPID);
                String json = Utilidades.getJsonFromClass(pSolicitacao);

                Log.d("Roni", "enviarSolicitacaoPid: " + json);

                String response = Utilidades.putRegistros(url, json);
                if (response != null) {
                    SolicitacaoPidRetorno retornoSolicitacao = Utilidades.getClassFromJson(response, SolicitacaoPidRetorno.class);
                    if (retornoSolicitacao.isSucesso()) {
                        bdSolicitacaoPid.updateSyncSolicitacaoPid(pSolicitacao.getId(), retornoSolicitacao);
                        serverIds.add(retornoSolicitacao.getIdSolicitacaoPid());
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
       updateStatusSolicitacaoPid(serverIds, bdSolicitacaoPid);
    }

    public static void getStatusSolicitacaoPid(Context pContext){
        try {
            BDSolicitacaoPid bdSolicitacaoPid = new BDSolicitacaoPid(pContext);
            ArrayList<Integer> serverIds = bdSolicitacaoPid.getServerIds();
            updateStatusSolicitacaoPid(serverIds, bdSolicitacaoPid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void updateStatusSolicitacaoPid(ArrayList<Integer> serverIds,BDSolicitacaoPid bdSolicitacaoPid ){
        for (SolicitacaoPidStatus solicitacaoPid: CallEndpointStatusSolicitacaoPid(serverIds)) {
            try {
                bdSolicitacaoPid.updateStatusSyncSolicitacaoPid(solicitacaoPid);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private static ArrayList<SolicitacaoPidStatus> CallEndpointStatusSolicitacaoPid(ArrayList<Integer> serverIds) {
        ArrayList<SolicitacaoPidStatus> retornoStatusPid = new ArrayList<SolicitacaoPidStatus>();
        try {
            URL urlStatusPid = new URL(URLs.URL_SOLICITACAOPID_STATUS);
            String json = Utilidades.getJsonFromClass(serverIds);

            String response = Utilidades.postRegistros(urlStatusPid, json);
            if (response != null) {
                SolicitacaoPidStatusRetorno retorno = Utilidades.getClassFromJson(response, SolicitacaoPidStatusRetorno.class);
                if(retorno != null && retorno.isSucesso()){
                    retornoStatusPid = retorno.getSolicitacoes();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retornoStatusPid;
    }
}