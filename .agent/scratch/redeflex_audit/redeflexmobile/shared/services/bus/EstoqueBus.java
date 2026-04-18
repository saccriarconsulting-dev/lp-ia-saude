package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.models.AuditagemCliente;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.PrecoFormaCache;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/06/2016.
 */
public class EstoqueBus extends BaseBus {

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getProdutos(int pTipoCarga, Context pContext) {
        try {
            DBEstoque dbEstoque = new DBEstoque(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.ESTOQUE
                    + "?idVendedor=" + String.valueOf(colaborador.getId())
                    + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Produto[] array = Utilidades.getArrayObject(urlfinal, Produto[].class);
            if (array != null && array.length > 0) {
                ArrayList<String> idList = new ArrayList<>();
                for (Produto item : array) {
                    if (!dbEstoque.isProdutoSendoVendido(item.getId())) {
                        dbEstoque.addProduto(item);
                        PrecoFormaCache.put(item);
                        idList.add(item.getId());
                    }
                }
                setSyncString(URLs.ESTOQUE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarAuditoria(Context pContext) {
        try {
            DBEstoque dbEstoque = new DBEstoque(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            ArrayList<AuditagemEstoque> list = dbEstoque.getAuditagemEstoquePendentesSync();
            for (AuditagemEstoque auditagemEstoque : list) {
                auditagemEstoque.setIdVendedor(String.valueOf(colaborador.getId()));
                JSONObject main = new JSONObject();
                try {
                    main.put("idVendedor", auditagemEstoque.getIdVendedor());
                    main.put("idProduto", auditagemEstoque.getIdProduto());
                    main.put("qtdeInformada", auditagemEstoque.getQtdeInformada());
                    main.put("qtdeReal", auditagemEstoque.getQtdeReal());
                    main.put("data", Util_IO.dateTimeToString(
                            auditagemEstoque.getData(),
                            Config.FormatDateTimeStringBanco
                    ));
                    main.put("codigoBarra", auditagemEstoque.getCodigoBarra());
                    main.put("codigoBarraFinal", auditagemEstoque.getCodigoBarraFinal());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.postRegistros(
                        new URL(URLs.AUDITAGEMESTOQUE),
                        main.toString()
                );
                if (response != null && response.equals("1")) {
                    dbEstoque.setSyncAuditagemEstoque(auditagemEstoque.getId());
                    if (auditagemEstoque.getIdCodigoBarra() > 0) {
                        dbEstoque.setSyncAuditagemEstoqueItem(auditagemEstoque.getIdCodigoBarra());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarAuditoriaCliente(Context pContext) {
        try {
            DBEstoque dbEstoque = new DBEstoque(pContext);
            ArrayList<AuditagemCliente> list = dbEstoque.getAuditagensClientePendentes();
            Colaborador colaborador = new DBColaborador(pContext).get();

            for (AuditagemCliente auditagemCliente : list) {
                JSONObject main = new JSONObject();
                try {
                    main.put("idVendedor", colaborador.getId());
                    main.put("idCliente", auditagemCliente.getIdCliente());
                    main.put("idProduto", auditagemCliente.getIdProduto());
                    main.put(
                            "data",
                            Util_IO.dateTimeToString(
                                    auditagemCliente.getData(),
                                    Config.FormatDateTimeStringBanco
                            )
                    );

                    CodBarra barra = new CodBarra();
                    barra.setCodBarraInicial(auditagemCliente.getCodigoBarra());
                    barra.setIndividual(
                            auditagemCliente.getCodigoBarraFinal() == null
                                    || auditagemCliente.getCodigoBarraFinal().length() == 0
                    );
                    barra.setCodBarraFinal(auditagemCliente.getCodigoBarraFinal());

                    String qtdBarraStr = barra.retornaQuantidade(UsoCodBarra.AUDITAGEM_CLIENTE);

                    int qtdTotal = auditagemCliente.getQuantidade();

                    String codInicial = barra.getCodBarraInicial();
                    if (codInicial != null && !codInicial.isEmpty()) {
                        try {
                            qtdTotal = Integer.parseInt(qtdBarraStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    main.put("qtdTotal", qtdTotal);
                    main.put("codigoBarra", barra.getCodBarraInicial());
                    main.put("codigoBarraFinal", barra.getCodBarraFinal());
                    main.put("qtdBarra", qtdBarraStr);

                    main.put("versaoApp", auditagemCliente.getVersaoApp());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.postRegistros(
                        new URL(URLs.AUDITAGEM_CLIENTE),
                        main.toString()
                );
                if (response != null && response.equals("1")) {
                    dbEstoque.setSyncAuditagemCliente(auditagemCliente.getId());
                    if (auditagemCliente.getIdCodigoBarra() > 0) {
                        dbEstoque.setSyncCodBarraCliente(auditagemCliente.getIdCodigoBarra());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getEstrutura(int pTipoCarga, Context pContext) {
        try {
            DBEstoque dbProduto = new DBEstoque(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.PRODUTO
                    + "?idVendedor=" + String.valueOf(colaborador.getId())
                    + "&tipoCarga=" + String.valueOf(pTipoCarga);
            EstruturaProd[] array = Utilidades.getArrayObject(urlfinal, EstruturaProd[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (EstruturaProd estruturaProd : array) {
                    dbProduto.addEstrutura(estruturaProd);
                    idList.add(estruturaProd.getId());
                }
                setSync(URLs.PRODUTO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}