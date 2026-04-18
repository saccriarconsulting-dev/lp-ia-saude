package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBAtualizarCliente;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.migracao.RegisterMigrationSubTax;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desenvolvimento on 24/06/2016.
 */
public class ClienteBus extends BaseBus {
    public static void getClientes(int pTipoCarga, Context pContext) {
        try {
            DBCliente dbCliente = new DBCliente(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.CLIENTE_MOBILE + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;
            Cliente[] array = Utilidades.getArrayObject(urlfinal, Cliente[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Cliente cliente : array) {
                    dbCliente.addCliente(cliente);
                    idList.add(Integer.parseInt(cliente.getId()));
                }
                setSync(URLs.CLIENTE_MOBILE, idList, colaborador.getId());
            }

            //verificarClientesAprovadosRecadastro(dbCliente, pContext);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void enviarAtualizacao(Context pContext) {
        DBCliente dbCliente = new DBCliente(pContext);
        ArrayList<Cliente> list = dbCliente.getPendentes();

        for (Cliente pCliente : list) {
            try {
                URL url = new URL(URLs.CLIENTE);
                JSONObject main = new JSONObject();
                try {
                    main.put("id", Util_IO.trataString(pCliente.getId()));
                    main.put("auditagem", Util_IO.trataString(pCliente.getAuditagem()));
                    main.put("celular", Util_IO.trataString(pCliente.getCelular()));
                    main.put("dddCelular", Util_IO.trataString(pCliente.getDddCelular()));
                    main.put("emailCliente", Util_IO.trataString(pCliente.getEmailCliente()));
                    main.put("atualizaBinario", pCliente.isAtualizaBinario());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                String result = Utilidades.postRegistros(url, main.toString());
                if (result != null && result.equals("1"))
                    dbCliente.updateSync(pCliente.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /*private static void verificarClientesAprovadosRecadastro(DBCliente dbCliente, Context pContext) {
        List<Integer> listaClientesAprovados = dbCliente.obterTodosClientesAprovadosRecadastro();
        if (!listaClientesAprovados.isEmpty()) {
            new DBAtualizarCliente(pContext).excluirClientesAprovados(listaClientesAprovados);
        }
    }*/
}
