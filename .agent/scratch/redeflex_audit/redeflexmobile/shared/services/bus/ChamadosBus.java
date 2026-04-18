package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.axys.redeflexmobile.shared.bd.DBChamados;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.models.AnexoChamado;
import com.axys.redeflexmobile.shared.models.Chamado;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Interacoes;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joao.viana on 07/03/2017.
 */

public class ChamadosBus extends BaseBus {
    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getChamados(int pTipoCarga, Context pContext) {
        try {
            DBChamados dbChamados = new DBChamados(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.CHAMADOS + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Chamado[] array = Utilidades.getArrayObject(urlfinal, Chamado[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Chamado chamado : array) {
                    boolean bAlteracao = dbChamados.addChamados(chamado);
                    idList.add(chamado.getChamadoID());
                    if (array.length < 5 && pTipoCarga != 0) {
                        Notificacoes.Chamado(pContext, dbChamados.retornaCodigo(chamado.getId()), "Chamado: " + String.valueOf(chamado.getChamadoID())
                                , (bAlteracao) ? "Nova Alteração de chamado" : "Novo Chamado");
                    }
                }
                setSync(URLs.CHAMADOS, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncChamado(int pIdServer, int pIdColaborador) {
//        try {
//            String urlfinal = URLs.CHAMADOS + "?idChamado=" + String.valueOf(pIdServer) + "&idVendedor=" + String.valueOf(pIdColaborador);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getInteracoes(int pTipoCarga, Context pContext) {
        try {
            DBChamados dbChamados = new DBChamados(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.INTERACOES + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            Interacoes[] array = Utilidades.getArrayObject(urlfinal, Interacoes[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (Interacoes interacoes : array) {
                    if (dbChamados.addInteracoesWebService(interacoes)) {
                        idList.add(interacoes.getInteracaoID());
                        if (interacoes.getIdAppMobile() == null && pTipoCarga != 0 && array.length < 5)
                            Notificacoes.Chamado(pContext, dbChamados.retornaCodigoInteracao(interacoes.getChamadoID())
                                    , "Chamado: " + String.valueOf(interacoes.getChamadoID()), "Nova interação no chamado");
                    }
                }
                setSync(URLs.INTERACOES, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncInteracao(int pIdServer, int pIdColaborador) {
//        try {
//            String urlfinal = URLs.INTERACOES + "?idInteracao=" + String.valueOf(pIdServer) + "&idVendedor=" + String.valueOf(pIdColaborador);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarInteracoes(Context pContext) {
        DBChamados dbChamados = new DBChamados(pContext);
        ArrayList<Interacoes> list = dbChamados.getInteracoesPendentes();
        for (Interacoes pInteracoes : list) {
            try {
                URL url = new URL(URLs.INTERACOES);
                JSONObject main = new JSONObject();
                try {
                    main.put("IdChamado", pInteracoes.getChamadoID());
                    main.put("Descricao", Util_IO.trataString(pInteracoes.getDescricao()));
                    main.put("DataCadastro", Util_IO.dateTimeToString(pInteracoes.getDataCadastro(), Config.FormatDateTimeStringBanco));
                    main.put("IdVendedor", pInteracoes.getIdUsuario());
                    main.put("IdAppMobile", pInteracoes.getIdAppMobile());
                    main.put("DataAppMobile", Util_IO.dateTimeToString(pInteracoes.getDataAppMobile(), Config.FormatDateTimeStringBanco));
                    main.put("ChamadoMotivoId", pInteracoes.getIdCallReason());
                } catch (NoSuchMethodError ex) {
                    ex.printStackTrace();
                    continue;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String response = Utilidades.putRegistros(url, main.toString());
                if (response != null) {
                    if (response.equals("1"))
                        dbChamados.updataInteracoesSync(pInteracoes.getIdAppMobile());
                    else
                        new DBSuporte(pContext).addErroSync(null, "ChamadosInteracoes", pInteracoes.getIdAppMobile());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarChamados(Context pContext) {
        DBChamados dbChamados = new DBChamados(pContext);
        ArrayList<Chamado> list = dbChamados.getChamadosPendentes();
        for (Chamado pChamado : list) {
            try {
                if (pChamado.getChamadoID() == null || pChamado.getChamadoID() == 0) {
                    URL url = new URL(URLs.CHAMADOS);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("FilialID", pChamado.getFilialID());
                        main.put("DepartamentoID", pChamado.getDepartamentoID());
                        main.put("SolicitanteID", pChamado.getSolicitanteID());
                        main.put("Assunto", Util_IO.trataString(pChamado.getAssunto()));
                        main.put("DataCadastro", Util_IO.dateTimeToString(pChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
                        main.put("IdAppMobile", pChamado.getIdAppMobile());
                        main.put("DataAppMobile", Util_IO.dateTimeToString(pChamado.getDataAppMobile(), Config.FormatDateTimeStringBanco));
                    } catch (NoSuchMethodError ex) {
                        ex.printStackTrace();
                        continue;
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    String response = Utilidades.putRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbChamados.updataChamadosSync(pChamado.getIdAppMobile());
                } else {
                    String urlfinal = URLs.CHAMADOS + String.format("?idChamado=%s&situacao=%s&dataAgendamento=%s"
                            , String.valueOf(pChamado.getChamadoID()), String.valueOf(pChamado.getStatusID())
                            , Util_IO.dateTimeToString(pChamado.getDataAgendamento(), "yyyy-MM-dd'T'HH:mm:ss"));

                    int valor = Utilidades.getObject(urlfinal, int.class);
                    if (valor == 1)
                        dbChamados.updataChamadosSync(pChamado.getIdAppMobile());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void getAnexos(int pTipoCarga, Context pContext) {
        try {
            DBChamados dbChamados = new DBChamados(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.ANEXO_CHAMADO + "?idVendedor=" + String.valueOf(colaborador.getId()) + "&tipoCarga=" + String.valueOf(pTipoCarga);
            AnexoChamado[] array = Utilidades.getArrayObject(urlfinal, AnexoChamado[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (AnexoChamado anexoChamado : array) {
                    Bitmap bImagem = Utilidades.decodeBase64(anexoChamado.getArquivo());
                    if (bImagem != null) {
                        String localFinal = Utilidades.getFilename(pContext);
                        try {
                            FileOutputStream fos = new FileOutputStream(localFinal);
                            bImagem.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            fos.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                            continue;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            continue;
                        }
                        anexoChamado.setArquivo(localFinal);
                        if (dbChamados.addAnexosWebService(anexoChamado))
                            idList.add(anexoChamado.getAnexoID());
                    }
                    setSync(URLs.ANEXO_CHAMADO, idList, colaborador.getId());
                }
            }
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @SuppressWarnings("TryWithIdenticalCatches")
//    private static void setSyncAnexos(int pIdServer, int pIdColaborador) {
//        try {
//            String urlfinal = URLs.ANEXO_CHAMADO + "?idAnexo=" + String.valueOf(pIdServer) + "&idVendedor=" + String.valueOf(pIdColaborador);
//            Utilidades.getObject(urlfinal, int.class);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void enviarAnexos(Context pContext) {
        try {
            DBChamados dbChamados = new DBChamados(pContext);
            ArrayList<AnexoChamado> list = dbChamados.getAnexosPendentes();
            for (AnexoChamado pAnexoChamado : list) {
                Bitmap bImagem = BitmapFactory.decodeFile(pAnexoChamado.getArquivo());
                if (bImagem != null) {
                    URL url = new URL(URLs.ANEXO_CHAMADO);

                    JSONObject main = new JSONObject();
                    try {
                        main.put("Id", "");
                        main.put("AnexoID", "");
                        main.put("InteracaoID", pAnexoChamado.getInteracaoID());
                        main.put("ChamadoID", pAnexoChamado.getChamadoID());
                        main.put("Arquivo", Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                        main.put("Nome", pAnexoChamado.getNome());
                        main.put("Tipo", pAnexoChamado.getTipo());
                        main.put("DataCadastro", Util_IO.dateTimeToString(pAnexoChamado.getDataCadastro(), Config.FormatDateTimeStringBanco));
                        main.put("IdAppMobile", pAnexoChamado.getIdAppMobile());
                        main.put("DataAppMobile", Util_IO.dateTimeToString(pAnexoChamado.getDataAppMobile(), Config.FormatDateTimeStringBanco));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (OutOfMemoryError ex) {
                        ex.printStackTrace();
                        continue;
                    }

                    String response = Utilidades.putRegistros(url, main.toString());
                    if (response != null && response.equals("1"))
                        dbChamados.updataAnexoSync(pAnexoChamado.getIdAppMobile());
                } else
                    dbChamados.deleteAnexo(String.valueOf(pAnexoChamado.getIdAppMobile()), pAnexoChamado.getArquivo());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
    }
}