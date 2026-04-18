package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBBancos {
    private Context mContext;
    private String mTabela = "Bancos";

    public DBBancos(Context pContext) {
        mContext = pContext;
    }

    public void addBanco(Bancos pBancos) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pBancos.getDescricao());
        values.put("situacao", pBancos.getSituacao());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", pBancos.getId())) {
            values.put("id", pBancos.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pBancos.getId()});
    }

    public Bancos getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getBancos(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<Bancos> getBancos(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[situacao]");
        sb.appendLine("FROM [Bancos]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Bancos> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Bancos bancos;
            if (cursor.moveToFirst()) {
                do {
                    bancos = new Bancos();
                    bancos.setId(cursor.getString(0));
                    bancos.setDescricao(cursor.getString(1));
                    bancos.setSituacao(cursor.getString(2));
                    lista.add(bancos);
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

    public ArrayList<Bancos> getBancos() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [situacao] = 'A'");

        return getBancos(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}