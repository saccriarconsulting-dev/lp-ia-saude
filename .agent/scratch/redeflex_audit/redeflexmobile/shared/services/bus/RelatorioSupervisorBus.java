package com.axys.redeflexmobile.shared.services.bus;

import com.axys.redeflexmobile.shared.models.RelatorioSupervisorVendedor;
import com.axys.redeflexmobile.shared.models.RequestModel;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RelatorioSupervisorBus {

    public static RequestModel obterVendedores(String idColoborador) {
        try {

            return Utilidades.getRequest(URLs.VENDEDORES_SUPERVISOR + "/" + idColoborador);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RequestModel obterResumoVenda(RelatorioSupervisorVendedor relatorio) {
        try {
            String json = Utilidades.getGson().toJson(relatorio);
            json = json.replace("IdVendedor", "idSupervisor");
            URL url = new URL(URLs.VENDAS_RESUMO);
            return Utilidades.postRequest(url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RequestModel obterVendasVendedor(RelatorioSupervisorVendedor relatorio) {
        try {
            String json = Utilidades.getGson().toJson(relatorio);
            URL url = new URL(URLs.VENDAS_VENDEDOR);
            return Utilidades.postRequest(url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
