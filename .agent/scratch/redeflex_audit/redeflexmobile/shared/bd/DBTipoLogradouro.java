package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.TipoLogradouro;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBTipoLogradouro {
    private Context mContext;
    private String mTabela = "TipoLogradouro";

    public DBTipoLogradouro(Context pContext) {
        mContext = pContext;
    }

    public void addTipoLogradouro(TipoLogradouro tipoLogradouro) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", tipoLogradouro.getDescricao());
        values.put("situacao", tipoLogradouro.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(tipoLogradouro.getId()))) {
            values.put("id", tipoLogradouro.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(tipoLogradouro.getId())});
    }

    public TipoLogradouro getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getTipoLogradouro(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<TipoLogradouro> getTipoLogradouro(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [TipoLogradouro]");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<TipoLogradouro> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            TipoLogradouro tipoLogradouro;
            if (cursor.moveToFirst()) {
                do {
                    tipoLogradouro = new TipoLogradouro();
                    tipoLogradouro.setId(cursor.getInt(0));
                    tipoLogradouro.setDescricao(cursor.getString(1));
                    tipoLogradouro.setSituacao(cursor.getString(2));
                    lista.add(tipoLogradouro);
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

    public ArrayList<TipoLogradouro> getTipoLogradouro() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [TipoLogradouro]");
        sb.appendLine("WHERE Situacao = 'A'");

        Cursor cursor = null;
        ArrayList<TipoLogradouro> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            TipoLogradouro tipoLogradouro;
            if (cursor.moveToFirst()) {
                do {
                    tipoLogradouro = new TipoLogradouro();
                    tipoLogradouro.setId(cursor.getInt(0));
                    tipoLogradouro.setDescricao(cursor.getString(1));
                    tipoLogradouro.setSituacao(cursor.getString(2));
                    lista.add(tipoLogradouro);
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

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}
