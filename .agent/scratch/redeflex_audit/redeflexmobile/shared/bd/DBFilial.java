package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

/**
 * Created by joao.viana on 10/01/2018.
 */

public class DBFilial {
    private Context mContext;
    private String mTabela = "Filial";

    public DBFilial(Context pContext) {
        mContext = pContext;
    }

    public void addFilial(Filial pFilial) throws Exception {
        ContentValues values = new ContentValues();
        values.put("descricao", pFilial.getDescricao());
        values.put("estado", pFilial.getEstado());
        values.put("ativo", pFilial.isAtivo());
        if (!Util_DB.isCadastrado(mContext, mTabela, "id", String.valueOf(pFilial.getId()))) {
            values.put("id", pFilial.getId());
            SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
        } else
            SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{String.valueOf(pFilial.getId())});
    }

    public Filial getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getFilialis(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{String.valueOf(pId)});
    }

    private ArrayList<Filial> getFilialis(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT [id]");
        sb.appendLine(",[descricao]");
        sb.appendLine(",[estado]");
        sb.appendLine(",[ativo]");
        sb.appendLine("FROM [Filial]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<Filial> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), pCampos);
            Filial filial;
            if (cursor.moveToFirst()) {
                do {
                    filial = new Filial();
                    filial.setDescricao(cursor.getString(1));
                    filial.setEstado(cursor.getString(2));
                    filial.setAtivo(Util_IO.numberToBoolean(cursor.getInt(3)));
                    lista.add(filial);
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

    public ArrayList<Filial> getFiliais() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [ativo] = 1");

        return getFilialis(sb.toString(), null);
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }
}