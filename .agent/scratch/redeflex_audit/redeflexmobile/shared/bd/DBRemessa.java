package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Remessa;
import com.axys.redeflexmobile.shared.models.RemessaAnexo;
import com.axys.redeflexmobile.shared.models.RemessaItem;
import com.axys.redeflexmobile.shared.models.RemessaLista;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joao.viana on 29/07/2016.
 */
public class DBRemessa {
    private Context mContext;
    public String mTabelaRemessa = "Remessa";
    public String mTabelaItens = "RemessaItem";

    public DBRemessa(Context pContext) {
        mContext = pContext;
    }

    public void addRemessa(Remessa pRemessa) throws Exception {
        ContentValues values = new ContentValues();
        values.put("id", pRemessa.getId());
        values.put("numero", pRemessa.getNumero());
        values.put("situacao", pRemessa.getSituacao());
        values.put("datageracao", Util_IO.dateTimeToString(pRemessa.getDatageracao(), Config.FormatDateTimeStringBanco));

        if (!Util_DB.isCadastrado(mContext, mTabelaRemessa, "id", String.valueOf(pRemessa.getId()))) {
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaRemessa, null, values);
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaRemessa, values, "[id]=?", new String[]{pRemessa.getId()});
        }

        if (pRemessa.getListaitem() != null && pRemessa.getListaitem().size() > 0) {
            for (RemessaItem item : pRemessa.getListaitem()) {
                values = new ContentValues();
                values.put("id", item.getId());
                values.put("idRemessa", pRemessa.getId());
                values.put("idProduto", item.getItemCodeSAP());
                values.put("qtdRemessa", item.getQtdRemessa());

                if (!Util_DB.isCadastrado(mContext, mTabelaItens, "id", String.valueOf(item.getId()))) {
                    SimpleDbHelper.INSTANCE.open(mContext).insert(mTabelaItens, null, values);
                } else {
                    SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItens, values, "[id]=?", new String[]{item.getId()});
                }
            }
        }
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItens, null, null);
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaRemessa, null, null);
    }

    public void deleteConfirmados() {
        Cursor cursor = null;
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT [id]");
            sb.appendLine(",IFNULL([localArquivo],'')");
            sb.appendLine("FROM [Remessa]");
            sb.appendLine("WHERE 1=1");
            sb.appendLine("AND [dataconfirmacao] < datetime('now', '-30 day') AND sync = 1");
            sb.appendLine("AND (([localArquivo] IS NULL AND [syncArquivo] != 2) OR ([localArquivo] IS NOT NULL AND [syncArquivo] = 4))");
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(1).length() > 0)
                            Utilidades.deletaArquivo(cursor.getString(1));
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItens, "[idRemessa]=? ", new String[]{cursor.getString(0)});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaRemessa, "[id]=? ", new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void deleteCancelados() {
        Cursor cursor = null;
        try {
            Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
            sb.appendLine("SELECT [id]");
            sb.appendLine(",IFNULL([localArquivo],'')");
            sb.appendLine("FROM [Remessa]");
            sb.appendLine("WHERE 1=1");
            sb.appendLine("AND [situacao] = 4");

            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(1).length() > 0)
                            Utilidades.deletaArquivo(cursor.getString(1));
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaItens, "[idRemessa]=? ", new String[]{cursor.getString(0)});
                        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabelaRemessa, "[id]=? ", new String[]{cursor.getString(0)});
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void updateSync(String pTabela, String pId) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(pTabela, values, "[id]=?", new String[]{pId});
    }

    public ArrayList<Remessa> getRemessa(boolean pPendente, String pNumero) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.numero");
        sb.appendLine(",t0.situacao");
        sb.appendLine(",t0.datageracao");
        sb.appendLine(",t0.sync");
        sb.appendLine(",IFNULL(t0.dataconfirmacao,datetime('now','localtime'))");
        sb.appendLine(",IFNULL(t0.syncArquivo,0)");
        sb.appendLine("FROM [Remessa] t0");
        sb.appendLine("WHERE 1=1");
        if (!Util_IO.isNullOrEmpty(pNumero))
            sb.appendLine("AND t0.numero LIKE '%" + pNumero + "%'");
        if (pPendente) {
            sb.appendLine("AND t0.sync = 0");
            sb.appendLine("AND t0.dataconfirmacao IS NOT NULL");
        }
        sb.appendLine("ORDER BY t0.id DESC");

        ArrayList<Remessa> list = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            Remessa remessa;
            if (cursor.moveToFirst()) {
                do {
                    remessa = new Remessa();
                    remessa.setId(cursor.getString(0));
                    remessa.setNumero(cursor.getString(1));
                    if (!pPendente && (cursor.getInt(6) == 3 || cursor.getInt(6) == 2)) {
                        remessa.setSituacao(32);
                    } else
                        remessa.setSituacao(cursor.getInt(2));
                    remessa.setDatageracao(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    remessa.setDataconfirmacao(Util_IO.stringToDate(cursor.getString(5), Config.FormatDateTimeStringBanco));
                    list.add(remessa);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ArrayList<Remessa> getRemessa(boolean pPendente) {
        return getRemessa(pPendente, "");
    }

    public ArrayList<RemessaLista> getRemessaById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.numero");
        sb.appendLine(",t0.situacao");
        sb.appendLine(",t0.datageracao");
        sb.appendLine(",t1.id");
        sb.appendLine(",t1.idProduto");
        sb.appendLine(",t1.qtdRemessa");
        sb.appendLine(",t2.nome");
        sb.appendLine(",IFNULL(t1.qtdInformada,0)");
        sb.appendLine(",IFNULL(t0.localArquivo,'')");
        sb.appendLine(",IFNULL(t0.syncArquivo,0)");
        sb.appendLine("FROM [Remessa] t0");
        sb.appendLine("JOIN [RemessaItem] t1 ON (t0.id = t1.idRemessa)");
        sb.appendLine("LEFT OUTER JOIN [Produto] t2 ON (t2.id = t1.idProduto)");
        sb.appendLine("WHERE t0.id = ?");
        sb.appendLine("ORDER BY t0.id DESC ");

        ArrayList<RemessaLista> lista = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), new String[]{pId});
            RemessaLista obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new RemessaLista();
                    obj.setId_capa(cursor.getString(0));
                    obj.setNumero_capa(cursor.getString(1));
                    obj.setSituacao_capa(cursor.getInt(2));
                    obj.setDatageracao_capa(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    obj.setIdItem_item(cursor.getString(4));
                    obj.setItemCodeSAP_item(cursor.getString(5));
                    obj.setQtdRemessa_item(cursor.getInt(6));
                    obj.setItemDescricao(cursor.getString(7));
                    obj.setQtdInformada_item(cursor.getInt(8));
                    obj.setLocalArquivo(cursor.getString(9));
                    obj.setSituacaoArquivo(cursor.getInt(10));
                    lista.add(obj);
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

    public void updateQtd(String pId, int pQuantidade) {
        ContentValues values = new ContentValues();
        values.put("qtdInformada", pQuantidade);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaItens, values, "[id]=?", new String[]{pId});
    }

    public void updateConfirmacao(String pId, Date pDataConfirmacao, String pLocalArquivo) {
        ContentValues values = new ContentValues();
        values.put("situacao", 3);
        values.put("dataconfirmacao", Util_IO.dateTimeToString(pDataConfirmacao, Config.FormatDateTimeStringBanco));
        values.put("syncArquivo", 0);
        values.put("localArquivo", pLocalArquivo);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaRemessa, values, "[id]=?", new String[]{pId});
    }

    public ArrayList<RemessaItem> getRemessaItem(String remessaId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t1.id");
        sb.appendLine(",t1.idProduto");
        sb.appendLine(",t1.qtdRemessa");
        sb.appendLine(",IFNULL(t0.dataconfirmacao,datetime('now','localtime'))");
        sb.appendLine(",t1.qtdRemessa");
        sb.appendLine(",t1.qtdInformada");
        sb.appendLine("FROM [Remessa] t0");
        sb.appendLine("JOIN [RemessaItem] t1 ON (t0.id = t1.idRemessa)");
        sb.appendLine("WHERE t1.sync = 0 ");

        if (!Util_IO.isNullOrEmpty(remessaId)) {
            sb.appendLine("AND t1.idRemessa = " + remessaId);
        }

        sb.appendLine("AND t0.dataconfirmacao IS NOT NULL");

        Cursor cursor = null;
        ArrayList<RemessaItem> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            RemessaItem obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new RemessaItem();
                    obj.setId(cursor.getString(0));
                    obj.setItemCodeSAP(cursor.getString(1));
                    obj.setDataConfirmacao(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateTimeStringBanco));
                    obj.setQtdRemessa(cursor.getInt(4));
                    obj.setQtdInformada(cursor.getInt(5));

                    list.add(obj);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public ArrayList<RemessaAnexo> getAnexos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT t0.id");
        sb.appendLine(",t0.numero");
        sb.appendLine(",t0.localArquivo");
        sb.appendLine(",t0.syncArquivo");
        sb.appendLine("FROM [Remessa] t0");
        sb.appendLine("WHERE t0.syncArquivo = 0");
        sb.appendLine("AND t0.localArquivo IS NOT NULL");
        sb.appendLine("AND t0.dataconfirmacao IS NOT NULL");

        Cursor cursor = null;
        ArrayList<RemessaAnexo> list = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            RemessaAnexo obj;
            if (cursor.moveToFirst()) {
                do {
                    obj = new RemessaAnexo();
                    obj.setIdRemessa(cursor.getString(0));
                    obj.setNumeroRemessa(cursor.getString(1));
                    obj.setLocalArquivo(cursor.getString(2));
                    obj.setSituacao(cursor.getString(3));

                    list.add(obj);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public void updataSyncAnexo(String pId, int pSync) {
        ContentValues values = new ContentValues();
        values.put("syncArquivo", pSync);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabelaRemessa, values, "[id]=?", new String[]{pId});
    }

    public void limpaAnexo(String pId) {
        String query = "UPDATE [Remessa] SET localArquivo = NULL, syncArquivo = 2 WHERE id = '" + pId + "'";
        SimpleDbHelper.INSTANCE.open(mContext).execSQL(query);
    }

    public boolean anexoPendente(String pId) {
        Util_IO.StringBuilder sbSql = new Util_IO.StringBuilder();
        sbSql.appendLine("SELECT [id]");
        sbSql.appendLine("FROM [Remessa]");
        sbSql.appendLine("WHERE [syncArquivo] IN (2,3)");
        sbSql.appendLine("AND [id] != ?");
        Cursor cursor = null;

        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sbSql.toString(), new String[]{pId});
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}