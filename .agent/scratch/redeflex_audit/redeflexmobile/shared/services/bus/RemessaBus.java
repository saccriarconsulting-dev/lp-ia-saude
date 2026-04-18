package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBRemessa;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.shared.models.RemessaAnexo;
import com.axys.redeflexmobile.shared.models.RemessaItem;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class RemessaBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getRemessa(int pTipoCarga, Context pContext) {
        try {
            DBRemessa dbRemessa = new DBRemessa(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.REMESSA + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Remessa[] array = Utilidades.getArrayObject(urlfinal, Remessa[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Remessa item : array) {
                    dbRemessa.addRemessa(item);
                    idList.add(Integer.parseInt(item.getId()));
                    Notificacoes.Remessa(pContext, item, "Nova Remessa");
                }
                setSync(URLs.REMESSA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSync(String pIdServer) {
//        try {
//            String urlfinal = URLs.REMESSA + "?idServer=" + pIdServer;
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarConfirmacaoRemessa(Context pContext) {
        DBRemessa dbRemessa = new DBRemessa(pContext);
        ArrayList<Remessa> lista = dbRemessa.getRemessa(true);
        for (Remessa item : lista) {
            try {
                URL url = new URL(URLs.REMESSA);
                item.setListaitem(dbRemessa.getRemessaItem(item.getId()));

                String json = new Gson().toJson(item);

                String response = Utilidades.putRegistros(url, json.toString());
                if (response != null && response.equals("1")) {
                    dbRemessa.updateSync(dbRemessa.mTabelaRemessa, item.getId());
                    for (RemessaItem remessaItem : item.getListaitem()) {
                        dbRemessa.updateSync(dbRemessa.mTabelaItens, remessaItem.getId());
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Deprecated
    public static void enviarConfirmacaoRemessaItem(Context pContext) {
        DBRemessa dbRemessa = new DBRemessa(pContext);
        ArrayList<RemessaItem> lista = dbRemessa.getRemessaItem(null);
        for (RemessaItem item : lista) {
            try {
                URL url = new URL(URLs.REMESSA_ITEM);

                JSONObject main = new JSONObject();
                try {
                    main.put("id", item.getId());
                    main.put("itemCodeSAP", item.getItemCodeSAP());
                    main.put("qtdRemessa", item.getQtdRemessa());
                    main.put("qtdInformada", item.getQtdInformada());
                    main.put("dataConfirmacao", Util_IO.dateTimeToString(item.getDataConfirmacao(), Config.FormatDateTimeStringBanco));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

//                String response = Utilidades.postRegistros(url, main.toString());
//                if (response != null && response.equals("1"))
//                    dbRemessa.updateSync(dbRemessa.mTabelaItens, item.getId());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarAnexo(Context pContext) {
        DBRemessa dbRemessa = new DBRemessa(pContext);
        ArrayList<RemessaAnexo> lista = dbRemessa.getAnexos();
        for (RemessaAnexo item : lista) {
            try {
                Bitmap bImagem = BitmapFactory.decodeFile(item.getLocalArquivo());
                if (bImagem == null) {
                    dbRemessa.limpaAnexo(item.getIdRemessa());
                    Notificacoes.RemessaAnexo(pContext, item.getIdRemessa(), "Remessa " + item.getNumeroRemessa() + " pendente"
                            , "Favor, fotografar a imagem do comprovante de recebimento da remessa");
                } else {
                    URL url = new URL(URLs.REMESSA_ANEXO);
                    JSONObject main = new JSONObject();

                    try {
                        main.put("id", item.getIdRemessa());
                        main.put("imagem", Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (OutOfMemoryError ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.putRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbRemessa.updataSyncAnexo(item.getIdRemessa(), 1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getAnexo(int pTipoCarga, Context pContext) {
        try {
            DBRemessa dbRemessa = new DBRemessa(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.REMESSA_ANEXO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            RemessaAnexo[] array = Utilidades.getArrayObject(urlfinal, RemessaAnexo[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (RemessaAnexo item : array) {
                    dbRemessa.updataSyncAnexo(item.getIdRemessa(), Integer.valueOf(item.getSituacao()));
                    idList.add(Integer.parseInt(item.getIdRemessa()));
                    String mensagem = "";
                    if (item.getSituacao().equals("3"))
                        mensagem = "Favor, fotografar novamente a imagem do comprovante de recebimento da remessa";
                    else
                        mensagem = "A imagem do comprovante está ok";

                    String titulo = "Comprovante da Remessa " + item.getNumeroRemessa() + " " + (item.getSituacao().equals("3") ? "Recusado" : "Aprovado");
                    Notificacoes.RemessaAnexo(pContext, item.getIdRemessa(), titulo, mensagem);
                }
                setSync(URLs.REMESSA_ANEXO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void setSyncAnexo(String pIdServe) {
        try {
            String urlfinal = URLs.REMESSA_ANEXO + "?idServer=" + pIdServe;
            Utilidades.getObject(urlfinal, int.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}