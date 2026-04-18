package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.FaixaPatrimonial;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBFaixaPatrimonial {
    private Context mContext;
    private String mTabela = "FaixaPatrimonial";

    public DBFaixaPatrimonial(Context pContext) {
        mContext = pContext;
    }

    public void addFaixaPatrimonial(FaixaPatrimonial pFaixaPatrimonial) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pFaixaPatrimonial.getDescricao());
        values.put("situacao", pFaixaPatrimonial.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pFaixaPatrimonial.getId()))) {
            values.put("id", pFaixaPatrimonial.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pFaixaPatrimonial.getId())});
    }

    public FaixaPatrimonial getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getFaixaPatrimonial(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<FaixaPatrimonial> getFaixaPatrimonial(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaPatrimonial]");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<FaixaPatrimonial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            FaixaPatrimonial faixapatrimonial;
            if (cursor.moveToFirst()) {
                do {
                    faixapatrimonial = new FaixaPatrimonial();
                    faixapatrimonial.setId(cursor.getInt(0));
                    faixapatrimonial.setDescricao(cursor.getString(1));
                    faixapatrimonial.setSituacao(cursor.getString(2));
                    lista.add(faixapatrimonial);
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

    public ArrayList<FaixaPatrimonial> getFaixaPatrimonial() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [FaixaPatrimonial]");
        sb.appendLine("WHERE Situacao = 'A'");

        Cursor cursor = null;
        ArrayList<FaixaPatrimonial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), null);
            FaixaPatrimonial faixapatrimonial;
            if (cursor.moveToFirst()) {
                do {
                    faixapatrimonial = new FaixaPatrimonial();
                    faixapatrimonial.setId(cursor.getInt(0));
                    faixapatrimonial.setDescricao(cursor.getString(1));
                    faixapatrimonial.setSituacao(cursor.getString(2));
                    lista.add(faixapatrimonial);
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
