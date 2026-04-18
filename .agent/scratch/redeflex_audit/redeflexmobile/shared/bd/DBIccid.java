package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Iccid;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.CodigoBarra;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by joao.viana on 09/02/2017.
 */

public class DBIccid {

    private static final String TABELA = "Iccid";
    private static final String COLUNA_ID_SERVER = "idServer";
    private static final String COLUNA_CODIGO_BARRA = "codigoBarra";
    private static final String COLUNA_CODIGO_SEM_VERIFICADOR = "codigoSemVerificador";
    private static final String COLUNA_ITEM_CODE = "itemCode";
    private Context mContext;

    public DBIccid(Context pContext) {
        mContext = pContext;
    }

    public void addIccid(Iccid pIccid) {
        try {
            long retorno = -1;
            if (pIccid.isAtivo()) {
                ContentValues values = new ContentValues();
                values.put("idServer", pIccid.getId());
                values.put("codigoBarra", pIccid.getCodigo());
                values.put("codigoSemVerificador", pIccid.getCodigoSemVerificador());
                values.put("itemCode", pIccid.getItemCode());
                if (!Util_DB.isCadastrado(mContext, TABELA, "codigoBarra", pIccid.getCodigo()))
                    retorno = SimpleDbHelper.INSTANCE.open(mContext).insert(TABELA, null, values);
                else
                    retorno = SimpleDbHelper.INSTANCE.open(mContext).update(TABELA, values, "[codigoBarra]=?", new String[]{pIccid.getCodigo()});
            } else
                SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, "[codigoBarra]=?", new String[]{pIccid.getCodigo()});
        }
        catch (Exception ex)
        {
            Log.d("Roni", "addIccid: Error --> " + ex.getMessage());
        }
    }

    public Iccid getByCodigo(String pCodigo) {
        if (!Util_IO.isNullOrEmpty(pCodigo)) {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT idServer");
            sb.appendLine(",codigoBarra");
            sb.appendLine(",codigoSemVerificador");
            sb.appendLine(",itemCode");
            sb.appendLine("FROM [Iccid]");
            sb.appendLine("WHERE codigoBarra = '" + pCodigo + "' ");
            Cursor cursor = null;
            try {
                cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    Iccid iccid = new Iccid();
                    iccid.setId(String.valueOf(cursor.getInt(0)));
                    iccid.setCodigo(cursor.getString(1));
                    iccid.setCodigoSemVerificador(cursor.getString(2));
                    iccid.setItemCode(cursor.getString(3));
                    iccid.setAtivo(true);
                    return iccid;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, null, null);
    }

    public void deletaIccid(int pIdVenda) {
        try {
            ArrayList<CodBarra> listCodBarra = new DBVenda(mContext).listCodigosbyVenda(pIdVenda);
            ArrayList<Iccid> listIccid;
            if (listCodBarra != null && listCodBarra.size() > 0) {
                for (CodBarra codigo : listCodBarra) {
                    listIccid = CodigoBarra.listaICCID(codigo);
                    if (listIccid != null && listIccid.size() > 0) {
                        for (Iccid item : listIccid) {
                            SimpleDbHelper.INSTANCE.open(mContext)
                                    .delete(
                                            TABELA,
                                            "[codigoSemVerificador]=?",
                                            new String[]{item.getCodigoSemVerificador()}
                                    );
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletaIccidConsignado(int pIdConsignado) {
        try {
            ArrayList<CodBarra> listCodBarra = new BDConsignado(mContext).listCodigosbyConsignado(pIdConsignado);
            ArrayList<Iccid> listIccid;
            if (listCodBarra != null && listCodBarra.size() > 0) {
                for (CodBarra codigo : listCodBarra) {
                    listIccid = CodigoBarra.listaICCID(codigo);
                    if (listIccid != null && listIccid.size() > 0) {
                        for (Iccid item : listIccid) {
                            SimpleDbHelper.INSTANCE.open(mContext)
                                    .delete(
                                            TABELA,
                                            "[codigoSemVerificador]=?",
                                            new String[]{item.getCodigoSemVerificador()}
                                    );
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletaIccidAuditagemEstoque(int pIdVenda) {
        try {
            ArrayList<CodBarra> listCodBarra = new DBVenda(mContext).listCodigosbyVenda(pIdVenda);
            ArrayList<Iccid> listIccid;
            if (listCodBarra != null && listCodBarra.size() > 0) {
                for (CodBarra codigo : listCodBarra) {
                    listIccid = CodigoBarra.listaICCID(codigo);
                    if (listIccid != null && listIccid.size() > 0) {
                        for (Iccid item : listIccid) {
                            String compareField = codigo.getGrupoProduto() == CodigoBarra.PRODUTO_FISICO
                                    ? "codigoBarra"
                                    : "codigoSemVerificador";

                            SimpleDbHelper.INSTANCE.open(mContext)
                                    .delete(
                                            TABELA,
                                            String.format("[%s]=?", compareField),
                                            new String[]{item.getCodigoSemVerificador()}
                                    );
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> obterListaCodigoBarraPorIccid(String itemCode) {

        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT ");
        sb.appendLine("codigoBarra ");
        sb.appendLine("FROM [Iccid] ");
        sb.appendLine("WHERE itemCode = '" + itemCode + "'");

        Cursor cursor = null;
        List<String> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Cliente cliente;
            if (cursor.moveToFirst()) {
                do {
                    lista.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return lista;
    }

    public List<CodBarra> obterListaCodigoBarraPorProduto(String idProduto, int idGrupo) {
        String sql = String.format(
                "SELECT %s, %s, %s, %s FROM [%s] WHERE %s = ?",
                COLUNA_ID_SERVER,
                COLUNA_CODIGO_BARRA,
                COLUNA_CODIGO_SEM_VERIFICADOR,
                COLUNA_ITEM_CODE,
                TABELA,
                COLUNA_ITEM_CODE
        );

        List<CodBarra> iccids = new LinkedList<>();
        try (Cursor cursor = SimpleDbHelper.INSTANCE
                .open(mContext)
                .rawQuery(sql, new String[]{idProduto})) {

            while (cursor.moveToNext()) {
                CodBarra codBarra = new CodBarra();
                codBarra.setCodBarraInicial(cursor.getString(1));
                codBarra.setIdProduto(idProduto);
                codBarra.setIndividual(true);
                codBarra.setGrupoProduto(idGrupo);
                iccids.add(codBarra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return iccids;
    }

    public void deletaIccidPorCodBarra(String codBarra) {
        try {
            SimpleDbHelper.INSTANCE.open(mContext).delete(TABELA, "[codigoBarra]=?", new String[]{codBarra});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}