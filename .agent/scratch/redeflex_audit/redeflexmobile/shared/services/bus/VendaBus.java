package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ListaDataPrazo;
import com.axys.redeflexmobile.shared.models.MobileCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.shared.models.VendaMobile;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joao.viana on 26/07/2016.
 */
public class VendaBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarVendasMobile(Context pContext) {
        DBVenda dbVenda = new DBVenda(pContext);
        ArrayList<VendaMobile> list = dbVenda.getVendasMobilePendentes();
        for (VendaMobile pVendaMobile : list) {
            try {
                // Verifica se tem Comprovante e converte para Base64
                if (!Util_IO.isNullOrEmpty(pVendaMobile.getComprovantePagto()))
                {
                    Bitmap bImagem;
                    bImagem = BitmapFactory.decodeFile(pVendaMobile.getComprovantePagto());

                    if (bImagem != null) {
                        try {
                            pVendaMobile.setNomeArquivoComprovante(new File(pVendaMobile.getComprovantePagto()).getName());
                            pVendaMobile.setComprovantePagto(Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                URL url = new URL(URLs.VENDA_MOBILE);
                String json = Utilidades.getJsonFromClass(pVendaMobile);

                String response = Utilidades.putRegistros(url, json);
                if (response != null && response.equals("1")) {
                    dbVenda.updateSyncVenda(pVendaMobile.getId());
                    if (pVendaMobile.getIdItem() > 0)
                        dbVenda.updateSyncItemVenda(pVendaMobile.getIdItem());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarMobileCodigoBarras(Context pContext) {
        DBVenda dbVenda = new DBVenda(pContext);
        ArrayList<MobileCodBarra> list = dbVenda.getVendasMobileCodBarraPendentes();
        for (MobileCodBarra pMobileCodBarra : list) {
            try {
                URL url = new URL(URLs.VENDA_MOBILE_BARRA);
                String response = Utilidades.postRegistros(url, Utilidades.getJsonFromClass(pMobileCodBarra));
                if (response != null) {
                    if (response.equalsIgnoreCase("1"))
                        dbVenda.updateSyncItemVendaCodigoBarra(pMobileCodBarra.getId());
                    else
                        new DBSuporte(pContext).addErroSync(pMobileCodBarra.getIdVendedor(), "ItemVendaCodigoBarra", pMobileCodBarra.getId());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getListaResolveDataPrazo(Context pContext) {
        try {
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();

            String urlfinal = URLs.RESOLVE_DATA_PRAZO + "?idVendedor=" + String.valueOf(colaborador.getId());
            ListaDataPrazo[] array = Utilidades.getArrayObject(urlfinal, ListaDataPrazo[].class);
            if (array != null && array.length > 0) {
                DBVenda dbVenda = new DBVenda(pContext);
                List<VendaMobile> listVenda = new ArrayList<>();
                for (ListaDataPrazo item : array) {
                    Venda venda = dbVenda.getVendabyId(item.getId());

                    if (venda.getDataCobranca() != null) {
                        String aa = Util_IO.dateTimeToString(venda.getDataCobranca(), Config.FormatDateTimeStringBanco);
                        if (aa == null) {
                            String bb = Util_IO.dateTimeToString(venda.getDataCobranca(), Config.FormatDateStringBr);
                            Date asda = Util_IO.stringToDate(bb, Config.FormatDateTimeStringBanco);
                            dbVenda.updateFormaPagamentoVenda(venda.getId(), venda.getIdFormaPagamento(), asda);
                        } else {
                            dbVenda.updateFormaPagamentoVenda(venda.getId(), venda.getIdFormaPagamento(), venda.getDataCobranca());
                        }

                        VendaMobile vendaMobile = dbVenda.getvendasMobileById(venda.getId());
                        listVenda.add(vendaMobile);
                    }
                }

                enviarResolveDataPrazo(listVenda);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static void enviarResolveDataPrazo(List<VendaMobile> listVenda) {
        try {
            URL url = new URL(URLs.RESOLVE_DATA_PRAZO);
            String json = Utilidades.getJsonFromClass(listVenda);

            String response = Utilidades.postRegistros(url, json);
            if (response != null && response.equals("1")) {
                String ignore = "ignore";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}