package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTroca;
import com.axys.redeflexmobile.shared.models.solicitacaotroca.SolicitacaoTrocaMotivo;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.network.util.JsonExcludeStrategy;
import com.axys.redeflexmobile.shared.util.DateSerializer;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SolicitacaoTrocaBus extends BaseBus {

    public static void getSolicitacaoTrocaMotivo(Context context, int pTipoRecarga) {
        try {
            Colaborador colaborador = new DBColaborador(context).get();
            DBSolicitacaoTroca dbSolicitacaoTroca = new DBSolicitacaoTroca(context);

            String urlfinal = String.format("%s?idVendedor=%s&tipoRecarga=%s",
                    URLs.SOLICITACAO_TROCA_MOTIVO,
                    String.valueOf(colaborador.getId()),
                    String.valueOf(pTipoRecarga));

            SolicitacaoTrocaMotivo[] array = Utilidades.getArrayObject(urlfinal, SolicitacaoTrocaMotivo[].class);

            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                Stream.ofNullable(array).forEach(solicitacaoTrocaMotivo -> {
                    dbSolicitacaoTroca.salvarSolicitacaoTrocaMotivo(solicitacaoTrocaMotivo);
                    idList.add(solicitacaoTrocaMotivo.getIdServer());
                });
                setSync(URLs.SOLICITACAO_TROCA_MOTIVO, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getSolicitacaoTroca(Context context, int pTipoRecarga) {
        try {
            Colaborador colaborador = new DBColaborador(context).get();
            DBSolicitacaoTroca dbSolicitacaoTroca = new DBSolicitacaoTroca(context);

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.SOLICITACAO_TROCA,
                    String.valueOf(colaborador.getId()),
                    String.valueOf(pTipoRecarga));

            String resultString = Utilidades.getRegistros(urlfinal);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .setExclusionStrategies(new JsonExcludeStrategy())
                    .setLenient()
                    .create();
            List<SolicitacaoTroca> solicitacoes = Arrays.asList(gson
                    .fromJson(resultString, SolicitacaoTroca[].class));

            if (solicitacoes != null && !solicitacoes.isEmpty()) {
                ArrayList<Integer> idList = new ArrayList<>();
                Stream.ofNullable(solicitacoes).forEach(solicitacao -> {
                    dbSolicitacaoTroca.salvarSolicitacaoSincronizacao(solicitacao);
                    idList.add(solicitacao.getIdServer());
                });
                setSync(URLs.SOLICITACAO_TROCA, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void enviarSolicitacoes(Context context) {
        Gson gson = Utilidades.getGsonInstance();
        DBSolicitacaoTroca dbSolicitacaoTroca = new DBSolicitacaoTroca(context);
        Stream.ofNullable(dbSolicitacaoTroca.obterSolicitacoesSincronizacao(false))
                .forEach(solicitacao -> {
                    try {
                        URL url = new URL(URLs.SOLICITACAO_TROCA);
                        String response = Utilidades.putRegistros(url, gson.toJson(solicitacao));
                        if (response != null && response.equals("1")) {
                            dbSolicitacaoTroca.setarSyncSolicitacao(solicitacao);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void enviarSolicitacoesAtualizacao(Context context) {
        Gson gson = Utilidades.getGsonInstance();
        DBSolicitacaoTroca dbSolicitacaoTroca = new DBSolicitacaoTroca(context);
        Stream.ofNullable(dbSolicitacaoTroca.obterSolicitacoesSincronizacao(true))
                .forEach(solicitacao -> Stream.ofNullable(solicitacao.getProdutos())
                        .forEach(produto -> {
                            try {
                                URL url = new URL(URLs.SOLICITACAO_TROCA);
                                String response = Utilidades.putRegistros(url, gson.toJson(solicitacao));
                                if (response != null && response.equals("1")) {
                                    dbSolicitacaoTroca.setarSyncSolicitacao(solicitacao);
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }));
    }
}