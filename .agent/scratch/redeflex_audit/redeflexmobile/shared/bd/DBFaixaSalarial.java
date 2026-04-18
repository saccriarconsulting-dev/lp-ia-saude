package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.FaixaSalarial;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBFaixaSalarial {
    private Context mContext;
    private String mTabela = "FaixaSalarial";

    public DBFaixaSalarial(Context pContext) {
        mContext = pContext;
    }

    public void addFaixaSalarial(FaixaSalarial pFaixaSalarial) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pFaixaSalarial.getDescricao());
        values.put("situacao", pFaixaSalarial.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pFaixaSalarial.getId()))) {
            values.put("id", pFaixaSalarial.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pFaixaSalarial.getId())});
    }

    public FaixaSalarial getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getFaixaSalarial(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<FaixaSalarial> getFaixaSalarial(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaSalarial]");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<FaixaSalarial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            FaixaSalarial faixasalarial;
            if (cursor.moveToFirst()) {
                do {
                    faixasalarial = new FaixaSalarial();
                    faixasalarial.setId(cursor.getInt(0));
                    faixasalarial.setDescricao(cursor.getString(1));
                    faixasalarial.setSituacao(cursor.getString(2));
                    lista.add(faixasalarial);
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

    public ArrayList<FaixaSalarial> getFaixaSalarial() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaSalarial]");
        sb.appendLine("WHERE Situacao = 'A'");

        Cursor cursor = null;
        ArrayList<FaixaSalarial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            FaixaSalarial faixasalarial;
            if (cursor.moveToFirst()) {
                do {
                    faixasalarial = new FaixaSalarial();
                    faixasalarial.setId(cursor.getInt(0));
                    faixasalarial.setDescricao(cursor.getString(1));
                    faixasalarial.setSituacao(cursor.getString(2));
                    lista.add(faixasalarial);
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
