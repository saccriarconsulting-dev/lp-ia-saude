package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.util.Log;

import com.axys.redeflexmobile.shared.bd.BDCampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBInformacaoCorban;
import com.axys.redeflexmobile.shared.bd.DBInformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.models.CampanhaMerchanClaroMaterial;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.InformacaoCorban;
import com.axys.redeflexmobile.shared.models.InformacaoCorbanTransacao;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.models.RequestModelGeneric;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.hamcrest.core.IsNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class InformacaoCorbanBus extends BaseBus{
    private static final String InformacaoCorban_BUS = "InformacaoCorbanBus";

    public static void obterInformacaoCorban(int pTipoCarga, Context pContext) {
        try {
            DBInformacaoCorban dbInformacaoCorban = new DBInformacaoCorban(pContext);
            DBInformacaoCorbanTransacao dbInformacaoCorbanTransacao = new DBInformacaoCorbanTransacao(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_INFORMACAOCORBAN + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;
            InformacaoCorban[] array = Utilidades.getArrayObject(urlfinal, InformacaoCorban[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (InformacaoCorban informacaoCorban : array) {
                    dbInformacaoCorban.addInformacaoCorban(informacaoCorban);
                    for (InformacaoCorbanTransacao informacaoCorbanTransacao : informacaoCorban.getDadostransacoes()) {
                        informacaoCorbanTransacao.setIdcliente(informacaoCorban.getIdcliente());
                        dbInformacaoCorbanTransacao.addInformacaoCorbanTransacao(informacaoCorbanTransacao);
                    }
                    idList.add(informacaoCorban.getId());
                }
                setSync(URLs.URL_INFORMACAOCORBAN, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static RequestModelGeneric<InformacaoCorban> converterDados(RequestModel dados) {
        RequestModelGeneric<InformacaoCorban> requestModelGeneric = new RequestModelGeneric<>();
        requestModelGeneric.models = new ArrayList<>();
        requestModelGeneric.exception = dados.error;

        Gson gson = Utilidades.getGson();
        try {
            requestModelGeneric.models = gson.fromJson(dados.result, new TypeToken<List<InformacaoCorban>>() {
            }.getType());
            return requestModelGeneric;
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        } catch (JsonParseException e) {
            Timber.e(e);
        } catch (NullPointerException e) {
            Timber.e(e);
        }

        return requestModelGeneric;
    }
}
