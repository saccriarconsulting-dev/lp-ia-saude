package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBModeloPOS;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Desenvolvimento on 28/06/2016.
 */
public class ModeloPOSBus extends BaseBus {

    public static void obterModelosPOS(Context context, int tipoCarga) {

        try {

            DBModeloPOS dbModeloPOS = new DBModeloPOS(context);
            Colaborador colaborador = new DBColaborador(context).get();

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.MODELO_POS,
                    colaborador.getId(),
                    tipoCarga);

            List<ModeloPOS> modelos = Arrays.asList(Utilidades
                    .getArrayObject(urlfinal, ModeloPOS[].class));

            if (modelos != null && !modelos.isEmpty()) {
                List<Integer> idList = new ArrayList<>();
                Stream.ofNullable(modelos).forEach(modeloPOS -> {
                    dbModeloPOS.salvarModeloPOS(modeloPOS);
                    idList.add(modeloPOS.getIdAppMobile());

                    Stream.ofNullable(modeloPOS.getConnections())
                            .map(conexao -> {
                                conexao.setIdModelPos(modeloPOS.getIdAppMobile());
                                return conexao;
                            })
                            .forEach(dbModeloPOS::salvarModeloPOSConexao);
                });
                setSync(URLs.MODELO_POS, idList, colaborador.getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}