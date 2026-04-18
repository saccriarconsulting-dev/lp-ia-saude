package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.BDConsignado;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItem;
import com.axys.redeflexmobile.shared.bd.BDConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.bd.BDSolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Consignado;
import com.axys.redeflexmobile.shared.models.ConsignadoItem;
import com.axys.redeflexmobile.shared.models.ConsignadoItemCodBarra;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.SolicitacaoPrecoDiferenciadoDetalhe;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitacaoPrecoDiferenciadoBus extends BaseBus {
     public static void enviarSolPrecoDiferenciado(Context pContext) {
        BDSolicitacaoPrecoDiferenciado bdPrecoDiferenciado = new BDSolicitacaoPrecoDiferenciado(pContext);
        ArrayList<SolicitacaoPrecoDiferenciado> list = bdPrecoDiferenciado.getSolicitacaoPendente();
        for (SolicitacaoPrecoDiferenciado pSolicitacao : list) {
            try {
                URL url = new URL(URLs.URL_SOLICITACAOPRECODIFERENCIADO);
                String json = Utilidades.getJsonFromClass(pSolicitacao);
                String response = Utilidades.putRegistros(url, json);
                if (response != null) {
                    SolicitacaoPrecoDiferenciado returSolicitacao = Utilidades.getClassFromJson(response, SolicitacaoPrecoDiferenciado.class);
                    if (returSolicitacao != null)
                        bdPrecoDiferenciado.updateSyncSolicitacao(returSolicitacao);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void getSolPrecoDiferenciado(int tipoCarga, Context context) {
        try {
            DBColaborador dbColaborador = new DBColaborador(context);
            Colaborador colaborador = dbColaborador.get();

            BDSolicitacaoPrecoDiferenciado dbSolicitacao = new BDSolicitacaoPrecoDiferenciado(context);
            BDSolicitacaoPrecoDiferenciadoDetalhe dbSolicitacaoDetalhe = new BDSolicitacaoPrecoDiferenciadoDetalhe(context);

            String urlfinal = URLs.URL_SOLICITACAOPRECODIFERENCIADO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + tipoCarga;
            SolicitacaoPrecoDiferenciado[] array = Utilidades.getArrayObject(urlfinal, SolicitacaoPrecoDiferenciado[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (SolicitacaoPrecoDiferenciado item : array) {
                    item.setId(0);
                    item.setDataSincronizacao(new Date());
                    item.setSync(1);
                    long idSolicitacao = dbSolicitacao.addSolicitacao(item);
                    for (SolicitacaoPrecoDiferenciadoDetalhe itemSolicitacao : item.getItensPrecoDif()) {
                        itemSolicitacao.setId(0);
                        itemSolicitacao.setIdSolicitacao((int) idSolicitacao);
                        long idItemSolicitacao = dbSolicitacaoDetalhe.addSolicitacaoDetalhes(itemSolicitacao);
                    }
                    idList.add(item.getIdServerSolicitacao());
                }
                setSync(URLs.URL_SOLICITACAOPRECODIFERENCIADO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
