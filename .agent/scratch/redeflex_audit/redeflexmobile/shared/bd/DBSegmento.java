package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBSegmento {
    private final Context mContext;
    private final String mTabela = "Segmento";

    public DBSegmento(Context pContext) {
        mContext = pContext;
    }

    public boolean existeSegmento() {
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery("SELECT id FROM [Segmento]", null)) {
            return (cursor != null && cursor.getCount() > 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void addSegmento(Segmento pSegmento) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pSegmento.getDescricao());
        values.put("situacao", pSegmento.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pSegmento.getCodigo())) {
            values.put("id", pSegmento.getCodigo());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pSegmento.getCodigo()});
    }

    public Segmento getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getSegmentos(sb.toString(), new String[]{pId}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    private ArrayList<Segmento> getSegmentos(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [Segmento]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Segmento> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Segmento segmento;
            if (cursor.moveToFirst()) {
                do {
                    segmento = new Segmento();
                    segmento.setCodigo(cursor.getString(0));
                    segmento.setDescricao(cursor.getString(1));
                    segmento.setSituacao(cursor.getString(2));
                    lista.add(segmento);
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

    public ArrayList<Segmento> getSegmento() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [situacao] = 'A'");
        sb.appendLine("ORDER BY [descricao]");

        return getSegmentos(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}
